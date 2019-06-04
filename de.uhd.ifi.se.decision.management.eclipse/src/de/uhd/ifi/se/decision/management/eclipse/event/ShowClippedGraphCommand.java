package de.uhd.ifi.se.decision.management.eclipse.event;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.LinkerImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class ShowClippedGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPath pathOfSelectedFile = CommandHelper.getPathOfSelectedFile(event);
		if (pathOfSelectedFile == null || !pathOfSelectedFile.toFile().exists()) {
			return null;
		}
		JiraClient jiraClient = JiraClient.getOrCreate();
		if (!jiraClient.isWorking()) {
			System.err.println("The authentication with the JIRA server failed.");
			return null;
		}

		Linker linker = new LinkerImpl(GitClient.getOrCreate(), jiraClient);
		// Just to load all elements and look for the invoked .java file in the
		// CodeClass-instances
		linker.createKnowledgeGraph();
		Node rootNode = null;
		for (CodeClass cc : CodeClass.getInstances()) {
			IPath fileloc = new Path(cc.getFileLocation());
			if (fileloc.equals(pathOfSelectedFile)) {
				rootNode = cc;
				break;
			}
		}
		if (rootNode != null) {
			KnowledgeGraphView knowledgeGraphView = new KnowledgeGraphViewImpl();
			knowledgeGraphView.createView(rootNode, ConfigPersistenceManager.getLinkDistance(), linker);
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
