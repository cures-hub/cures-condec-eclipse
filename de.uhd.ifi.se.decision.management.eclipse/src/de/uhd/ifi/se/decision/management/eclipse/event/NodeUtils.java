package de.uhd.ifi.se.decision.management.eclipse.event;

import org.gephi.graph.api.Node;

/**
 * Contains static methods for nodes
 *
 */
public class NodeUtils {
	
	/**
     * converts a gephi-node to a knowledge graph-node
     * @param selectedNode
     * 		the gephi-node to be converted
     * @return
     * 		the converted knowledge graph-node
     */
	public static de.uhd.ifi.se.decision.management.eclipse.model.Node convertNode(Node selectedNode) {
    	
    	de.uhd.ifi.se.decision.management.eclipse.model.Node node = null;
    	
    	if (selectedNode != null) {
    		try {
    			int nodeID = Integer.parseInt((String) selectedNode.getId());
    			node = de.uhd.ifi.se.decision.management.eclipse.model.Node.getNodeById(nodeID);
    		}
    		catch(NumberFormatException e) {
    			return null;
    		}
    	}
    	
    	if (!(node instanceof de.uhd.ifi.se.decision.management.eclipse.model.Node)) {
    		return null;
    	}
    	
    	return node;
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
	public static boolean clickInNode(Node node, int x, int y) {
    	float xdiff = node.x() - x;
        float ydiff = -node.y() - y; //Note that y axis is inverse for node coordinates
        float radius = node.size();

        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }

}
