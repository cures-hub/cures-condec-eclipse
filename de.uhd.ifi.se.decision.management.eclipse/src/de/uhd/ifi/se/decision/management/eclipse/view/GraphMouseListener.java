package de.uhd.ifi.se.decision.management.eclipse.view;

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
	
//	private Node getSelectedNode(int eX, int eY) {
//		Node selectedNode = null;
//		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
//		Graph graph = graphModel.getGraph();
//		NodeIterable nodes = graph.getNodes();
//		Iterator<Node> nodeIterator = nodes.iterator();
//		System.out.println(Integer.toString(eX) + " " + Integer.toString(eY));
//		while (nodeIterator.hasNext()) {
//	        Node nextNode = nodeIterator.next();
//	        System.out.println(Float.toString(nextNode.x()) + " " + Float.toString(nextNode.y()));
//	        float x = nextNode.x();
//	        float y = -nextNode.y();
//	        float size = nextNode.size();
//	        final int minX = Math.round(x - size);
//	        final int maxX = Math.round(x + size);
//	        final int minY = Math.round(y - size);
//	        final int maxY = Math.round(y + size);
//	        if (minX <= eX && eX <= maxX && minY <= eY && eY <= maxY) {
//	            selectedNode = nextNode;
//	        }
//	    }
//		return selectedNode;
//	}
	
	@Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
		for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
            if (clickingInNode(node, event)) {
                properties.putValue("display-label.node.id", node.getId());
                if (event.button == PreviewMouseEvent.Button.RIGHT) {
        			ContextMenu contextMenu = new ContextMenu(node);
            		contextMenu.show(event.keyEvent.getComponent(), event.x, event.y);
        		}
                event.setConsumed(true);
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
