package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.JumpToUtils;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

/**
 * Implementation of a popup-menu for clicking on a node
 *
 */
public class PopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -4578618714780522965L;
	
    /**
     * Creates a popup-menu on a selected node.
     * @param selectedNode
     * 		the node that was clicked on
     */
	public PopupMenu(Node selectedNode) {
    	
		de.uhd.ifi.se.decision.management.eclipse.model.Node node = convertNode(selectedNode);
		
		JMenuItem jumpTo = new JMenuItem("Jump to");
		JMenuItem highlightNode = new JMenuItem("Highlight node");
		JMenuItem clippedGraph = new JMenuItem("Show clipped graph");
		JMenuItem fullGraph = new JMenuItem("Show full graph");
		JMenuItem createNode = new JMenuItem("Create node");
		JMenuItem createLink = new JMenuItem("Create link to");
		JMenuItem removeLink = new JMenuItem("Remove link to");
    	
    	if (node != null) {
    		
    		jumpTo.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
    				JumpToUtils.jumpTo(node);
        		}
        	});
    		
    		add(jumpTo);
    		
    		highlightNode.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
    				highlight(node);
        		}
        	});
    		
    		add(highlightNode);
    		
    		clippedGraph.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			createClippedGraph(node);
        		}
        	});
    		
    		add(clippedGraph);
    		
    	}
    	
    	fullGraph.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			createFullGraph();
    		}
    	});
    	
    	add(fullGraph);
    	
    	createNode.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			createNode();
    		}
    	});
    	
    	add(createNode);
    	
    	if (node != null) {
    		
    		clippedGraph.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			createClippedGraph(node);
        		}
        	});
    		
    		add(clippedGraph);
    		
    	}
    	
    	fullGraph.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			createFullGraph();
    		}
    	});
    	
    	add(fullGraph);
    	
    	if (node != null) {
    		
    		createLink.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			PreviewSketch.createLink = true;
        		}
        	});
    		
    		if (!(node instanceof CodeMethod)) {
    			add(createLink);
    		}
    		
    		add(createLink);
    		
    		removeLink.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			PreviewSketch.removeLink = true;
        		}
        	});
    		
    		if (!(node instanceof CodeMethod)) {
    			add(removeLink);
    		}
    		
    		add(removeLink);
    	}
    }
    
    /**
     * Converts a gephi-node to a knowledge graph-node.
     * @param selectedNode
     * 		the gephi-node to be converted
     * @return
     * 		the converted knowledge graph-node
     */
	private de.uhd.ifi.se.decision.management.eclipse.model.Node convertNode(Node selectedNode) {
    	de.uhd.ifi.se.decision.management.eclipse.model.Node node = null;
    	
    	if (selectedNode != null) {
    		String nodeLabel = selectedNode.getLabel();
        	int start = nodeLabel.indexOf('[') + 1;
        	int end = nodeLabel.indexOf(']');
        	int nodeId = Integer.parseInt(nodeLabel.substring(start, end));
        	node = de.uhd.ifi.se.decision.management.eclipse.model.Node.getNodeById(nodeId);
    	}
    	
    	return node;
    }
	
	/**
     * Creates a full knowledge graph.
     */
	private void createFullGraph() {
		KnowledgeGraphImpl.clear();
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
		knowledgeGraph.updateWithPersistanceData();
		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
		knowledgeGraphView.update(knowledgeGraph);
    }
	
	/**
     * Creates a clipped knowledge graph from a start node.
     * @param node
     * 		the start node the knowledge graph is generated from
     */
	private void createClippedGraph(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
		KnowledgeGraphImpl.clear();
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(node, ConfigPersistenceManager.getLinkDistance());
		knowledgeGraph.updateWithPersistanceData();
		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
		knowledgeGraphView.update(knowledgeGraph);
    }
	
	/**
     * Creates a new knowledge graph node.
     */
	private void createNode() {
		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
		knowledgeGraphView.createNode();
    }
	
	/**
     * Highlights a node in the knowledge graph
     * @param node
     * 		the node to be highlighted
     */
	private void highlight(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
		knowledgeGraphView.highlightSelectedNodeAndUpdate(node);
    }
}
