package de.uhd.ifi.se.decision.management.eclipse.view;

import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;

public interface MaspDesigner {

	void createFullMap(Linker linker);

	void createSelectedMap(de.uhd.ifi.se.decision.management.eclipse.model.Node rootNode, int depth, Linker linker);

}