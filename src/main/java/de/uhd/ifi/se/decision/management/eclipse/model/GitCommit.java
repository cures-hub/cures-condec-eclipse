package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.List;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;

import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public interface GitCommit extends Node {

	void setIssueKeyBase(String issueKeyBase);

	// ReferencedIssues-Section
	List<Issue> getReferencedJiraIssues();

	void setReferencedJiraIssues(List<Issue> referencedIssues);

	boolean addReferencedIssue(Issue issue);

	boolean removeReferencedIssue(Issue issue);
	// END ReferencedIssues-Section

	// CommitDecision-Section
	List<DecisionKnowledgeElementImpl> getCommitDecisions();

	void setCommitDecisions(List<DecisionKnowledgeElementImpl> decisionKnowledgeElements);

	boolean addCommitDecision(DecisionKnowledgeElementImpl decisionKnowledgeElement);

	boolean removeCommitDecision(DecisionKnowledgeElementImpl decisionKnowledgeElement);
	// END CommitDecision-Section

	// ChangedClasses-Section
	List<CodeClassImpl> getChangedClasses();

	void setChangedClasses(List<CodeClassImpl> changedClasses);

	boolean addChangedClass(CodeClassImpl changedClass);

	boolean removeChangedClass(CodeClassImpl changedClass);
	// END CHangedClasses-Section

	// IssueKey-Section
	List<String> getJiraIssueKeys();

	Set<String> getIssueKeysAsSet();

	void setJiraIssueKeys(List<String> issueKeys);

	boolean addJiraIssueKey(String issueKey);

	boolean removeIssueKey(String issueKey);
	// END IssueKey-Section

	RevCommit getBindedRevCommit();

	void extractChangedClasses(GitClient gm);

}