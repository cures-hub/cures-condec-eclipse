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

/**
 * Implementation of the Gephi-PreviewMouseListener
 *
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {
	
	@Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
		createPopupMenu(event, workspace);
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	createPopupMenu(event, workspace);
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	createPopupMenu(event, workspace);
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	createPopupMenu(event, workspace);
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
			for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
				if (NodeUtils.clickInNode(node, event.x, event.y)) {
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
