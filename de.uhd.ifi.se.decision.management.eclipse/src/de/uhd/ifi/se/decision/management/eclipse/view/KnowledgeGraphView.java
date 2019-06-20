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
	 * Creates an overview of all knowledge in the knowledge graph. The knowledge
	 * covers decision knowledge, JIRA issues such as requirements and work items,
	 * commits, and files (e.g., classes and methods).
	 * 
	 * @see KnowledgeGraph
	 * @param knowledgeGraph
	 *            graph of commits, changed files, decision knowledge, and JIRA
	 *            issues such as requirements and work items.
	 */
	void createView(KnowledgeGraph knowledgeGraph);

	/**
	 * Creates an overview of the knowledge in the knowledge graph. The knowledge
	 * covers decision knowledge, JIRA issues such as requirements and work items,
	 * commits, and files (e.g., classes and methods).
	 * 
	 * @see KnowledgeGraph
	 * @param knowledgeGraph
	 *            graph of commits, changed files, decision knowledge, and JIRA
	 *            issues such as requirements and work items.
	 * @param title
	 *            frame title of the view.
	 */
	void createView(KnowledgeGraph knowledgeGraph, String title);
}