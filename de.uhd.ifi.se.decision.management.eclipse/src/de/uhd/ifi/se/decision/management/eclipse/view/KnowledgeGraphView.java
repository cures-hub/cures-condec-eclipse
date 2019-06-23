package de.uhd.ifi.se.decision.management.eclipse.view;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;

/**
 * Interface to create a view for the knowledge graph model class.
 * 
 * @see KnowledgeGraph
 * @see GephiGraph
 */
public interface KnowledgeGraphView {

	/**
	 * Creates an overview of the knowledge in the knowledge graph. The knowledge
	 * covers decision knowledge, JIRA issues such as requirements and work items,
	 * commits, and files (e.g., classes and methods).
	 * 
	 * @see KnowledgeGraph
	 * @param title
	 *            frame title of the view.
	 */
	void createView(String title);
}