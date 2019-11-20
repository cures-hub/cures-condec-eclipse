package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.Component;
import java.awt.Point;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import de.uhd.ifi.se.decision.management.eclipse.event.NodeUtils;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;

/**
 * Implementation of the Gephi-PreviewMouseListener
 *
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {
	
	public static boolean createLink = false;
	private static Node sourceNode = null;
	private static Node targetNode = null;
	
	@Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
		
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	if (createLink == false) {
			createPopupMenu(event, workspace);
			sourceNode = getClickedNode(event, workspace);
		}
		else if (createLink == true) {
			targetNode = getClickedNode(event, workspace);
			
			createLink();
			
			createLink = false;
			sourceNode = null;
			targetNode = null;
		}
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	
    }
    
    /**
     * Checks if a node was clicked and returns it
     * @param event
     * 		the mouse event containing the mouse click
     * @param workspace
     * 		the workspace this is taking place in
     * @return
     * 		if a node was clicked, the node that was clicked; alse null
     */
    private Node getClickedNode(PreviewMouseEvent event, Workspace workspace) {
		for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
			if (NodeUtils.clickInNode(node, event.x, event.y)) {
    			return node;
    		};
			
        }

    	return null;

    }
    
    /**
     * Creates a popup menu if a node was right clicked
     * @param event
     * 		the mouse event containing the mouse click
     * @param workspace
     * 		the workspace this is taking place in
     * @return
     * 		true, if a popup-menu was created; false, if no popup-menu was created
     */
    private boolean createPopupMenu(PreviewMouseEvent event, Workspace workspace) {
    	if (event.button == PreviewMouseEvent.Button.RIGHT) {
			Node selectedNode = getClickedNode(event, workspace);
			PopupMenu popup = new PopupMenu(selectedNode);
    		Component component = event.keyEvent.getComponent();
    		Point point = component.getMousePosition();
    		popup.show(component, point.x, point.y);
    		
            return true;

        }
    	
    	return false;

    }
    
    /**
     * Creates a link between the source node and the target node, if sourceNode and targetNode exist.
     * 
     */
    private void createLink() {
    	KnowledgeGraph graph = new KnowledgeGraphImpl();
    	
    	if ((targetNode != null) && (sourceNode != null)) {
    		if (graph.linkExists(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode)) == false) {
    			graph.createLink(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode));
    			System.out.println("Link created!");
    		}
    		else {
    			System.out.println("Link already exists!");
    		}
		}

    }

}
