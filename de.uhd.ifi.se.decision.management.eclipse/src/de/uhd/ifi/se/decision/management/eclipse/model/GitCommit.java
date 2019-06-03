package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
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

	void setIssueKeyBase(String issueKeyBase);

	// ReferencedIssues-Section
	List<Issue> getReferencedJiraIssues();

	void setReferencedJiraIssues(List<Issue> referencedIssues);

	boolean addReferencedJiraIssue(Issue issue);

	boolean removeReferencedJiraIssue(Issue issue);
	// END ReferencedIssues-Section

	// CommitDecision-Section
	List<DecisionKnowledgeElement> getCommitDecisions();

	void setCommitDecisions(List<DecisionKnowledgeElement> decisionKnowledgeElements);

	boolean addCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement);

	boolean removeCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement);
	// END CommitDecision-Section

	// ChangedClasses-Section
	List<CodeClass> getChangedClasses();

	void setChangedClasses(List<CodeClass> changedClasses);

	boolean addChangedClass(CodeClass changedClass);

	boolean removeChangedClass(CodeClass changedClass);
	// END CHangedClasses-Section

	// IssueKey-Section
	List<String> getJiraIssueKeys();

	void setJiraIssueKeys(List<String> issueKeys);

	boolean addJiraIssueKey(String issueKey);

	boolean removeJiraIssueKey(String issueKey);
	// END IssueKey-Section

	RevCommit getRevCommit();

	void extractChangedClasses(GitClient gm);

}