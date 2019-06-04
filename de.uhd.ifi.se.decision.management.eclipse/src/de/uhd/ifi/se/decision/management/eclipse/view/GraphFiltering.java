package de.uhd.ifi.se.decision.management.eclipse.view;

import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class GraphFiltering {

	public boolean bShowCommits = true;
	public boolean bShowDecisionKnowledge = true;
	public boolean bShowKIDecision = true;
	public boolean bShowKIIssue = true;
	public boolean bShowKIAlternative = true;
	public boolean bShowKICon = true;
	public boolean bShowKIPro = true;
	public boolean bShowKIGoal = true;
	public boolean bShowKIOther = true;
	public boolean bShowIssues = true;
	public boolean bShowMethods = true;
	public boolean bShowFiles = true;
	public boolean bShowCFClasses = true;
	public boolean bShowCFOther = true;

	public boolean shouldBeVisible(Node node) {
		if (node instanceof GitCommit && bShowCommits || node instanceof JiraIssue && bShowIssues
				|| node instanceof CodeMethod && bShowMethods) {
			return true;
		} else if (node instanceof DecisionKnowledgeElement && bShowDecisionKnowledge) {
			DecisionKnowledgeElement dke = (DecisionKnowledgeElement) node;
			switch (dke.getKnowledgeType()) {
			case ALTERNATIVE:
				if (bShowKIAlternative)
					return true;
				break;
			case CON:
				if (bShowKICon)
					return true;
				break;
			case DECISION:
				if (bShowKIDecision)
					return true;
				break;
			case GOAL:
				if (bShowKIGoal)
					return true;
				break;
			case ISSUE:
				if (bShowKIIssue)
					return true;
				break;
			case PRO:
				if (bShowKIPro)
					return true;
				break;
			case OTHER:
				if (bShowKIOther)
					return true;
				break;
			default:
				return true;
			}
			return false;
		} else if (node instanceof CodeClass && bShowFiles) {
			CodeClass cc = (CodeClass) node;
			if (cc.getPath().getFileExtension().equalsIgnoreCase("java")) {
				if (bShowCFClasses) {
					return true;
				} else {
					return false;
				}
			} else {
				if (bShowCFOther) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

}
