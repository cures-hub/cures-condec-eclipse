package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class GitCommitImpl extends NodeImpl implements Node, GitCommit {
	private List<DecisionKnowledgeElement> decisionKnowledgeElements;
	private List<String> issueKeys = new ArrayList<String>();
	private List<Issue> referencedIssues = new ArrayList<Issue>();
	private List<CodeClass> changedClasses = new ArrayList<CodeClass>();
	private RevCommit revCommit;
	private String issueKeyBase = "";
	private static Map<RevCommit, GitCommitImpl> instances = new HashMap<RevCommit, GitCommitImpl>();

	public static Set<GitCommitImpl> getInstances() {
		Set<GitCommitImpl> output = new HashSet<GitCommitImpl>();
		for (Map.Entry<RevCommit, GitCommitImpl> entry : instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	public static GitCommitImpl getOrCreate(RevCommit commit, String issueKeyBase) {
		if (instances.containsKey(commit)) {
			return instances.get(commit);
		} else {
			return new GitCommitImpl(commit, issueKeyBase);
		}
	}

	private GitCommitImpl(RevCommit commit) {
		this(commit, null);
	}

	private GitCommitImpl(RevCommit commit, String issueKeyBase) {
		instances.put(commit, this);
		this.revCommit = commit;
		this.issueKeyBase = issueKeyBase;
		this.decisionKnowledgeElements = CommitMessageParser.extractDecisionKnowledgeFromCommit(commit);
		for (DecisionKnowledgeElement knowledgeElement : decisionKnowledgeElements) {
			this.addLinkedNode(knowledgeElement);
			knowledgeElement.addLinkedNode(this);
		}
		this.issueKeys = CommitMessageParser.getAllMentionedIssueKeys(commit.getFullMessage(), this.issueKeyBase);
	}

	@Override
	public void setIssueKeyBase(String issueKeyBase) {
		this.issueKeyBase = issueKeyBase;
	}

	// ReferencedIssues-Section
	@Override
	public List<Issue> getReferencedJiraIssues() {
		return this.referencedIssues;
	}

	@Override
	public void setReferencedJiraIssues(List<Issue> referencedIssues) {
		this.referencedIssues = referencedIssues;
	}

	@Override
	public boolean addReferencedJiraIssue(Issue issue) {
		if (this.referencedIssues.contains(issue)) {
			return false;
		} else {
			return this.referencedIssues.add(issue);
		}
	}

	@Override
	public boolean removeReferencedJiraIssue(Issue issue) {
		return this.referencedIssues.remove(issue);
	}
	// END ReferencedIssues-Section

	// CommitDecision-Section
	@Override
	public List<DecisionKnowledgeElement> getCommitDecisions() {
		return this.decisionKnowledgeElements;
	}

	@Override
	public void setCommitDecisions(List<DecisionKnowledgeElement> decisionKnowledgeElements) {
		this.decisionKnowledgeElements = decisionKnowledgeElements;
	}

	@Override
	public boolean addCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement) {
		return this.decisionKnowledgeElements.add(decisionKnowledgeElement);
	}

	@Override
	public boolean removeCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement) {
		return this.decisionKnowledgeElements.remove(decisionKnowledgeElement);
	}
	// END CommitDecision-Section

	// ChangedClasses-Section
	@Override
	public List<CodeClass> getChangedClasses() {
		return this.changedClasses;
	}

	@Override
	public void setChangedClasses(List<CodeClass> changedClasses) {
		this.changedClasses = changedClasses;
	}

	@Override
	public boolean addChangedClass(CodeClass changedClass) {
		return this.changedClasses.add(changedClass);
	}

	@Override
	public boolean removeChangedClass(CodeClass changedClass) {
		return this.changedClasses.remove(changedClass);
	}
	// END CHangedClasses-Section

	// IssueKey-Section
	@Override
	public List<String> getJiraIssueKeys() {
		return this.issueKeys;
	}

	@Override
	public void setJiraIssueKeys(List<String> issueKeys) {
		this.issueKeys = issueKeys;
	}

	@Override
	public boolean addJiraIssueKey(String issueKey) {
		return this.issueKeys.add(issueKey);
	}

	@Override
	public boolean removeJiraIssueKey(String issueKey) {
		return this.issueKeys.remove(issueKey);
	}
	// END IssueKey-Section

	@Override
	public RevCommit getRevCommit() {
		return this.revCommit;
	}

	@Override
	public String toString() {
		return this.revCommit.getShortMessage();
	}

	@Override
	public void extractChangedClasses(GitClient gm) {
		this.changedClasses = gm.getDiffEntries(this);
		for (Node node : this.changedClasses) {
			this.addLinkedNode(node);
			node.addLinkedNode(this);
		}
	}
}