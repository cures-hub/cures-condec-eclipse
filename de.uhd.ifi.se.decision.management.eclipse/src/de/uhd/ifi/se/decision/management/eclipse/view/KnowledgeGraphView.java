package de.uhd.ifi.se.decision.management.eclipse.view;

import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public interface KnowledgeGraphView {

	/**
	 * Creates an overview of all knowledge documented for the project. The
	 * knowledge covers decision knowledge, JIRA issues such as requirements, and
	 * code (classes and methods).
	 * 
	 * @param linker
	 */
	void createView(Linker linker);

	/**
	 * Creates an overview of all knowledge accessible from a certain node within a
	 * certain distance. The knowledge covers decision knowledge, JIRA issues such
	 * as requirements, and code (classes and methods).
	 * 
	 * @param linker
	 */
	void createView(Node selectedNode, int distance, Linker linker);
}