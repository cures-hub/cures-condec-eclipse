package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.JumpToUtils;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

/**
 * Implementation of a popup-menu for clicking on a node
 *
 */
public class PopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -4578618714780522965L;
	
    /**
     * Creates a popup-menuon a selected node
     * @param selectedNode
     * 		the node that was clicked on
     */
	public PopupMenu(Node selectedNode) {
    	
		JMenuItem jumpTo = new JMenuItem("Jump to");
		JMenuItem createLink = new JMenuItem("Create link to");
		JMenuItem removeLink = new JMenuItem("Remove link to");
    	
    	de.uhd.ifi.se.decision.management.eclipse.model.Node node = convertNode(selectedNode);
    		
    	if (node != null) {
    		jumpTo.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
    				jumpTo(node);
        		}
        	});
    		
    		add(jumpTo);
    		
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
     * converts a gephi-node to a knowledge graph-node
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
     * calls the correct jumpTo-method for the node
     * @param node
     * 		the node to be jumped to
     */
	private void jumpTo(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
    	if (node instanceof JiraIssueImpl) {
			JumpToUtils.jumpToJiraIssue((JiraIssue) node);
		}
		else if (node instanceof GitCommit) {
			JumpToUtils.jumpToGitCommit((GitCommit) node);
		}
		else if (node instanceof ChangedFile) {
			JumpToUtils.jumpToChangedFile((ChangedFile) node);
		}
		else if (node instanceof CodeMethod) {
			JumpToUtils.jumpToMethod((CodeMethod) node);
		}
		else if (node instanceof DecisionKnowledgeElement) {
			JumpToUtils.jumpToDecisionKnowledgeElement((DecisionKnowledgeElement) node);
		}
    }
}
