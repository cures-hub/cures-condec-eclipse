package de.uhd.ifi.se.decision.management.eclipse.view;

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

	void highlightNode(Node node);

	void highlightSelectedNode();
}