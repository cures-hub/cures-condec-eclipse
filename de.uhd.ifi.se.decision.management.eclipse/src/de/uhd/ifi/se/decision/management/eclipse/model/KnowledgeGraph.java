package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.Set;

import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;

/**
 * Interface to create a knowledge graph for the entire project or a sub-graph
 * from a given start node with a certain distance (set in the constructor). The
 * knowledge covers decision knowledge, JIRA issues such as requirements and
 * work items, commits, files (e.g., Java classes), and methods. Extends the
 * JGraphT graph interface.
 * 
 * @see GitClient
 * @see JiraClient
 * @see Graph
 */
public interface KnowledgeGraph extends Graph<Node, Link> {
	
	/**
	 * Returns the start node that the graph is created from. The start node can be
	 * an object of the class {@link ChangedFile} (e.g. a selected Java class),
	 * {@link GitCommit}, {@link JiraIssue}, {@link DecisionKnowledgeElement}, or
	 * {@link CodeMethod}.
	 * 
	 * @return start node that the graph is created from as a {@link Node} object.
	 *         The start node can be null.
	 */
	Node getStartNode();

	/**
	 * Returns the start nodes that the graph is created from, e.g. a set of Java
	 * classes.
	 * 
	 * @return start nodes that the graph is created from as a set of {@link Node}
	 *         objects. The set can be empty.
	 */
	Set<Node> getStartNodes();

	/**
	 * Returns the git client to connect to a git repository associated with this
	 * Eclipse project. Retrieves commits and code changes (diffs) in git. It can be
	 * set in the constructor.
	 * 
	 * @see GitClient
	 * 
	 * @return git client to connect to a git repository.
	 */
	GitClient getGitClient();

	/**
	 * Returns the JIRA client to connect to a JIRA project associated with this
	 * Eclipse project. Retrieves JIRA issues. It can be set in the constructor.
	 * 
	 * @see JiraClient
	 * 
	 * @return JIRA client to connect to a JIRA project.
	 */
	JiraClient getJiraClient();
	
	/**
	 * Creates an edge from node node1 to node node2
	 * 
	 * @param node1
	 * 		source node
	 * @param node2
	 * 		target node
	 */
	void insertLink(Node node1, Node node2);
	
	/**
	 * Inserts the link into the knowledge graph.
	 * 
	 * @param link
	 * 		the link to be inserted
	 */
	void insertLink(Link link);
	
	/**
	 * Checks whether a link between node1 and node2 exists.
	 * 
	 * @param node1
	 * 		source node
	 * @param node2
	 * 		target node
	 * @return
	 * 		true, if a link exists; false, if no link exists
	 */
	boolean linkExists(Node node1, Node node2);
	
	/**
	 * Checks whether the link exists in the knowledge graph.
	 * 
	 * @param link
	 * 		the link to be checked
	 * @return
	 * 		true, if the link exists; false, if the link doesn't exist
	 */
	boolean linkExists(Link link);
	
	/**
	 * Reads persistence data from JSON file and updates the knowledge graph with it
	 */
	void updateWithPersistanceData();
}