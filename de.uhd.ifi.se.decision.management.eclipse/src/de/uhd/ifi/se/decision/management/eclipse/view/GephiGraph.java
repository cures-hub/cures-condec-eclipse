package de.uhd.ifi.se.decision.management.eclipse.view;

import org.gephi.graph.api.NodeIterable;
import org.gephi.project.api.Workspace;
import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Interface to create a gephi graph from the knowledge graph. Used in the
 * KnowledgeGraphView class.
 * 
 * @see KnowledgeGraph
 * @see KnowledgeGraphView
 */
public interface GephiGraph {
	
	/**
	 * Creates the gephi graph from a knowledge graph. The knowledge covers decision
	 * knowledge, JIRA issues such as requirements and work items, commits, and
	 * files (e.g., classes and methods).
	 * 
	 * @see Graph
	 * @see KnowledgeGraph
	 * @param knowledgeGraph
	 *            graph of decision knowledge, JIRA issues such as requirements and
	 *            work items, commits, and files (e.g., classes and methods). The
	 *            knowledge graph is a JGraphT graph.
	 */
	void createGephiGraph(KnowledgeGraph knowledgeGraph);
	
	/**
	 * Updates the gephi graph from a knowledge graph. The knowledge covers decision
	 * knowledge, JIRA issues such as requirements and work items, commits, and
	 * files (e.g., classes and methods).
	 * 
	 * @see Graph
	 * @see KnowledgeGraph
	 * @param knowledgeGraph
	 *            graph of decision knowledge, JIRA issues such as requirements and
	 *            work items, commits, and files (e.g., classes and methods). The
	 *            knowledge graph is a JGraphT graph.
	 */
	void update(KnowledgeGraph knowledgeGraph);

	/**
	 * Returns a gephi node for a given model node object.
	 * 
	 * @see Node
	 * @param node
	 *            object of the model Node class.
	 * @return gephi node.
	 */
	org.gephi.graph.api.Node getGephiNode(Node node);

	/**
	 * Returns a gephi node for a given model node object.
	 * 
	 * @see Node
	 * @param nodeId
	 *            id of an object of the model Node class.
	 * @return gephi node.
	 */
	org.gephi.graph.api.Node getGephiNode(long nodeId);

	/**
	 * Returns a gephi node for a given model node object.
	 * 
	 * @see Node
	 * @param nodeText
	 *            text of an object of the model Node class.
	 * @return gephi node.
	 */
	org.gephi.graph.api.Node getGephiNode(String nodeText);

	/**
	 * Returns all gephi nodes in the gephi graph.
	 * 
	 * @see NodeIterable
	 * @return all gephi nodes in the gephi graph.
	 */
	NodeIterable getNodes();

	void setSizeOfAllNodes(float size);

	void setSizeOfNode(org.gephi.graph.api.Node gephiNode, float size);

	void setSizeOfNode(long selectedNodeId, float size);

	void setSizeOfNode(Node node, float size);
	
	Workspace getWorkspace();
}