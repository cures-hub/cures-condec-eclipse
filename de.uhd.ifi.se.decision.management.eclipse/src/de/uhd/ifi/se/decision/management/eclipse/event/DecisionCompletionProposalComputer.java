package de.uhd.ifi.se.decision.management.eclipse.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import de.uhd.ifi.se.decision.management.eclipse.Activator;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.ChangeImpactAnalysisView;
import de.uhd.ifi.se.decision.management.eclipse.view.DecisionExplorationView;
import de.uhd.ifi.se.decision.management.eclipse.view.GraphToTextConverter;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.GraphToTextConverterImpl;

/**
 * 
 * Provides the plug-in with a custom content assist. With content assist
 * invocation decision knowledge exploration and change impact analysis is
 * triggered.
 * 
 * @decision Present related knowledge and change impact in dedicated views.
 * @issue How to present related knowledge and change impact to developers?
 * @alternative Present related knowledge and change impact as a list of
 *              proposals.
 * @con Would mislead developers. Developers associate content assist with
 *      auto-completion and proposals for bug-fixes.
 * 
 * @decision Content assist invocation triggers decision exploration view and
 *           change impact analysis view
 * @issue How to trigger decision exploration?
 */
public class DecisionCompletionProposalComputer implements IJavaCompletionProposalComputer {

	/**
	 * Informs the computer that a content assist session has started.
	 */
	@Override
	public void sessionStarted() {
		// Currently not used in this plug-in.
	}

	/**
	 * The computeCompletionProposals method provides the content assist with custom
	 * Content
	 * 
	 * @param context
	 *            the context in which the content assist is invoked
	 * @return List<ICompletionProposal> returns a List of ICompletionProposals to
	 *         be shown in the Content Assist
	 */
	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context,
			IProgressMonitor monitor) {

		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();

		try {
			String writtenString = context.computeIdentifierPrefix().toString();

			// the developer hasn't started to write something
			if (writtenString.isEmpty()) {
				showRelatedKnowledge();
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		return proposals;
	}

	public static void showRelatedKnowledge() {
		IPath pathOfFile = CurrentFile.getOpenFilePath();
		int line = CurrentFile.getLineOfCursorPosition();

		DecisionExplorationView explorationView = (DecisionExplorationView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.DecisionExploration");

		ChangedFile selectedFile = ChangedFile.getOrCreate(pathOfFile);
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(selectedFile,
				ConfigPersistenceManager.getLinkDistance());
		
		GraphToTextConverter converter = new GraphToTextConverterImpl(knowledgeGraph);
		explorationView.setContent(converter.produceDecisionExploration(line));

		ChangeImpactAnalysisView changeImpactView = (ChangeImpactAnalysisView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.ChangeImpactAnalysis");

		changeImpactView.setContent(GraphToTextConverterImpl.analyzeChange(pathOfFile, line));
	}

	/**
	 * The computeContextInformation method provides context information to content
	 * assist proposals
	 * 
	 * @param context
	 *            the context in which the content assist is invoked
	 * @return List<IContexInformation> the list of context information which is
	 *         shown besides the content assist proposals
	 */
	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context,
			IProgressMonitor monitor) {
		return null;
	}

	/**
	 * The getErrorMessage method provides an error message
	 * 
	 * @return String the error message
	 */
	@Override
	public String getErrorMessage() {
		return "Creating Proposals in Progress"; // This message is shown in the lower left corner of the eclipse IDE
	}

	/**
	 * Informs the computer that a content assist session has ended.
	 */
	@Override
	public void sessionEnded() {
		// Currently not used in this plug-in.
	}
}