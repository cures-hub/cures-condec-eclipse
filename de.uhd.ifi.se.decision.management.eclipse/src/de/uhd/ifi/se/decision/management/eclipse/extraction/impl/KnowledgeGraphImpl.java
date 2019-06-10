package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.atlassian.jira.rest.client.api.domain.IssueLink;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;

/**
 * Class to create a knowledge graph for the entire project or a sub-graph from
 * a given start node with a certain distance (set in the constructor). The
 * knowledge covers decision knowledge, JIRA issues such as requirements and
 * work items, commits, files (e.g., Java classes), and methods.
 */
public class KnowledgeGraphImpl implements KnowledgeGraph {
	private GitClient gitClient;
	private JiraClient jiraClient;
	private Graph<Node, Link> graph;

	/**
	 * Constructor for the KnowledgeGraph. Creates a graph for the entire project.
	 * The knowledge covers decision knowledge, JIRA issues such as requirements and
	 * work items, commits, files (e.g., Java classes), and methods.
	 * 
	 * @see GitClient
	 * @see JiraClient
	 * @see Graph
	 * @param gitClient
	 *            to connect to a git repository associated with this Eclipse
	 *            project. Retrieves commits and code changes (diffs) in git.
	 * @param jiraClient
	 *            to connect to a JIRA project associated with this Eclipse project.
	 *            Retrieves JIRA issues.
	 */
	public KnowledgeGraphImpl(GitClient gitClient, JiraClient jiraClient) {
		this.gitClient = gitClient;
		this.jiraClient = jiraClient;
		this.graph = createGraph();
	}

	/**
	 * Constructor for the KnowledgeGraph. Creates a graph for the entire project.
	 * The knowledge covers decision knowledge, JIRA issues such as requirements and
	 * work items, commits, files (e.g., Java classes), and methods.
	 * 
	 * @see GitClient
	 * @see JiraClient
	 * @see Graph
	 */
	public KnowledgeGraphImpl() {
		this(GitClient.getOrCreate(), JiraClient.getOrCreate());
	}

	/**
	 * Constructor for the KnowledgeGraph. Creates a sub-graph from the given start
	 * node with a certain distance. The knowledge covers decision knowledge, JIRA
	 * issues such as requirements and work items, commits, files (e.g., Java
	 * classes), and methods.
	 * 
	 * @see GitClient
	 * @see JiraClient
	 * @see Graph
	 * @param startNode
	 *            the graph is built from this node.
	 * @param distance
	 *            from the start node that the knowledge graph is traversed.
	 */
	public KnowledgeGraphImpl(Node startNode, int distance) {
		this.gitClient = GitClient.getOrCreate();
		this.jiraClient = JiraClient.getOrCreate();
		this.graph = createGraph(startNode, distance);
	}

	private Graph<Node, Link> createGraph() {
		graph = new DirectedWeightedMultigraph<Node, Link>(LinkImpl.class);

		addCommitsAndFiles();

		// All commits need to be loaded first
		addFiles();
		addMethods();

		addJiraIssues();

		return graph;
	}

	private void addCommitsAndFiles() {
		for (GitCommit gitCommit : gitClient.getCommits()) {
			createGraph(gitCommit, 1);

			graph.addVertex(gitCommit);
			for (Node node : gitCommit.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(gitCommit, node);
			}

			for (DecisionKnowledgeElement element : gitCommit.getDecisionKnowledgeFromMessage()) {
				graph.addVertex(element);
				graph.addEdge(element, gitCommit);
			}
		}
	}

	private void addMethods() {
		for (CodeMethod codeMethod : CodeMethod.getInstances()) {
			graph.addVertex(codeMethod);
			for (Node node : codeMethod.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(codeMethod, node);
			}
		}
	}

	private void addFiles() {
		for (CodeClass codeClass : CodeClass.getInstances()) {
			graph.addVertex(codeClass);
			for (Node node : codeClass.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(codeClass, node);
			}
		}
	}

	private void addJiraIssues() {
		for (JiraIssue jiraIssue : jiraClient.getAllJiraIssues()) {
			createGraph(jiraIssue, 1);

			graph.addVertex(jiraIssue);
			for (Node node : jiraIssue.getLinkedNodes()) {
				graph.addVertex(node);
				graph.addEdge(jiraIssue, node);
			}
		}
	}

	private Graph<Node, Link> createGraph(Node node, int distance) {
		Graph<Node, Link> graph = new DirectedWeightedMultigraph<Node, Link>(LinkImpl.class);
		createLinks(node, 0, distance);

		graph.addVertex(node);
		for (Node linkedNode : node.getLinkedNodes()) {
			graph.addVertex(linkedNode);
			graph.addEdge(node, linkedNode);
		}

		return graph;
	}

	private void createLinks(Node node, int currentDepth, int maxDepth) {
		if (currentDepth >= maxDepth || graph.containsVertex(node)) {
			return;
		}

		graph.addVertex(node);

		if (node instanceof GitCommitImpl) {
			addJiraIssuesForCommit((GitCommit) node, currentDepth, maxDepth);
		}
		if (node instanceof JiraIssueImpl) {
			JiraIssue jiraIssue = (JiraIssue) node;
			addCommitsForJiraIssue(jiraIssue, currentDepth, maxDepth);
			addLinkedJiraIssuesForJiraIssue(jiraIssue, currentDepth, maxDepth);
		}
	}

	private void addJiraIssuesForCommit(GitCommit gitCommit, int currentDepth, int maxDepth) {
		List<String> keys = gitCommit.getJiraIssueKeys();
		if (keys.size() <= 0) {
			return;
		}
		JiraIssue jiraIssue = JiraIssue.getOrCreate(keys.get(0), jiraClient);
		if (jiraIssue == null) {
			return;
		}
		graph.addVertex(jiraIssue);
		graph.addEdge(gitCommit, jiraIssue);
		createLinks(jiraIssue, currentDepth + 1, maxDepth);
	}

	private void addCommitsForJiraIssue(JiraIssue jiraIssue, int currentDepth, int maxDepth) {
		Set<GitCommit> commits = gitClient.getCommitsForIssueKey(jiraIssue.getJiraIssueKey());
		for (GitCommit commit : commits) {
			graph.addVertex(commit);
			graph.addEdge(jiraIssue, commit);
			createLinks(commit, currentDepth + 1, maxDepth);
		}
	}

	private void addLinkedJiraIssuesForJiraIssue(JiraIssue jiraIssue, int currentDepth, int maxDepth) {
		for (IssueLink jiraIssueLink : jiraIssue.getJiraIssue().getIssueLinks()) {
			JiraIssue linkedJiraIssue = JiraIssue.getOrCreate(jiraIssueLink.getTargetIssueKey(), jiraClient);
			if (linkedJiraIssue == null) {
				return;
			}
			graph.addVertex(linkedJiraIssue);
			graph.addEdge(jiraIssue, linkedJiraIssue);
			createLinks(linkedJiraIssue, currentDepth + 1, maxDepth);
		}
	}

	@Override
	public Graph<Node, Link> getGraph() {
		return graph;
	}

	@Override
	public GitClient getGitClient() {
		return gitClient;
	}

	@Override
	public JiraClient getJiraClient() {
		return jiraClient;
	}
}
