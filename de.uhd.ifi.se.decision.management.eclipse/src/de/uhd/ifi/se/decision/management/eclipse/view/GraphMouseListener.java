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

/**
 * Implementation of the Gephi-PreviewMouseListener
 *
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {
	
	@Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
		createPopupMenu(event, properties, workspace);
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	createPopupMenu(event, properties, workspace);
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	createPopupMenu(event, properties, workspace);
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	createPopupMenu(event, properties, workspace);
    }

    /**
     * Checks if a mouse click of the mouse event was on a node
     * @param node
     * 		the node that is checked
     * @param event
     * 		the event containing the mouse click
     * @return
     * 		true, if the node was clicked; false, if the node was not clicked
     */
    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
    	float xdiff = node.x() - event.x;
        float ydiff = -node.y() - event.y;
        float radius = node.size();

        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }
    
    /**
     * Creates a popup menu if a node was right clicked
     * @param event
     * 		the mouse event containing the mouse click
     * @param properties
     * 		the properties of the mouse event
     * @param workspace
     * 		the workspace this is taking place in
     * @return
     * 		true, if a popup-menu was created; false, if no popup-menu was created
     */
    private boolean createPopupMenu(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	if (event.button == PreviewMouseEvent.Button.RIGHT) {
			for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
				if (clickingInNode(node, event)) {
        			PopupMenu popup = new PopupMenu(node);
            		Component component = event.keyEvent.getComponent();
            		Point point = component.getMousePosition();
            		popup.show(component, point.x, point.y);
            		
                    return true;
        		}
				
            }

        }
    	
    	return false;

    }

}
