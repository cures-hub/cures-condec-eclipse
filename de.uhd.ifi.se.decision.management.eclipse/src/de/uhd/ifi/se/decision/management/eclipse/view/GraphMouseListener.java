package de.uhd.ifi.se.decision.management.eclipse.view;

import javax.swing.JOptionPane;

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
		for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
//			if (event.button == PreviewMouseEvent.Button.RIGHT) {
//				if (clickingInNode(node, event)) {
//        			ContextMenu contextMenu = new ContextMenu(node);
//            		contextMenu.show(event.keyEvent.getComponent(), event.x, event.y);
//        		}
//                event.setConsumed(true);
//                return;
//            }
			if (clickingInNode(node, event)) {
                properties.putValue("display-label.node.id", node.getId());
                System.err.println("Node " + node.getLabel() + " clicked!");//System.out is ignored in Netbeans platform applications!!
                JOptionPane.showMessageDialog(null, "Node " + node.getLabel() + " clicked!");
                event.setConsumed(true);//So the renderer is executed and the graph repainted
                return;
            }
        }

        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    }

    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
        float xdiff = node.x() - event.x;
        float ydiff = -node.y() - event.y;
        float radius = node.size();

        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }

}
