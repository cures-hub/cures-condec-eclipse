package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.atlassian.jira.rest.client.api.domain.IssueLink;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
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
 * work items, commits, files (e.g., Java classes), and methods. Extends the
 * JGraphT DirectedWeightedMultigraph.
 * 
 * @see DirectedWeightedMultigraph
 */
public class KnowledgeGraphImpl extends DirectedWeightedMultigraph<Node, Link> implements KnowledgeGraph {

	private static final long serialVersionUID = 1L;
	private GitClient gitClient;
	private JiraClient jiraClient;

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
		super(LinkImpl.class);
		this.gitClient = gitClient;
		this.jiraClient = jiraClient;
		createGraph();
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
		this(GitClient.getOrCreate(), JiraClient.getOrCreate(), startNode, distance);
	}

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
	 * @param startNode
	 *            the graph is built from this node.
	 * @param distance
	 *            from the start node that the knowledge graph is traversed.
	 */
	public KnowledgeGraphImpl(GitClient gitClient, JiraClient jiraClient, Node startNode, int distance) {
		super(LinkImpl.class);
		this.gitClient = gitClient;
		this.jiraClient = jiraClient;
		createGraph(startNode, distance);
	}

	private void createGraph() {
		addCommitsAndFiles();

		// All commits need to be loaded first
		addFiles();
		addMethods();

		addJiraIssues();
	}

	private void addCommitsAndFiles() {
		for (GitCommit gitCommit : gitClient.getCommits()) {
			createLinks(gitCommit, 0, 1);

			this.addVertex(gitCommit);
			for (Node node : gitCommit.getLinkedNodes()) {
				this.addVertex(node);
				this.addEdge(gitCommit, node);
			}

			for (DecisionKnowledgeElement element : gitCommit.getDecisionKnowledgeFromMessage()) {
				this.addVertex(element);
				this.addEdge(element, gitCommit);
			}
		}
	}

	private void addMethods() {
		for (CodeMethod codeMethod : CodeMethod.getInstances()) {
			this.addVertex(codeMethod);
			for (Node node : codeMethod.getLinkedNodes()) {
				this.addVertex(node);
				this.addEdge(codeMethod, node);
			}
		}
	}

	private void addFiles() {
		for (ChangedFile codeClass : ChangedFile.getInstances()) {
			this.addVertex(codeClass);
			for (Node node : codeClass.getLinkedNodes()) {
				this.addVertex(node);
				this.addEdge(codeClass, node);
			}
		}
	}

	private void addJiraIssues() {
		for (JiraIssue jiraIssue : jiraClient.getAllJiraIssues()) {
			createLinks(jiraIssue, 0, 1);

			this.addVertex(jiraIssue);
			for (Node node : jiraIssue.getLinkedNodes()) {
				this.addVertex(node);
				this.addEdge(jiraIssue, node);
			}
		}
	}

	private void createGraph(Node node, int distance) {
		createLinks(node, 0, distance);

		this.addVertex(node);
		for (Node linkedNode : node.getLinkedNodes()) {
			this.addVertex(linkedNode);
			this.addEdge(node, linkedNode);
		}
	}

	private void createLinks(Node node, int currentDepth, int maxDepth) {
		if (currentDepth >= maxDepth || this.containsVertex(node)) {
			return;
		}

		this.addVertex(node);

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
		this.addVertex(jiraIssue);
		this.addEdge(gitCommit, jiraIssue);
		createLinks(jiraIssue, currentDepth + 1, maxDepth);
	}

	private void addCommitsForJiraIssue(JiraIssue jiraIssue, int currentDepth, int maxDepth) {
		Set<GitCommit> commits = gitClient.getCommitsForJiraIssue(jiraIssue.getJiraIssueKey());
		for (GitCommit commit : commits) {
			this.addVertex(commit);
			this.addEdge(jiraIssue, commit);
			createLinks(commit, currentDepth + 1, maxDepth);
		}
	}

	private void addLinkedJiraIssuesForJiraIssue(JiraIssue jiraIssue, int currentDepth, int maxDepth) {
		for (IssueLink jiraIssueLink : jiraIssue.getIssue().getIssueLinks()) {
			JiraIssue linkedJiraIssue = JiraIssue.getOrCreate(jiraIssueLink.getTargetIssueKey(), jiraClient);
			if (linkedJiraIssue == null) {
				return;
			}
			this.addVertex(linkedJiraIssue);
			this.addEdge(jiraIssue, linkedJiraIssue);
			createLinks(linkedJiraIssue, currentDepth + 1, maxDepth);
		}
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
