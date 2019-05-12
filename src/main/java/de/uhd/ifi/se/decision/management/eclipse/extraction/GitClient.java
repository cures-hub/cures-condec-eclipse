package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.IssueKey;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;

public interface GitClient {

	/**
	 * 
	 * @return Returns a Set with all commits of the current branch.
	 */
	Set<GitCommitImpl> getAllCommits();

	/**
	 * Show what author and revision last modified each line of a file.
	 * 
	 * @param filePath
	 *            path to the file to be blamed
	 * @return git blame result for the given file
	 */
	BlameResult getGitBlameForFile(IPath filePath);

	/**
	 * Retrieve the commit message for a given line from a blamed file as a
	 * RevCommit.
	 * 
	 * @param filePath
	 *            path to the file to be blamed
	 * @param line
	 *            the line that is to be analyzed
	 * @return RevCommit returns all information about the commit as RevCommit
	 */
	GitCommit getCommitForLine(IPath filePath, int line);

	/**
	 * Retrieve the commit message for a given line from a blamed file
	 * 
	 * @param filePath
	 *            path to the file to be blamed
	 * @param line
	 *            the line that is to be analyzed
	 * @return commit message
	 */
	String getCommitMessageForLine(IPath filePath, int line);

	/**
	 * Retrieve the commits with the issue key in their commit message.
	 * 
	 * @param issueKey
	 *            issue key for which commits are searched
	 * @return commits with the issue key in their commit message
	 */
	Set<GitCommitImpl> getCommitsForIssueKey(IssueKey issueKey);

	/**
	 * Get the parent commit for a given commit.
	 * 
	 * @param revCommit
	 *            commit as a RevCommit object
	 * @return parent commit as a RevCommit object
	 */
	RevCommit getParent(RevCommit revCommit);

	/**
	 * Get a list of diff entries for a commit.
	 * 
	 * @param revCommit
	 *            commit as a RevCommit object
	 * @return list of diff entries
	 */
	List<CodeClassImpl> getDiffEntries(GitCommit commit);

	/**
	 * Get a map of diff entries and the respective edit lists for a commit.
	 * 
	 * @param revCommit
	 *            commit as a RevCommit object
	 * @return map of diff entries and respective edit lists
	 */
	Map<DiffEntry, EditList> getDiffEntriesMappedToEditLists(GitCommit commit);

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

	/**
	 * Get the git object identifier, e.g., HEAD, refs/heads/master or commit id
	 * 
	 * @return git object identifier
	 */
	String getReference();

	/**
	 * Set the git object identifier, e.g., HEAD, refs/heads/master or commit id
	 * 
	 * @param reference
	 *            git object identifier
	 */
	void setReference(String reference);

	/**
	 * Get the jgit repository object.
	 * 
	 * @return jgit repository object
	 */
	Repository getRepository();

	/**
	 * Set the jgit repository object.
	 * 
	 * @param repository
	 *            jgit repository object
	 */
	void setRepository(Repository repository);

	/**
	 * Get the jgit git object.
	 * 
	 * @return jgit git object
	 */
	Git getGit();

	/**
	 * Set the jgit git object.
	 * 
	 * @param git
	 *            jgit git object
	 */
	void setGit(Git git);

	/**
	 * Retrieve the commits with the issue key in their commit message.
	 * 
	 * @param issueKey
	 *            issue key for which commits are searched
	 * @return commits with the issue key in their commit message
	 */
	Set<RevCommit> getCommitsForIssueKey(String issueKey);

	/**
	 * Retrieve the commit message for a given line from a blamed file as a
	 * RevCommit.
	 * 
	 * @param filePath
	 *            path to the file to be blamed
	 * @param line
	 *            the line that is to be analyzed
	 * @return RevCommit returns all information about the commit as RevCommit
	 */
	RevCommit getRevCommitForLine(IPath filePath, int line);

	/**
	 * Get a map of diff entries and the respective edit lists for a commit.
	 * 
	 * @param revCommit
	 *            commit as a RevCommit object
	 * @return map of diff entries and respective edit lists
	 */
	Map<DiffEntry, EditList> getDiffEntriesMappedToEditLists(RevCommit revCommit);
}
