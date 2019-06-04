package de.uhd.ifi.se.decision.management.eclipse.view;

import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public interface KnowledgeGraphView {

	void showGraph(Linker linker);

	void showSubGraph(Node rootNode, int depth, Linker linker);

	void updateNodeSizes();

}