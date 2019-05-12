package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.List;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;

import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElement;

public interface GitCommit {

	void setIssueKeyBase(String issueKeyBase);

	// ReferencedIssues-Section
	List<Issue> getReferencedJiraIssues();

	void setReferencedJiraIssues(List<Issue> referencedIssues);

	boolean addReferencedIssue(Issue issue);

	boolean removeReferencedIssue(Issue issue);
	// END ReferencedIssues-Section

	// CommitDecision-Section
	List<DecisionKnowledgeElement> getCommitDecisions();

	void setCommitDecisions(List<DecisionKnowledgeElement> decisionKnowledgeElements);

	boolean addCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement);

	boolean removeCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement);
	// END CommitDecision-Section

	// ChangedClasses-Section
	List<CodeClassImpl> getChangedClasses();

	void setChangedClasses(List<CodeClassImpl> changedClasses);

	boolean addChangedClass(CodeClassImpl changedClass);

	boolean removeChangedClass(CodeClassImpl changedClass);
	// END CHangedClasses-Section

	// IssueKey-Section
	List<IssueKey> getJiraIssueKeys();

	Set<IssueKey> getIssueKeysAsSet();

	void setJiraIssueKeys(List<IssueKey> issueKeys);

	boolean addJiraIssueKey(IssueKey issueKey);

	boolean removeIssueKey(IssueKey issueKey);
	// END IssueKey-Section

	RevCommit getBindedRevCommit();

	void extractChangedClasses(GitClient gm);

}