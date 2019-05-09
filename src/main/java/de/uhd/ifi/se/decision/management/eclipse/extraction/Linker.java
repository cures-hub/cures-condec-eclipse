package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueLink;

import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.INode;
import de.uhd.ifi.se.decision.management.eclipse.model.IssueKey;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElement;

public class Linker {
	private GitClient gitManager;
	private JiraClient jiraManager;

	public Linker(GitClient gitManager, JiraClient jiraManager) {
		this.gitManager = gitManager;
		this.jiraManager = jiraManager;
	}

	public GitClient getGitManager() {
		return gitManager;
	}

	public void setGitManager(GitClient gitManager) {
		this.gitManager = gitManager;
	}

	public JiraClient getJiraManager() {
		return jiraManager;
	}

	public void setJiraManager(JiraClient jiraManager) {
		this.jiraManager = jiraManager;
	}

	public Map<INode, Set<INode>> createFullMap() {
		Map<INode, Set<INode>> map = new HashMap<INode, Set<INode>>();
		for (GitCommit gc : gitManager.getAllCommits()) { 
			gc.extractChangedClasses(gitManager);
			createLinks(gc, 1);
			map.put(gc, gc.getLinks());
			for(DecisionKnowledgeElement dke : gc.getCommitDecisions()) {
				Set<INode> n = new HashSet<INode>();
				n.add(gc);
				map.put(dke, n);
			}
		}
		// All commits needed to be loaded first
		for(CodeClass cc : CodeClass.getInstances()) {
			map.put(cc, cc.getLinks());
		}
		// All commits needed to be loaded first
		for(CodeMethod cm : CodeMethod.getInstances()) {
			map.put(cm, cm.getLinks());
		}
		for (JiraIssue ji : jiraManager.getAllIssues()) {
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
	public Set<INode> createLinks(INode node, int maxDepth) {
		for(GitCommit gc : gitManager.getAllCommits()) {
			gc.extractChangedClasses(gitManager);
		}
		Set<INode> visitedNodes = new HashSet<INode>();
		createLinks(node, 0, maxDepth, visitedNodes);
		return visitedNodes;
	}

	private void createLinks(INode node, int currentDepth, int maxDepth, Set<INode> visitedNodes) {
		// Create Links, if node wasn't visited yet
		if (currentDepth < maxDepth && !visitedNodes.contains(node)) {
			visitedNodes.add(node);			
			if (node instanceof GitCommit) {
				GitCommit gc = (GitCommit) node;
				List<IssueKey> keys = gc.getIssueKeys();
				if (keys.size() > 0) {
					JiraIssue ji = JiraIssue.getOrCreate(keys.get(0), jiraManager);
					if (ji != null) {
						linkBidirectional(node, ji);
						createLinks(ji, currentDepth + 1, maxDepth, visitedNodes);
					}
				}
				for (DecisionKnowledgeElement cd : gc.getCommitDecisions()) {
					createLinks(cd, currentDepth + 1, maxDepth, visitedNodes);
				}
			} 
			if (node instanceof DecisionKnowledgeElement) {
				// Nothing more to do - Git-Decisions have no other links than to the
				// corresponding commit.
				System.err.println("DecisionKnowledgeElement as a Node");
			} 
			if (node instanceof CodeClass) {
				for(INode n : node.getLinks()) {
					createLinks(n, currentDepth + 1, maxDepth, visitedNodes);
				}
			} 
			if (node instanceof CodeMethod) {
				for(INode n : node.getLinks()) {
					createLinks(n, currentDepth +1, maxDepth, visitedNodes);
				}
			} 
			if (node instanceof JiraIssue) {
				JiraIssue ji = (JiraIssue) node;
				Set<GitCommit> commits = gitManager.getCommitsForIssueKey(ji.getIssueKey());
				for (GitCommit commit : commits) {
					linkBidirectional(node, commit);
					createLinks(commit, currentDepth + 1, maxDepth, visitedNodes);
				}
				Issue issue = ji.getBindedIssue();
				for (IssueLink il : issue.getIssueLinks()) {
					JiraIssue ji2 = JiraIssue.getOrCreate(IssueKey.getOrCreate(il.getTargetIssueKey()), jiraManager);
					if (ji2 != null) {
						linkBidirectional(node, ji2);
						createLinks(ji2, currentDepth + 1, maxDepth, visitedNodes);
					}
				}				
			}
		}
	}

	private void linkBidirectional(INode in1, INode in2) {
		in1.addLinkedNode(in2);
		in2.addLinkedNode(in1);
	}
}
