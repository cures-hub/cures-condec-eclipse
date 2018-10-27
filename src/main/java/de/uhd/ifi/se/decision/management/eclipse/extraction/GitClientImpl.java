package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * Class to connect git and eclipse
 * 
 * @decision The jgit library is used to access git repositories. 
 * @issue How to access commits related to a JIRA issue?
 * @pro The jGit library is open source and no third party JIRA plug-in needs to be installed
 * @alternative Git repositories are connected via the Git Integration for JIRA Plug-in
 */
public class GitClientImpl implements GitClient { 

	private Repository repository;
	private Git git;
	private String reference;

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

	public GitClientImpl(IPath repositoryPath, String reference) {
		this(repositoryPath.toString(), reference);
	}

	public GitClientImpl(String repositoryPath) {
		this(repositoryPath, "HEAD");
	}

	public GitClientImpl(IPath repositoryPath) {
		this(repositoryPath.toString(), "HEAD");
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
	public RevCommit getCommitForLine(IPath filePath, int line) {
		BlameResult blameResult = getGitBlameForFile(filePath);
		return blameResult.getSourceCommit(line);
	}

	@Override
	public String getCommitMessageForLine(IPath filePath, int line) {
		return getCommitForLine(filePath, line).getFullMessage();
	}

	@Override
	public Set<RevCommit> getCommitsForIssueKey(String issueKey) {
		Set<RevCommit> commitsForIssueKey = new LinkedHashSet<RevCommit>();
		try {
			Iterable<RevCommit> iterable = git.log().call();
			Iterator<RevCommit> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				RevCommit commit = iterator.next();
				if (getIssueKey(commit.getFullMessage()).equals(issueKey)) {
					commitsForIssueKey.add(commit);
				}
			}
		} catch (GitAPIException e) {
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
	public static String getIssueKey(String commitMessage) {
		if (commitMessage.contains(" ")) {
			String[] split = commitMessage.split("[:+ ]");
			return split[0];
		} else {
			return "";
		}
	}

	@Override
	public List<DiffEntry> getDiffEntries(RevCommit revCommit) {
		List<DiffEntry> diffEntries = new ArrayList<DiffEntry>();

		DiffFormatter diffFormatter = getDiffFormater(revCommit);
		try {
			RevCommit parentCommit = this.getParent(revCommit);
			diffEntries = diffFormatter.scan(parentCommit.getTree(), revCommit.getTree());
		} catch (IOException e) {
			e.printStackTrace();
		}
		diffFormatter.close();
		return diffEntries;
	}

	private DiffFormatter getDiffFormater(RevCommit revCommit) {
		DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		diffFormatter.setRepository(this.repository);
		diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		diffFormatter.setDetectRenames(true);
		return diffFormatter;
	}

	@Override
	public Map<DiffEntry, EditList> getDiffEntriesMappedToEditLists(RevCommit revCommit) {
		Map<DiffEntry, EditList> diffEntriesMappedToEditLists = new HashMap<DiffEntry, EditList>();
		List<DiffEntry> diffEntries = new ArrayList<DiffEntry>();

		DiffFormatter diffFormatter = getDiffFormater(revCommit);
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
			return "Diff entry is missing.";
		}

		String newPath = diffEntry.getNewPath();
		if (!newPath.contains(".java")) {
			return "The plugin can only parse .java files.";
		}

		IPath filePath = new Path(this.getRepository().getDirectory().toPath().toString()).removeLastSegments(1).append(newPath);
		System.out.println(filePath);
		File file = filePath.toFile();
		
		if (!file.isFile()) {
			return "File does not exist in file system (anymore).";
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
						methodsToString = methodsToString + "An Insert happened in the method " + methodDeclaration.getNameAsString()
								+ "\n";
					}
				}
		}
		return methodsToString;
	}

	@Override
	public String getReference() {
		return reference;
	}

	@Override
	public void setReference(String reference) {
		this.reference = reference;
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
}