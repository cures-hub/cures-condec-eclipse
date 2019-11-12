package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.JumpToCommandHelper;
import de.uhd.ifi.se.decision.management.eclipse.event.NodeUtils;
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
    	
    	de.uhd.ifi.se.decision.management.eclipse.model.Node node = NodeUtils.convertNode(selectedNode);
    		
    	if (node != null) {
    		
    		jumpTo.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
    				jumpTo(node);
        		}
        	});
    		
    		add(jumpTo);
    		
    		createLink.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
    				
        		}
        	});
    		
    		add(createLink);
    		
    	}
    }
    
    /**
     * calls the correct jumpTo-method for the node
     * @param node
     * 		the node to be jumped to
     */
	private void jumpTo(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
    	if (node instanceof JiraIssueImpl) {
			JumpToCommandHelper.jumpToJiraIssue((JiraIssue) node);
		}
		else if (node instanceof GitCommit) {
			JumpToCommandHelper.jumpToGitCommit((GitCommit) node);
		}
		else if (node instanceof ChangedFile) {
			JumpToCommandHelper.jumpToChangedFile((ChangedFile) node);
		}
		else if (node instanceof CodeMethod) {
			JumpToCommandHelper.jumpToMethod((CodeMethod) node);
		}
		else if (node instanceof DecisionKnowledgeElement) {
			JumpToCommandHelper.jumpToDecisionKnowledgeElement((DecisionKnowledgeElement) node);
		}
    }
}
