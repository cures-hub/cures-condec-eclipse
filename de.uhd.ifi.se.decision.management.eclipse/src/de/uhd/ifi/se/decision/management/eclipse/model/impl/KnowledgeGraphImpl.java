package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

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
	private static KnowledgeGraph knowledgeGraph;
	private GitClient gitClient;
	private JiraClient jiraClient;
	private Set<Node> startNodes;

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
		this.startNodes = new HashSet<Node>();
		this.gitClient = gitClient;
		this.jiraClient = jiraClient;
		createGraph();
		
		knowledgeGraph = this;
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
		
		knowledgeGraph = this;
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
		
		knowledgeGraph = this;
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
		this.startNodes = new HashSet<Node>();
		this.startNodes.add(startNode);
		createGraph(startNode, distance);
		
		knowledgeGraph = this;
	}
	
	/**
	 * Returns the instance of KnowledgeGraph.
	 * 
	 * @return the instance of the knowledge graph
	 */
	public static KnowledgeGraph getInstance() {
		return knowledgeGraph;
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
			this.addVertex(gitCommit);
			createLinks(gitCommit, 0, 1);

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
			this.addVertex(jiraIssue);
			createLinks(jiraIssue, 0, 1);

			for (Node node : jiraIssue.getLinkedNodes()) {
				this.addVertex(node);
				this.addEdge(jiraIssue, node);
			}
		}
	}

	private void createGraph(Node node, int distance) {
		if (node == null) {
			return;
		}
		this.addVertex(node);
		createLinks(node, 0, distance);

		for (Node linkedNode : node.getLinkedNodes()) {
			this.addVertex(linkedNode);
			this.addEdge(node, linkedNode);
		}
	}

	private void createLinks(Node node, int currentDepth, int maxDepth) {
		if (currentDepth >= maxDepth) {
			return;
		}

		if (node instanceof ChangedFile) {
			for (GitCommit commit : ((ChangedFile) node).getCommits()) {
				this.addVertex(commit);
				this.addEdge(node, commit);
				createLinks(commit, currentDepth + 1, maxDepth);
			}
		}
		if (node instanceof GitCommit) {
			GitCommit commit = (GitCommit) node;
			addJiraIssuesForCommit(commit, currentDepth, maxDepth);
			for (DecisionKnowledgeElement element : commit.getDecisionKnowledgeFromMessage()) {
				this.addVertex(element);
				this.addEdge(element, commit);
			}
		}
		if (node instanceof JiraIssue) {
			JiraIssue jiraIssue = (JiraIssue) node;
			addCommitsForJiraIssue(jiraIssue, currentDepth, maxDepth);
			addLinkedJiraIssuesForJiraIssue(jiraIssue, currentDepth, maxDepth);
		}
	}

	private void addJiraIssuesForCommit(GitCommit gitCommit, int currentDepth, int maxDepth) {
		Set<String> keys = gitCommit.getJiraIssueKeys();
		if (keys.size() <= 0) {
			return;
		}
		JiraIssue jiraIssue = JiraIssue.getOrCreate(keys.iterator().next(), jiraClient);
		if (jiraIssue == null) {
			return;
		}
		this.addVertex(jiraIssue);
		this.addEdge(gitCommit, jiraIssue);
		createLinks(jiraIssue, currentDepth + 1, maxDepth);
	}

	private void addCommitsForJiraIssue(JiraIssue jiraIssue, int currentDepth, int maxDepth) {
		Set<GitCommit> commits = jiraIssue.getCommits();
		for (GitCommit commit : commits) {
			this.addVertex(commit);
			this.addEdge(jiraIssue, commit);
			createLinks(commit, currentDepth + 1, maxDepth);
		}
	}

	private void addLinkedJiraIssuesForJiraIssue(JiraIssue jiraIssue, int currentDepth, int maxDepth) {
		for (String key : jiraIssue.getKeysOfLinkedJiraIssues()) {
			JiraIssue linkedJiraIssue = JiraIssue.getOrCreate(key, jiraClient);
			this.addVertex(linkedJiraIssue);
			this.addEdge(jiraIssue, linkedJiraIssue);
			createLinks(linkedJiraIssue, currentDepth + 1, maxDepth);
		}
	}
	
	public void insertLink(Node node1, Node node2) {
		this.addEdge(node1, node2);
	}
	
	public boolean linkExists(Node node1, Node node2) {
		if (this.containsEdge(node1, node2)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public Set<Node> getStartNodes() {
		return this.startNodes;
	}

	@Override
	public Node getStartNode() {
		if (getStartNodes().isEmpty()) {
			return null;
		}
		return getStartNodes().iterator().next();
	}

	@Override
	public GitClient getGitClient() {
		return gitClient;
	}

	@Override
	public JiraClient getJiraClient() {
		return jiraClient;
	}
	
	@Override
	public String toString() {
		String graphAsString = "The start node for knowledge exploration is the ";
		int distance = 0;

		BreadthFirstIterator<Node, Link> iterator = new BreadthFirstIterator<Node, Link>(this, this.getStartNodes());
		while (iterator.hasNext()) {
			Node node = iterator.next();

			if (iterator.getDepth(node) > distance) {
				distance++;
				graphAsString += "\n" + "At distance " + distance + " the following nodes are linked:\n";
			}

			graphAsString += node.toString() + "\n";
		}

		return graphAsString;
	}
}
