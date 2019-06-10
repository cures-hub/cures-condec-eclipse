package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;

/**
 * Interface for git commits as part of the knowledge graph.
 */
public interface GitCommit extends Node {

	/**
	 * Instances of GitCommit that are identified by the SHA-1 identifier.
	 */
	static Map<String, GitCommit> instances = new HashMap<String, GitCommit>();

	/**
	 * Retrieves an existing GitCommit instance or creates a new instance if there
	 * is no instance for the given SHA-1 identifier.
	 * 
	 * @param commit
	 *            RevCommit object.
	 * @param projectKey
	 *            the commit message is parsed for to retrieve linked JIRA issues.
	 * @return either a new or already existing GitCommit instance.
	 */
	public static GitCommit getOrCreate(RevCommit commit, String projectKey) {
		if (instances.containsKey(commit.getName())) {
			return instances.get(commit.getName());
		}
		GitCommit gitCommit = new GitCommitImpl(commit, projectKey);
		instances.put(commit.getName(), gitCommit);
		return gitCommit;
	}

	/**
	 * Returns decision knowledge elements documented in the commit message.
	 * 
	 * @see CommitMessageParser
	 * @return list of all decision knowledge elements explicitly marked in a commit
	 *         message.
	 */
	List<DecisionKnowledgeElement> getDecisionKnowledgeFromMessage();

	/**
	 * Returns the files changed by the commit (both java and other files).
	 * 
	 * @see CodeClass
	 * @return list of changed files.
	 */
	List<CodeClass> getChangedFiles();

	/**
	 * Sets the files changed by the commit (both java and other files). This method
	 * is used in the GitClient.
	 * 
	 * @param changedFiles
	 *            list of changed files.
	 * @see CodeClass
	 * @see GitClient
	 */
	void setChangedFiles(List<CodeClass> changedFiles);

	/**
	 * Returns the JIRA issue keys mentioned in the commit message.
	 * 
	 * @see CommitMessageParser
	 * @return list of the JIRA issue keys mentioned in a commit message.
	 */
	List<String> getJiraIssueKeys();

	/**
	 * Returns the RevCommit object associated with this GitCommit.
	 * 
	 * @see RevCommit
	 * @return object of class {@link RevCommit};
	 */
	RevCommit getRevCommit();
}