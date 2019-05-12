package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueLink;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.IssueKey;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethod;
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

	@Override
	public Map<Node, Set<Node>> createFullMap() {
		Map<Node, Set<Node>> map = new HashMap<Node, Set<Node>>();
		for (GitCommitImpl gc : gitClient.getAllCommits()) { 
			gc.extractChangedClasses(gitClient);
			createLinks(gc, 1);
			map.put(gc, gc.getLinks());
			for(DecisionKnowledgeElementImpl dke : gc.getCommitDecisions()) {
				Set<Node> n = new HashSet<Node>();
				n.add(gc);
				map.put(dke, n);
			}
		}
		// All commits needed to be loaded first
		for(CodeClassImpl cc : CodeClassImpl.getInstances()) {
			map.put(cc, cc.getLinks());
		}
		// All commits needed to be loaded first
		for(CodeMethod cm : CodeMethod.getInstances()) {
			map.put(cm, cm.getLinks());
		}
		for (JiraIssueImpl ji : jiraClient.getAllIssues()) {
			createLinks(ji, 1);
			map.put(ji, ji.getLinks());
		}
		return map;
	}

	/**
	 * @param node The node, which should be analyzed for further links.
	 * @param How  deep is the function allowed to go to through the nodes.
	 * @return Returns a Set of all visited nodes.
	 * 
	 * @issue There is no known API for getting all Commits touching a given
	 *        codeline. Only the last commit, which changed a specific line, can be
	 *        retrieved.
	 */
	@Override
	public Set<Node> createLinks(Node node, int maxDepth) {
		for(GitCommit gc : gitClient.getAllCommits()) {
			gc.extractChangedClasses(gitClient);
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
				List<IssueKey> keys = gc.getJiraIssueKeys();
				if (keys.size() > 0) {
					JiraIssueImpl ji = JiraIssueImpl.getOrCreate(keys.get(0), jiraClient);
					if (ji != null) {
						linkBidirectional(node, ji);
						createLinks(ji, currentDepth + 1, maxDepth, visitedNodes);
					}
				}
				for (DecisionKnowledgeElementImpl cd : gc.getCommitDecisions()) {
					createLinks(cd, currentDepth + 1, maxDepth, visitedNodes);
				}
			} 
			if (node instanceof DecisionKnowledgeElementImpl) {
				// Nothing more to do - Git-Decisions have no other links than to the
				// corresponding commit.
				System.err.println("DecisionKnowledgeElement as a Node");
			} 
			if (node instanceof CodeClassImpl) {
				for(Node n : node.getLinks()) {
					createLinks(n, currentDepth + 1, maxDepth, visitedNodes);
				}
			} 
			if (node instanceof CodeMethod) {
				for(Node n : node.getLinks()) {
					createLinks(n, currentDepth +1, maxDepth, visitedNodes);
				}
			} 
			if (node instanceof JiraIssueImpl) {
				JiraIssue ji = (JiraIssue) node;
				Set<GitCommitImpl> commits = gitClient.getCommitsForIssueKey(ji.getJiraIssueKey());
				for (GitCommitImpl commit : commits) {
					linkBidirectional(node, commit);
					createLinks(commit, currentDepth + 1, maxDepth, visitedNodes);
				}
				Issue issue = ji.getJiraIssue();
				for (IssueLink il : issue.getIssueLinks()) {
					JiraIssueImpl ji2 = JiraIssueImpl.getOrCreate(IssueKey.getOrCreate(il.getTargetIssueKey()), jiraClient);
					if (ji2 != null) {
						linkBidirectional(node, ji2);
						createLinks(ji2, currentDepth + 1, maxDepth, visitedNodes);
					}
				}				
			}
		}
	}

	private void linkBidirectional(Node in1, Node in2) {
		in1.addLinkedNode(in2);
		in2.addLinkedNode(in1);
	}
}
