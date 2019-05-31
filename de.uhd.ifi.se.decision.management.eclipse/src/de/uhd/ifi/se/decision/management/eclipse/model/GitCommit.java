package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;

public interface GitCommit extends Node {

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