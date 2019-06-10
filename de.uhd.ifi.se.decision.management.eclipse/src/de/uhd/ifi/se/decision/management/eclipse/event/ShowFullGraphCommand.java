package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class ShowFullGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!CommandHelper.isValidSelection(event)) {
			return null;
		}
		JiraClient jiraClient = JiraClient.getOrCreate();
		if (!jiraClient.isWorking()) {
			System.err.println("The authentication with the JIRA server failed.");
			return null;
		}
		GitClient gitClient = GitClient.getOrCreate();
		KnowledgeGraph linker = new KnowledgeGraphImpl(gitClient, jiraClient);
		KnowledgeGraphView knowledgeGraphView = new KnowledgeGraphViewImpl();
		knowledgeGraphView.createView(linker);
		return null;
	}
}