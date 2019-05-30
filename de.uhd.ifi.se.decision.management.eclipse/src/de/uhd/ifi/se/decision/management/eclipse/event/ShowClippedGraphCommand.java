package de.uhd.ifi.se.decision.management.eclipse.event;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.LinkerImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.MapDesigner;

public class ShowClippedGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPath pathOfSelectedFile = CommandHelper.getPathOfSelectedFile(event);
		if (pathOfSelectedFile == null || !pathOfSelectedFile.toFile().exists()) {
			return null;
		}

		JiraClient jiraClient = JiraClientImpl.getOrCreate();
		if (jiraClient.authenticate() != 0) {
			System.out.println("There was an error when authenticate JiraManager");
		}
		Linker linker = new LinkerImpl(GitClientImpl.getOrCreate(), jiraClient);
		// Just to load all elements and look for the invoked .java file in the
		// CodeClass-instances
		linker.createFullMap();
		Node rootNode = null;
		for (CodeClass cc : CodeClassImpl.getInstances()) {
			IPath fileloc = new Path(cc.getFileLocation());
			if (fileloc.equals(pathOfSelectedFile)) {
				rootNode = cc;
				break;
			}
		}
		if (rootNode != null) {
			MapDesigner mapDesigner = MapDesigner.getOrCreate();
			mapDesigner.createSelectedMap(rootNode, ConfigPersistenceManager.getLinkDistance(), linker);
		} else {
			JOptionPane optionPane = new JOptionPane("Could not find the selected file in the repository!",
					JOptionPane.WARNING_MESSAGE);
			JDialog dialog = optionPane.createDialog("Error!");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
		return null;
	}
}
