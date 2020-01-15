package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.Test;
import org.openide.util.Lookup;

public class TestPopupMenu {
	
	@Test
	public void testPopupMenu() {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node node = graphModel.factory().newNode("node");
        node.setLabel("[0] Node");
        
        assertNotNull(new PopupMenu(node));
	}
	
	public void testPopupMenuNull() {
        assertNull(new PopupMenu(null));
	}

}
