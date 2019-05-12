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
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.CoreConfig.AutoCRLF;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Class to connect git and eclipse
 * 
 * @decision The jgit library is used to access git repositories.
 * @issue How to access commits related to a JIRA issue?
 * @pro The jGit library is open source and no third party JIRA plug-in needs to
 *      be installed
 * @alternative Git repositories are connected via the Git Integration for JIRA
 *              Plug-in
 */
public class GitClientImpl implements GitClient {

	private Repository repository;
	private Git git;
	private DiffFormatter diffFormatter;

	private static Map<String, GitClient> instances = new HashMap<String, GitClient>();

	public static GitClient getOrCreate() {
		String path = ConfigPersistenceManager.getPathToGit().toString().toLowerCase();
		if (instances.containsKey(path)) {
			return instances.get(path);
		} else {
			GitClient gm = new GitClientImpl();
			instances.put(path, gm);
			return gm;
		}
	}

	/**
	 * Constructor for GitClient class
	 * 
	 * @decision Write a new GitClient constructor
	 * @alternative Extend the RepositoryProvider class of EGit
	 * 
	 * @param repositoryPath
	 *            (required) path to .git folder
	 * @param reference
	 *            (optional) git object identifier, e.g., HEAD, refs/heads/master or
	 *            commit id
	 */
	public GitClientImpl(String repositoryPath, String reference) {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		repositoryBuilder.setMustExist(true);
		repositoryBuilder.setGitDir(new File(repositoryPath));
		try {
			this.repository = repositoryBuilder.build();
			this.repository.resolve(reference);
			this.git = new Git(this.repository);
			StoredConfig config = this.repository.getConfig();
			// @decision Disable system dependent new line statements
			// @issue The internal representation of a file might add system dependent new
			// line statements, for example CR LF in Windows
			config.setEnum(ConfigConstants.CONFIG_CORE_SECTION, null, ConfigConstants.CONFIG_KEY_AUTOCRLF,
					AutoCRLF.TRUE);
			config.save();
		} catch (IOException e) {
			System.err.println("Repository could not be found.");
			e.printStackTrace();
		}
	}

	private GitClientImpl() {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		repositoryBuilder.setMustExist(true);
		repositoryBuilder.setGitDir(ConfigPersistenceManager.getPathToGit().toFile());
		String reference = ConfigPersistenceManager.getBranch();
		if (reference == null || reference.isEmpty()) {
			reference = "HEAD";
		}
		try {
			this.repository = repositoryBuilder.build();
			this.repository.resolve(reference);
			this.git = new Git(this.repository);
			StoredConfig config = this.repository.getConfig();
			// @decision Disable system dependent new line statements
			// @issue The internal representation of a file might add system dependent new
			// line statements, for example CR LF in Windows
			config.setEnum(ConfigConstants.CONFIG_CORE_SECTION, null, ConfigConstants.CONFIG_KEY_AUTOCRLF,
					AutoCRLF.TRUE);
			config.save();
		} catch (IOException e) {
			System.err.println("Repository could not be found.");
			e.printStackTrace();
		}
		this.diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		this.diffFormatter.setRepository(this.repository);
		this.diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		this.diffFormatter.setDetectRenames(true);
	}

	@Override
	public Set<GitCommitImpl> getAllCommits() {
		Set<GitCommitImpl> allCommits = new HashSet<GitCommitImpl>();
		try {
			Iterable<RevCommit> commits = this.git.log().all().call();
			for (RevCommit rc : commits) {
				allCommits.add(GitCommitImpl.getOrCreate(rc, ConfigPersistenceManager.getProjectKey()));
			}
		} catch (Exception e) {
			System.out.println("Failed to load all commits of the current branch");
		}
		return allCommits;
	}

