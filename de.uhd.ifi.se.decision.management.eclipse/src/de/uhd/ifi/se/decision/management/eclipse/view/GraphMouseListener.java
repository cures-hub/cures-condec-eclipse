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

    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
    	float xdiff = node.x() - event.x;
        float ydiff = -node.y() - event.y;
        float radius = node.size();

        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }
    
    private void createPopupMenu(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    	if (event.button == PreviewMouseEvent.Button.RIGHT) {
			for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
				if (clickingInNode(node, event)) {
        			PopupMenu popup = new PopupMenu(node);
            		Component component = event.keyEvent.getComponent();
            		Point point = component.getMousePosition();
            		popup.show(component, point.x, point.y);
            		
                    return;
        		}
				
            }

        }

    }

}
