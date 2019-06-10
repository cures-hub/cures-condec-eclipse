package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;

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
	 * @return either a new or already existing GitCommit instance.
	 */
	public static GitCommit getOrCreate(RevCommit commit, String issueKeyBase) {
		if (instances.containsKey(commit.getName())) {
			return instances.get(commit.getName());
		}
		GitCommit gitCommit = new GitCommitImpl(commit, issueKeyBase);
		instances.put(commit.getName(), gitCommit);
		return new GitCommitImpl(commit, issueKeyBase);
	}

	/**
	 * Retrieves decision knowledge elements documented in the commit message.
	 * 
	 * @see CommitMessageParser
	 * @return list of all decision knowledge elements explicitly marked in a
	 *         message.
	 */
	List<DecisionKnowledgeElement> getDecisionKnowledgeFromMessage();

	List<CodeClass> getChangedClasses();

	void setChangedClasses(List<CodeClass> changedClasses);

	List<String> getJiraIssueKeys();

	/**
	 * Returns the RevCommit object associated with this GitCommit.
	 * 
	 * @see RevCommit
	 * @return object of class {@link RevCommit};
	 */
	RevCommit getRevCommit();
}