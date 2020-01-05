package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;

import de.uhd.ifi.se.decision.management.eclipse.Activator;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.DecisionExplorationView;

public class KnowledgeExtractionCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPath pathOfSelectedFile = CommandHelper.getPathOfSelectedFile(event);
		if (pathOfSelectedFile == null || !pathOfSelectedFile.toFile().exists()) {
			return null;
		}
		showRelatedKnowledge(pathOfSelectedFile);
		return null;
	}

	public void showRelatedKnowledge(IPath pathOfFile) {
		DecisionExplorationView explorationView = (DecisionExplorationView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.DecisionExploration");

		ChangedFile selectedFile = ChangedFile.getOrCreate(pathOfFile);
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(selectedFile,
				ConfigPersistenceManager.getLinkDistance());

		explorationView.setContent(knowledgeGraph.toString());
	}
}