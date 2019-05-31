package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.LinkerImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.MapDesigner;

public class ShowFullGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!CommandHelper.isValidSelection(event)) {
			return null;
		}
		JiraClient jiraClient = JiraClient.getOrCreate();
		if (!jiraClient.isAuthenticated()) {
			System.err.println("The authentication with the JIRA server failed.");
			return null;
		}
		GitClient gitClient = GitClientImpl.getOrCreate();
		Linker linker = new LinkerImpl(gitClient, jiraClient);
		MapDesigner mapDesigner = MapDesigner.getOrCreate();
		mapDesigner.createFullMap(linker);
		return null;
	}
}