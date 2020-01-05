package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class ShowFullGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!CommandHelper.isValidSelection(event)) {
			return null;
		}

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
		String title = "Knowledge Graph for Repository \"" + ConfigPersistenceManager.getPathToGit() + "\"";
		KnowledgeGraphViewImpl.getInstance(knowledgeGraph, title);

		return null;
	}
}