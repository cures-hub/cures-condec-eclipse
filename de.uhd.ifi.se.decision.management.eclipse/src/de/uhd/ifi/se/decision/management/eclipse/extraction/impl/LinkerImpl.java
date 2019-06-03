package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

public class LinkerImpl implements Linker {
	private GitClient gitClient;
	private JiraClient jiraClient;

	public LinkerImpl(GitClient gitClient, JiraClient jiraClient) {
		this.gitClient = gitClient;
		this.jiraClient = jiraClient;
	}

	@Override
	public Map<Node, Set<Node>> createKnowledgeGraph() {
		Map<Node, Set<Node>> map = new HashMap<Node, Set<Node>>();
		for (GitCommit gitCommit : gitClient.getCommits()) {
			gitCommit.extractChangedClasses(gitClient);
			createLinks(gitCommit, 1);
			map.put(gitCommit, gitCommit.getLinkedNodes());
			for (DecisionKnowledgeElement dke : gitCommit.getDecisionKnowledgeFromMessage()) {
				Set<Node> n = new HashSet<Node>();
				n.add(gitCommit);
				map.put(dke, n);
			}
		}
		
		// All commits need to be loaded first
		for (CodeClass codeClass : CodeClass.getInstances()) {
			map.put(codeClass, codeClass.getLinkedNodes());
		}
		
		// All commits need to be loaded first
		for (CodeMethod codeMethod : CodeMethod.getInstances()) {
			map.put(codeMethod, codeMethod.getLinkedNodes());
		}
		
		for (JiraIssue jiraIssue : jiraClient.getAllJiraIssues()) {
			createLinks(jiraIssue, 1);
			map.put(jiraIssue, jiraIssue.getLinkedNodes());
		}
		return map;
	}

	@Override
	public Set<Node> createLinks(Node node, int maxDepth) {
		for (GitCommit gitCommit : gitClient.getCommits()) {
			gitCommit.extractChangedClasses(gitClient);
		}
		Set<Node> visitedNodes = new HashSet<Node>();
		createLinks(node, 0, maxDepth, visitedNodes);
		return visitedNodes;
	}

	private void createLinks(Node node, int currentDepth, int maxDepth, Set<Node> visitedNodes) {
		// Create Links, if node wasn't visited yet
		if (currentDepth < maxDepth && !visitedNodes.contains(node)) {
			visitedNodes.add(node);
			if (node instanceof GitCommitImpl) {
				GitCommit gc = (GitCommit) node;
				List<String> keys = gc.getJiraIssueKeys();
				if (keys.size() > 0) {
					JiraIssue ji = JiraIssue.getOrCreate(keys.get(0), jiraClient);
					if (ji != null) {
						linkBidirectional(node, ji);
						createLinks(ji, currentDepth + 1, maxDepth, visitedNodes);
					}
				}
				for (DecisionKnowledgeElement cd : gc.getDecisionKnowledgeFromMessage()) {
					createLinks(cd, currentDepth + 1, maxDepth, visitedNodes);
				}
			}
			if (node instanceof DecisionKnowledgeElementImpl) {
				// Nothing more to do - Git-Decisions have no other links than to the
				// corresponding commit.
				System.err.println("DecisionKnowledgeElement as a Node");
			}
			if (node instanceof CodeClassImpl) {
				for (Node n : node.getLinkedNodes()) {
					createLinks(n, currentDepth + 1, maxDepth, visitedNodes);
				}
			}
			if (node instanceof CodeMethodImpl) {
				for (Node n : node.getLinkedNodes()) {
					createLinks(n, currentDepth + 1, maxDepth, visitedNodes);
				}
			}
			if (node instanceof JiraIssueImpl) {
				JiraIssue ji = (JiraIssue) node;
				Set<GitCommit> commits = gitClient.getCommitsForIssueKey(ji.getJiraIssueKey());
				for (GitCommit commit : commits) {
					linkBidirectional(node, commit);
					createLinks(commit, currentDepth + 1, maxDepth, visitedNodes);
				}
				Issue issue = ji.getJiraIssue();
				for (IssueLink il : issue.getIssueLinks()) {
					JiraIssue ji2 = JiraIssue.getOrCreate(il.getTargetIssueKey(), jiraClient);
					if (ji2 != null) {
						linkBidirectional(node, ji2);
						createLinks(ji2, currentDepth + 1, maxDepth, visitedNodes);
					}
				}
			}
		}
	}

	private void linkBidirectional(Node node1, Node node2) {
		node1.addLinkedNode(node2);
		node2.addLinkedNode(node1);
	}
	
	@Override
	public GitClient getGitClient() {
		return gitClient;
	}

	@Override
	public void setGitClient(GitClient gitClient) {
		this.gitClient = gitClient;
	}

	@Override
	public JiraClient getJiraClient() {
		return jiraClient;
	}

	@Override
	public void setJiraClient(JiraClient jiraClient) {
		this.jiraClient = jiraClient;
	}

}
