package de.uhd.ifi.se.decision.management.eclipse.changesupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import de.uhd.ifi.se.decision.management.eclipse.Activator;
import de.uhd.ifi.se.decision.management.eclipse.view.ChangeImpactAnalysisView;
import de.uhd.ifi.se.decision.management.eclipse.view.DecisionExplorationView;

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
	 * The sessionStarted method acts when the session is started 
	 */
	@Override
	public void sessionStarted() {
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
			if (writtenString.equals("")) {
				showRelatedKnowledge();
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		return proposals;
	}

	public static void showRelatedKnowledge() {
		DecisionExplorationView explorationView = (DecisionExplorationView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.DecisionExploration");

		explorationView.exploreDecision();

		ChangeImpactAnalysisView changeImpactView = (ChangeImpactAnalysisView) Activator
				.getViewPart("de.uhd.ifi.se.decision.management.eclipse.view.ChangeImpactAnalysis");

		try {
			changeImpactView.analyseChangeImpact();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * The sessionEnded method acts when the session is ended
	 */
	@Override
	public void sessionEnded() {
	}
}