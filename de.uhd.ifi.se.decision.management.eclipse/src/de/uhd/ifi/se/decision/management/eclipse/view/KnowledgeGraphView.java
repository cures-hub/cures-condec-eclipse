package de.uhd.ifi.se.decision.management.eclipse.view;

import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Interface to create a view for the knowledge graph model class. The knowledge
 * covers decision knowledge, JIRA issues such as requirements and work items,
 * commits, and files (e.g., classes and methods).
 * 
 * @see KnowledgeGraph
 * @see GephiGraph
 */
public interface KnowledgeGraphView {

	/**
	 * Refreshes the knowledge graph view from a knowledge graph. The knowledge covers decision
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
	void update(KnowledgeGraph graph);
	
	/**
	 * Highlights the node by increasing its size.
	 * 
	 * @param node
	 *            in the knowledge graph as a {@link Node} object.
	 */
	void highlightNode(Node node);

	/**
	 * Highlights the currently selected node by increasing its size.
	 * 
	 * @param node
	 *            in the knowledge graph as a {@link Node} object.
	 */
	void highlightSelectedNode();
}