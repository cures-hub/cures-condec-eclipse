package de.uhd.ifi.se.decision.management.eclipse.view;

import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public interface KnowledgeGraphView {

	/**
	 * Creates an overview of all knowledge documented for the project. The
	 * knowledge covers decision knowledge, JIRA issues such as requirements, and
	 * code (classes and methods).
	 * 
	 * @param linker
	 */
	void createView(KnowledgeGraph linker);

	/**
	 * Creates an overview of all knowledge accessible from a certain node within a
	 * certain distance. The knowledge covers decision knowledge, JIRA issues such
	 * as requirements, and code (classes and methods).
	 * 
	 * @param linker
	 */
	void createView(Node selectedNode, int distance, KnowledgeGraph linker);

	void createView(Graph<Node, Link> graph, String frameTitle);

	void createView(Graph<Node, Link> graph);
}