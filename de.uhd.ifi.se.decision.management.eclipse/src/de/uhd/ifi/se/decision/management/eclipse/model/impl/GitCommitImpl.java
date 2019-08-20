package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;
import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for git commits as part of the knowledge graph.
 */
public class GitCommitImpl extends NodeImpl implements GitCommit {
	private RevCommit revCommit;
	private Set<String> jiraIssueKeys;
	private Set<ChangedFile> changedFiles;
	private Set<DecisionKnowledgeElement> decisionKnowledgeElements;
	private Set<JiraIssue> linkedJiraIssues;

	public GitCommitImpl(RevCommit revCommit, String projectKey) {
		this.revCommit = revCommit;
		if (revCommit == null) {
			return;
		}

		this.jiraIssueKeys = CommitMessageParser.getJiraIssueKeys(revCommit, projectKey);
		this.decisionKnowledgeElements = CommitMessageParser.extractDecisionKnowledge(this);
		this.changedFiles = new HashSet<ChangedFile>();
		this.linkedJiraIssues = new HashSet<JiraIssue>();
	}

	public GitCommitImpl(RevCommit commit) {
		this(commit, null);
	}

	@Override
	public Set<DecisionKnowledgeElement> getDecisionKnowledgeFromMessage() {
		return decisionKnowledgeElements;
	}

	@Override
	public Set<ChangedFile> getChangedFiles() {
		return changedFiles;
	}

	@Override
	public void setChangedFiles(Set<ChangedFile> changedFiles) {
		this.changedFiles = changedFiles;
	}

	@Override
	public Set<String> getJiraIssueKeys() {
		return jiraIssueKeys;
	}

	@Override
	public RevCommit getRevCommit() {
		return revCommit;
	}

	@Override
	public String toString() {
		if (revCommit == null) {
			return "";
		}
		return "Commit: " + revCommit.getShortMessage();
	}
	
	@Override
	public String getFullMessage() {
		if (revCommit == null) {
			return "";
		}
		return revCommit.getFullMessage();
	}

	@Override
	public Set<JiraIssue> getLinkedJiraIssues() {
		return linkedJiraIssues;
	}

	@Override
	public Set<Node> getLinkedNodes() {
		Set<Node> linkedNodes = new HashSet<Node>();
		linkedNodes.addAll(this.getChangedFiles());
		linkedNodes.addAll(this.getDecisionKnowledgeFromMessage());
		linkedNodes.addAll(this.getLinkedJiraIssues());
		return linkedNodes;
	}
}