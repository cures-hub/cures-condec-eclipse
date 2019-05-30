package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.LinkerImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.MapDesigner;

public class ShowFullGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!CommandHelper.isValidSelection(event)) {
			return null;
		}
		JiraClient jiraClient = JiraClientImpl.getOrCreate();
		if (jiraClient.authenticate() != 0) {
			System.out.println("There was an error when authenticate JiraManager");
		}
		GitClient gitClient = GitClientImpl.getOrCreate();
		Linker linker = new LinkerImpl(gitClient, jiraClient);
		MapDesigner mapDesigner = MapDesigner.getOrCreate();
		mapDesigner.createFullMap(linker);
		return null;
	}
}