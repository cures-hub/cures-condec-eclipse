package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.CoreConfig.AutoCRLF;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Class to connect to a git repository associated with this Eclipse project.
 * Retrieves commits and code changes (diffs) in git.
 * 
 * @issue How to access the git repository?
 * @decision Write a new GitClient constructor!
 * @alternative Extend the RepositoryProvider class of EGit!
 * 
 * @issue How to access commits related to a JIRA issue?
 * @decision The jgit library is used to access git repositories!
 * @pro The jGit library is open source and no third party JIRA plug-in needs to
 *      be installed.
 */
public class GitClientImpl implements GitClient {

	private Repository repository;
	private Git git;
	private DiffFormatter diffFormatter;
	private String projectKey;

	/**
	 * Constructor for GitClient class. Uses the settings stored in the
	 * ConfigPersistenceManager to set the path, reference, and JIRA project key.
	 */
	public GitClientImpl() {
		this(ConfigPersistenceManager.getPathToGit(), ConfigPersistenceManager.getBranch(),
				ConfigPersistenceManager.getProjectKey());
	}

	/**
	 * Constructor for GitClient class.
	 * 
	 * @param path
	 *            to .git folder.
	 * @param reference
	 *            git object identifier, e.g., HEAD, refs/heads/master or commit id.
	 * @param projectKey
	 *            of the associated JIRA project.
	 */
	public GitClientImpl(IPath path, String reference, String projectKey) {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		repositoryBuilder.setMustExist(true);
		repositoryBuilder.setGitDir(path.toFile());
		try {
			this.repository = repositoryBuilder.build();
			this.setReference(reference);
			this.git = new Git(this.repository);
			StoredConfig config = this.repository.getConfig();
			// @issue The internal representation of a file might add system dependent new
			// line statements, for example CR LF in Windows. How to deal with different
			// line endings?
			// @decision Disable system dependent new line statements!
			config.setEnum(ConfigConstants.CONFIG_CORE_SECTION, null, ConfigConstants.CONFIG_KEY_AUTOCRLF,
					AutoCRLF.TRUE);
			config.save();
		} catch (IOException e) {
			System.err.println("Repository could not be found.");
		}
		this.diffFormatter = initDiffFormatter(repository);
		this.projectKey = projectKey;
	}

	private DiffFormatter initDiffFormatter(Repository repository) {
		DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		if (repository != null) {
			diffFormatter.setRepository(repository);
			diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
			diffFormatter.setDetectRenames(true);
		}
		return diffFormatter;
	}

