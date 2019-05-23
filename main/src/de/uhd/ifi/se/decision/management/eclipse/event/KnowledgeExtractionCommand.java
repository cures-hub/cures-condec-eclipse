package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uhd.ifi.se.decision.management.eclipse.Activator;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TextualRepresentation;
import de.uhd.ifi.se.decision.management.eclipse.view.ChangeImpactAnalysisView;
import de.uhd.ifi.se.decision.management.eclipse.view.DecisionExplorationView;

@SuppressWarnings("restriction")
public class KnowledgeExtractionCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPath pathOfFile = getPathOfSelectedFile(event);
		showRelatedKnowledge(pathOfFile);
		return null;
	}

	private IPath getPathOfSelectedFile(ExecutionEvent event) {
		CompilationUnit compilationUnit = getCompilationUnit(event);
		return compilationUnit.getPath().removeFirstSegments(1);
	}

	private CompilationUnit getCompilationUnit(ExecutionEvent event) {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof CompilationUnit) {
			return (CompilationUnit) firstElement;
		}
		return null;
	}

	public void showRelatedKnowledge(IPath pathOfFile) {
		int line = 1;

		DecisionExplorationView explorationView = (DecisionExplorationView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.DecisionExploration");

		explorationView.setContent(TextualRepresentation.produceDecisionExploration(pathOfFile, line));

		ChangeImpactAnalysisView changeImpactView = (ChangeImpactAnalysisView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.ChangeImpactAnalysis");

		changeImpactView.setContent(TextualRepresentation.analyzeChange(pathOfFile, line));
	}
}