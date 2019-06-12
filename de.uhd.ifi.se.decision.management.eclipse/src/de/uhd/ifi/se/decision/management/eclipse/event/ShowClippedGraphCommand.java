package de.uhd.ifi.se.decision.management.eclipse.event;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;

import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
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

		Node startNode = getSelectedNode(pathOfSelectedFile);
		if (startNode == null) {
			createErrorDialog();
			return null;
		}

		int distance = ConfigPersistenceManager.getLinkDistance();
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(startNode, distance);
		KnowledgeGraphView knowledgeGraphView = new KnowledgeGraphViewImpl();
		String title = "Knowledge Graph for \"" + startNode.toString() + "\" with Link Distance " + distance;
		knowledgeGraphView.createView(knowledgeGraph, title);

		return null;
	}

	private Node getSelectedNode(IPath pathOfSelectedFile) {
		return ChangedFile.getOrCreate(pathOfSelectedFile);
	}

	private void createErrorDialog() {
		JOptionPane optionPane = new JOptionPane("Could not find the selected file in the repository!",
				JOptionPane.WARNING_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error!");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}
}
