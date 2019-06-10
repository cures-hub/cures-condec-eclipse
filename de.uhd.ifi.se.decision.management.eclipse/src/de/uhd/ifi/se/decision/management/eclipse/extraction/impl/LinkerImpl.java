package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedWeightedMultigraph;

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
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;

public class LinkerImpl implements Linker {
	private GitClient gitClient;
	private JiraClient jiraClient;
	private List<Link> visitedLinks;

	public LinkerImpl(GitClient gitClient, JiraClient jiraClient) {
		this.gitClient = gitClient;
		this.jiraClient = jiraClient;
		this.visitedLinks = new ArrayList<Link>();
	}

	@Override
	public Graph<Node, Link> createGraph() {
		Graph<Node, Link> graph = new DirectedWeightedMultigraph<Node, Link>(LinkImpl.class);

		for (GitCommit gitCommit : gitClient.getCommits()) {
			gitCommit.extractChangedClasses(gitClient);
			createGraph(gitCommit, 1);

			graph.addVertex(gitCommit);
			for (Node node : gitCommit.getLinkedNodes()) {
				graph.addVertex(node);
				this.visitedLinks.add(graph.addEdge(gitCommit, node));
			}

			for (DecisionKnowledgeElement element : gitCommit.getDecisionKnowledgeFromMessage()) {
				graph.addVertex(element);
				graph.addEdge(element, gitCommit);
			}
		}

		// All commits need to be loaded first
		for (CodeClass codeClass : CodeClass.getInstances()) {
			graph.addVertex(codeClass);
			for (Node node : codeClass.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(codeClass, node);
			}
		}

		// All commits need to be loaded first
		for (CodeMethod codeMethod : CodeMethod.getInstances()) {
			graph.addVertex(codeMethod);
			for (Node node : codeMethod.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(codeMethod, node);
			}
		}

		for (JiraIssue jiraIssue : jiraClient.getAllJiraIssues()) {
			createGraph(jiraIssue, 1);

			graph.addVertex(jiraIssue);
			for (Node node : jiraIssue.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(jiraIssue, node);
			}
		}

		return graph;
	}

	@Override
	public Set<Node> createGraph(Node node, int maxDepth) {
		Set<Node> visitedNodes = new HashSet<Node>();
		createLinks(node, 0, maxDepth, visitedNodes);
		return visitedNodes;
	}

	public Set<Node> createLinks(Node node, int maxDepth, Set<Node> visitedNodes) {
		createLinks(node, 0, maxDepth, visitedNodes);
		return visitedNodes;
	}

	private void createLinks(Node node, int currentDepth, int maxDepth, Set<Node> visitedNodes) {
		// Create Links, if node wasn't visited yet
		if (currentDepth < maxDepth && !visitedNodes.contains(node)) {
			visitedNodes.add(node);
			if (node instanceof GitCommitImpl) {
				GitCommit gitCommit = (GitCommit) node;
				List<String> keys = gitCommit.getJiraIssueKeys();
				if (keys.size() > 0) {
					JiraIssue jiraIssue = JiraIssue.getOrCreate(keys.get(0), jiraClient);
					if (jiraIssue != null) {
						linkBidirectional(node, jiraIssue);
						createLinks(jiraIssue, currentDepth + 1, maxDepth, visitedNodes);
					}
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
