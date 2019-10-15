package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gephi.graph.api.Node;

import de.uhd.ifi.se.decision.management.eclipse.event.JumpToCommandHelper;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

public class ContextMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -4578618714780522965L;
	private final JMenuItem jumpTo;
	
    public ContextMenu(Node selectedNode) {
    	jumpTo = new JMenuItem("Jump to");
    	
    	if (selectedNode != null) {
    		
    		String nodeLabel = selectedNode.getLabel();
        	int first = nodeLabel.indexOf('[');
        	int second = nodeLabel.indexOf('[', first+1);
        	int nodeId = Integer.parseInt(nodeLabel.substring(first, second));
        	de.uhd.ifi.se.decision.management.eclipse.model.Node node = 
        			de.uhd.ifi.se.decision.management.eclipse.model.Node.getNodeById(nodeId);
    		
        	if (node != null) {
        		jumpTo.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
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
            	});
        		
        		add(jumpTo);
        	}
    	}
    }
}