	@Override
	public BlameResult getGitBlameForFile(IPath filePath) {
		BlameResult blameResult;
		try {
			blameResult = git.blame().setFilePath(filePath.toString()).call();
			return blameResult;
		} catch (GitAPIException | NullPointerException e) {
			System.err.println("File could not be found.");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public GitCommit getCommitForLine(IPath filePath, int line) {
		BlameResult blameResult = getGitBlameForFile(filePath);
		return GitCommitImpl.getOrCreate(blameResult.getSourceCommit(line), ConfigPersistenceManager.getProjectKey());
	}

	@Override
	public String getCommitMessageForLine(IPath filePath, int line) {
		return getCommitForLine(filePath, line).getRevCommit().getFullMessage();
	}

	@Override
	public Set<GitCommit> getCommitsForIssueKey(String issueKey) {
		Set<GitCommit> commitsForIssueKey = new LinkedHashSet<GitCommit>();
		try {
			Iterable<RevCommit> iterable = git.log().call();
			Iterator<RevCommit> iterator = iterable.iterator();
			while (iterator.hasNext()) {
              RevCommit revCommit = iterator.next();
              if (getIssueKey(revCommit.getFullMessage()).equals(issueKey)) {
            	  GitCommit commit = GitCommitImpl.getOrCreate(iterator.next(), ConfigPersistenceManager.getProjectKey());
                  commitsForIssueKey.add(commit);
              }
			}
		} catch (GitAPIException | NullPointerException e) {
			System.err.println("Could not retrieve commits for the issue key " + issueKey);
			e.printStackTrace();
		}
		return commitsForIssueKey;
	}
	
	/**
	 * Retrieves the issue key from a commit message
	 * 
	 * @param commitMessage
	 *            a commit message that should contain an issue key
	 * @return extracted issue key
	 */
	public static String getFirstIssueKey(String commitMessage, String issueKeyBase) {
		List<String> keys = getAllMentionedIssueKeys(commitMessage, issueKeyBase);
		if (keys.size() > 0) {
			return keys.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Returns all Issue-Keys which are mentioned in a message.
	 * 
	 * @param commitMessage
	 *            All mentioned IssueKeys must not contain a space. Positive
	 *            Example: "Example-123"
	 * @return List of all mentioned IssueKeys. May contain duplicates; Ordered by
	 *         apperance in message.
	 */
	public static List<String> getAllMentionedIssueKeys(String commitMessage, String issueKeyBase) {
		List<String> keys = new ArrayList<String>();
		commitMessage = commitMessage.replace("\r\n", " ").replace("\n", " ");
		String[] words = commitMessage.toLowerCase().split(" ");
		if (issueKeyBase == null) {
			return keys;
		}
		String basekey = issueKeyBase.toLowerCase();
		for (String word : words) {
			if (word.contains(basekey + "-")) {
				keys.add(word);
			}
		}
		return keys;
	}

	@Override
	public List<CodeClassImpl> getDiffEntries(GitCommit commit) {
		RevCommit revCommit = commit.getRevCommit();
		List<CodeClassImpl> changedClasses = new ArrayList<CodeClassImpl>();
		try {
			RevCommit parentCommit = this.getParent(revCommit);
			List<DiffEntry> entries = this.diffFormatter.scan(parentCommit.getTree(), revCommit.getTree());
			for (DiffEntry entry : entries) {
				changedClasses.add(CodeClassImpl.getOrCreate(entry, ConfigPersistenceManager.getPathToGit().toString()));
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
		CompilationUnit compilationUnit;
		try {
			fileInputStream = new FileInputStream(filePath.toString());
			compilationUnit = JavaParser.parse(fileInputStream); // produces real readable code
			fileInputStream.close();

			MethodVisitor methodVisitor = new MethodVisitor();
			compilationUnit.accept(methodVisitor, null);
			methodDeclarations = methodVisitor.getMethodDeclarations();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String methodsToString = "";
		for (Edit edit : editList) {
			for (MethodDeclaration methodDeclaration : methodDeclarations)
				if (edit.getEndB() >= methodDeclaration.getBegin().get().line) {
					if (edit.getBeginB() <= methodDeclaration.getEnd().get().line) {
						methodsToString = methodsToString + "An Insert happened in the method "
								+ methodDeclaration.getNameAsString() + "\n";
					}
				}
		}
		return methodsToString;
	}

	@Override
	public String getReference() {
		return ConfigPersistenceManager.getBranch();
	}

	@Override
	public void setReference(String reference) {
		// TODO
	}

	@Override
	public Repository getRepository() {
		return repository;
	}

	@Override
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@Override
	public Git getGit() {
		return this.git;
	}

	@Override
	public void setGit(Git git) {
		this.git = git;
	}

	/**
	 * Retrieves the issue key from a commit message
	 * 
	 * @param commitMessage
	 *            a commit message that should contain an issue key
	 * @return extracted issue key
	 */
	public static String getIssueKey(String commitMessage) {
		if (commitMessage.contains(" ")) {
			String[] split = commitMessage.split("[:+ ]");
			return split[0];
		} else {
			return "";
		}
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

}