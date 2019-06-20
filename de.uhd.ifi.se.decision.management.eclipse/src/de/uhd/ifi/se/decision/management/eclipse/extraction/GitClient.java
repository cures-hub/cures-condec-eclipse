package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Interface to connect to a git repository associated with this Eclipse
 * project. Retrieves commits and code changes (diffs) in git.
 * 
 * @see GitCommit
 */
public interface GitClient {

	/**
	 * Instances of the GitClient that are identified by the path of the git
	 * repository.
	 */
	public Map<IPath, GitClient> instances = new HashMap<IPath, GitClient>();

	/**
	 * Retrieves an existing GitClient instance or creates a new instance if there
	 * is no instance for the given path yet.
	 * 
	 * @return GitClient instance.
	 */
	public static GitClient getOrCreate() {
		IPath path = ConfigPersistenceManager.getPathToGit();
		String reference = ConfigPersistenceManager.getBranch();
		String projectKey = ConfigPersistenceManager.getProjectKey();
		return getOrCreate(path, reference, projectKey);
	}

	/**
	 * Retrieves an existing GitClient instance or creates a new instance if there
	 * is no instance for the given path yet.
	 * 
	 * @param path
	 *            to the .git folder.
	 * @param reference
	 *            git object identifier, e.g., HEAD, refs/heads/master or commit id.
	 * @param projectKey
	 *            of the associated JIRA project.
	 * @return GitClient instance.
	 */
	public static GitClient getOrCreate(IPath path, String reference, String projectKey) {
		if (instances.containsKey(path)) {
			return instances.get(path);
		}
		GitClient gitClient = new GitClientImpl(path, reference, projectKey);
		instances.put(path, gitClient);
		return gitClient;
	}

	/**
	 * Retrieve the commit message for a given line from a blamed file as a
	 * RevCommit.
	 * 
	 * @param filePath
	 *            path to the file to be blamed.
	 * @param line
	 *            the line that is to be analyzed.
	 * @return {@link GitCommit} object.
	 */
	GitCommit getCommitForLine(IPath filePath, int line);

	/**
	 * Retrieve the commit message for a given line from a blamed file.
	 * 
	 * @param filePath
	 *            path to the file to be blamed.
	 * @param line
	 *            the line that is to be analyzed.
	 * @return commit message.
	 */
	String getCommitMessageForLine(IPath filePath, int line);

	/**
	 * Retrieves all commits on the current branch.
	 * 
	 * @return set of all commits on the current branch as a list of
	 *         {@link GitCommit} objects.
	 */
	List<GitCommit> getCommits();

	/**
	 * Retrieve the commits with the JIRA issue key in their commit message.
	 * 
	 * @param jiraIssueKey
	 *            key for which commits are searched.
	 * @return commits with the JIRA issue key in their commit message as a list of
	 *         {@link GitCommit} objects.
	 */
	List<GitCommit> getCommitsForJiraIssue(String jiraIssueKey);

	/**
	 * Get a map of diff entries and the respective edit lists for a commit.
	 * 
	 * @param commit
	 *            as a {@link GitCommit} object.
	 * @return map of diff entries and respective edit lists.
	 */
	Map<DiffEntry, EditList> getDiff(GitCommit commit);

	/**
	 * Returns a set of changed files for a commit.
	 * 
	 * @param commit
	 *            as a {@link GitCommit} object
	 * @return set of {@link ChangedFile} objects.
	 */
	Set<ChangedFile> getChangedFiles(GitCommit commit);

	/**
	 * Get the jgit git object.
	 * 
	 * @return jgit git object.
	 */
	Git getGit();

	/**
	 * Show what author and revision last modified each line of a file.
	 * 
	 * @param filePath
	 *            path to the file to be blamed.
	 * @return git blame result for the given file.
	 */
	BlameResult getGitBlameForFile(IPath filePath);

	/**
	 * Get the parent commit for a given commit.
	 * 
	 * @param commit
	 *            commit as a {@link GitCommit} object.
	 * @return parent commit as a {@link GitCommit} object.
	 */
	GitCommit getParent(GitCommit commit);

	/**
	 * Gets the git object identifier, e.g., HEAD, refs/heads/master or commit id.
	 * 
	 * @return git object identifier.
	 */
	String getReference();

	/**
	 * Get the jgit repository object.
	 * 
	 * @return jgit repository object
	 */
	Repository getRepository();

	/**
	 * Sets the git object identifier, e.g., HEAD, refs/heads/master or commit id.
	 * 
	 * @param reference
	 *            git object identifier.
	 */
	void setReference(String reference);

	/**
	 * Gets the changed methods in a diff entry
	 * 
	 * @param diffEntry
	 *            a git commit diff for one file
	 * @param editList
	 *            a list of changes for the file
	 * @return Changed methods in a diff entry as a String
	 */
	String whichMethodsChanged(DiffEntry diffEntry, EditList editList);
}
