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
	 * Resets the filter text field and the selected node.
	 * 
	 * @return
	 * 			  true, if the filters were correctly reset.
	 */
	boolean resetFilters();
	
	/**
	 * Highlights the node by increasing its size.
	 * 
	 * @param node
	 *            in the knowledge graph as a {@link Node} object.
	 * @return
	 * 			  true, if the node was correctly highlighted.
	 */
	boolean highlightNode(Node node);

	/**
	 * Highlights the currently selected node by increasing its size.
	 * 
	 * @param node
	 *            in the knowledge graph as a {@link Node} object.
	 * @return
	 * 			  true, if the node was correctly highlighted.
	 */
	boolean highlightSelectedNode();
	
	/**
	 * Highlights the selected node and updates the knowledge graph view.
	 * 
	 * @param selectedNode
	 *            in the knowledge graph as a {@link Node} object.
	 * @return
	 * 			  true, if the node was correctly highlighted.
	 */
	boolean highlightSelectedNodeAndUpdate(Node node);
	
	/**
	 * Creates a dialog-window that allows the user to create a new node in the knowledge graph.
	 * 
	 * @return
	 * 			  true, if the create node dialog-window was correctly created.
	 */
	boolean createNode();
	
	/**
	 * Returns the gephi graph created from the knowledgeGraphView.
	 * 
	 * @see GephiGraph
	 * 
	 * @return gephi graph
	 */
	GephiGraph getGephiGraph();
}