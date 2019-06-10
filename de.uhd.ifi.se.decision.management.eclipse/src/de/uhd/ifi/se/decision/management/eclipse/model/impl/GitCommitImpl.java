package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class GitCommitImpl extends NodeImpl implements Node, GitCommit {
	private RevCommit revCommit;
	private List<String> jiraIssueKeys = new ArrayList<String>();
	private List<CodeClass> changedClasses;
	private List<DecisionKnowledgeElement> decisionKnowledgeElements;
	

	public GitCommitImpl(RevCommit revCommit, String projectKey) {
		this.revCommit = revCommit;
		this.jiraIssueKeys = CommitMessageParser.getJiraIssueKeys(revCommit, projectKey);
		
		this.decisionKnowledgeElements = CommitMessageParser.extractDecisionKnowledge(revCommit);
		for (DecisionKnowledgeElement knowledgeElement : decisionKnowledgeElements) {
			this.addLinkedNode(knowledgeElement);
			knowledgeElement.addLinkedNode(this);
		}
		
		this.changedClasses = new ArrayList<CodeClass>();
	}

	public GitCommitImpl(RevCommit commit) {
		this(commit, null);
	}

	@Override
	public List<DecisionKnowledgeElement> getDecisionKnowledgeFromMessage() {
		return this.decisionKnowledgeElements;
	}

	@Override
	public List<CodeClass> getChangedClasses() {
		return this.changedClasses;
	}

	@Override
	public void setChangedClasses(List<CodeClass> changedClasses) {
		this.changedClasses = changedClasses;
		for (Node node : this.changedClasses) {
			this.addLinkedNode(node);
			node.addLinkedNode(this);
		}
	}

	@Override
	public List<String> getJiraIssueKeys() {
		return this.jiraIssueKeys;
	}

	@Override
	public RevCommit getRevCommit() {
		return this.revCommit;
	}

	@Override
	public String toString() {
		return this.revCommit.getShortMessage();
	}
}