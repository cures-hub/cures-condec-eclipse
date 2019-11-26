package de.uhd.ifi.se.decision.management.eclipse.persistence;

import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.NodeUtils;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

/**
 * Manages the persistence of knowledge.
 */
public class KnowledgePersistenceManager {
	
	/**
     * Creates a link between the source node and the target node, if sourceNode and targetNode exist.
     *
     * @param sourceNode
	 * 		the source node
	 * @param targetNode
	 * 		the target node
     */
    static public void createLink(Node sourceNode, Node targetNode) {
    	KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
    	
    	if ((targetNode != null) && (sourceNode != null)) {
    		if (knowledgeGraph.linkExists(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode)) == false) {
    			knowledgeGraph.createLink(NodeUtils.convertNode(sourceNode), NodeUtils.convertNode(targetNode));
    		}
		}
    	
    	KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
    	
    	knowledgeGraphView.update(knowledgeGraph);
    }
	
}