	@Override
	public Set<GitCommit> getCommits() {
		Set<GitCommit> commits = new HashSet<GitCommit>();
		try {
			Iterable<RevCommit> iterable = git.log().call();
			Iterator<RevCommit> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				GitCommit commit = GitCommit.getOrCreate(iterator.next(), projectKey);
				commits.add(commit);
			}
		} catch (GitAPIException | NullPointerException e) {
			System.err.println("Could not retrieve the commits of the repository. Message: " + e);
		}
		return commits;
	}

	@Override
	public BlameResult getGitBlameForFile(IPath filePath) {
		BlameResult blameResult = null;
		try {
			blameResult = git.blame().setFilePath(filePath.toString()).call();
		} catch (GitAPIException | NullPointerException e) {
			System.err.println("File could not be found.");
			e.printStackTrace();
		}
		return blameResult;
	}

	@Override
	public GitCommit getCommitForLine(IPath filePath, int line) {
		BlameResult blameResult = getGitBlameForFile(filePath);
		if (blameResult == null) {
			return null;
		}
		return GitCommit.getOrCreate(blameResult.getSourceCommit(line), projectKey);
	}

	@Override
	public String getCommitMessageForLine(IPath filePath, int line) {
		return getCommitForLine(filePath, line).getRevCommit().getFullMessage();
	}

	@Override
	public Set<GitCommit> getCommitsForJiraIssue(String issueKey) {
		Set<GitCommit> commitsForJiraIssue = new HashSet<GitCommit>();
		for (GitCommit commit : getCommits()) {
			if (CommitMessageParser.getIssueKey(commit).equalsIgnoreCase(issueKey)) {
				commitsForJiraIssue.add(commit);
			}
		}
		return commitsForJiraIssue;
	}

	@Override
	public List<ChangedFile> getDiffEntries(GitCommit commit) {
		RevCommit revCommit = commit.getRevCommit();
		List<ChangedFile> changedClasses = new ArrayList<ChangedFile>();
		IPath pathToGit = ConfigPersistenceManager.getPathToGit();
		try {
			RevCommit parentCommit = this.getParent(revCommit);
			List<DiffEntry> entries = this.diffFormatter.scan(parentCommit.getTree(), revCommit.getTree());
			for (DiffEntry entry : entries) {
				changedClasses.add(ChangedFile.getOrCreate(entry, pathToGit));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {

		}
		// this.diffFormatter.close();
		return changedClasses;
	}

	@Override
	public Map<DiffEntry, EditList> getDiffEntriesMappedToEditLists(GitCommit commit) {
		RevCommit revCommit = commit.getRevCommit();
		Map<DiffEntry, EditList> diffEntriesMappedToEditLists = new HashMap<DiffEntry, EditList>();
		List<DiffEntry> diffEntries = new ArrayList<DiffEntry>();
		try {
			RevCommit parentCommit = this.getParent(revCommit);
			diffEntries = this.diffFormatter.scan(parentCommit.getTree(), revCommit.getTree());
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (DiffEntry diffEntry : diffEntries) {
			try {
				EditList editList = this.diffFormatter.toFileHeader(diffEntry).toEditList();
				diffEntriesMappedToEditLists.put(diffEntry, editList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// this.diffFormatter.close();
		return diffEntriesMappedToEditLists;
	}

	@Override
	public RevCommit getParent(RevCommit revCommit) {
		RevCommit parentCommit;
		try {
			RevWalk revWalk = new RevWalk(repository);
			parentCommit = revWalk.parseCommit(revCommit.getParent(0).getId());
			revWalk.close();
		} catch (IOException e) {
			System.err.println("Could not get the parent commit for " + revCommit);
			e.printStackTrace();
			return null;
		}
		return parentCommit;
	}

	@Override
	public String whichMethodsChanged(DiffEntry diffEntry, EditList editList) {

		if (diffEntry == null) {
			return "Diff entry is missing.\n";
		}

		String newPath = diffEntry.getNewPath();
		if (!newPath.endsWith(".java")) {
			return "The plugin can only parse *.java files.\n";
		}

		IPath filePath = new Path(this.getRepository().getDirectory().toPath().toString()).removeLastSegments(1)
				.append(newPath);
		System.out.println(filePath);
		File file = filePath.toFile();

		if (!file.isFile()) {
			return "File does not exist in file system (anymore).\n";
		}

		Set<MethodDeclaration> methodDeclarations = new LinkedHashSet<MethodDeclaration>();

		FileInputStream fileInputStream;
		ParseResult<CompilationUnit> parseResult;
		try {
			fileInputStream = new FileInputStream(filePath.toString());
			JavaParser javaParser = new JavaParser();
			parseResult = javaParser.parse(fileInputStream); // produces real readable code
			fileInputStream.close();

			CompilationUnit compilationUnit = parseResult.getResult().get();
			MethodVisitor methodVisitor = new MethodVisitor();
			compilationUnit.accept(methodVisitor, null);
			methodDeclarations = methodVisitor.getMethodDeclarations();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String methodsToString = "";
		for (Edit edit : editList) {
			for (MethodDeclaration methodDeclaration : methodDeclarations)
				if (edit.getEndB() >= methodDeclaration.getBegin().get().line
						&& edit.getBeginB() <= methodDeclaration.getEnd().get().line) {
					methodsToString = methodsToString + "An Insert happened in the method "
							+ methodDeclaration.getNameAsString() + "\n";
				}
		}
		return methodsToString;
	}

	@Override
	public String getReference() {
		try {
			return this.repository.getFullBranch();
		} catch (IOException e) {
			System.err.println("Branch name could not be retrieved. Message: " + e);
		}
		return "HEAD";
	}

	@Override
	public Repository getRepository() {
		return repository;
	}

	@Override
	public Git getGit() {
		return this.git;
	}

	@Override
	public RevCommit getRevCommitForLine(IPath filePath, int line) {
		BlameResult blameResult = getGitBlameForFile(filePath);
		return blameResult.getSourceCommit(line);
	}

	@Override
	public Map<DiffEntry, EditList> getDiffEntriesMappedToEditLists(RevCommit revCommit) {
		Map<DiffEntry, EditList> diffEntriesMappedToEditLists = new HashMap<DiffEntry, EditList>();
		List<DiffEntry> diffEntries = new ArrayList<DiffEntry>();

		DiffFormatter diffFormatter = getDiffFormater();
		try {
			RevCommit parentCommit = this.getParent(revCommit);
			diffEntries = diffFormatter.scan(parentCommit.getTree(), revCommit.getTree());
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (DiffEntry diffEntry : diffEntries) {
			try {
				EditList editList = diffFormatter.toFileHeader(diffEntry).toEditList();
				diffEntriesMappedToEditLists.put(diffEntry, editList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		diffFormatter.close();
		return diffEntriesMappedToEditLists;
	}

	private DiffFormatter getDiffFormater() {
		DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		diffFormatter.setRepository(this.repository);
		diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		diffFormatter.setDetectRenames(true);
		return diffFormatter;
	}

	@Override
	public void setReference(String reference) {
		try {
			if (reference == null || reference.isEmpty()) {
				this.repository.resolve("HEAD");
			} else {
				this.repository.resolve(reference);
			}
		} catch (RevisionSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
}