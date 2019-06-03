package de.uhd.ifi.se.decision.management.eclipse.persistence;

import java.awt.Color;

import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.view.LayoutType;

public class GraphSettings {
	public static LayoutType getLayoutType() {
		return LayoutType.YIFAN_HU;
	}

	public static Color getCommitColor() {
		return Color.RED;
	}

	public static Color getJiraIssueColor() {
		return Color.GREEN;
	}

	public static Color getDecisionKnowledgeElementColor() {
		return Color.ORANGE;
	}

	public static Color getChangedFilesColor() {
		return Color.BLUE;
	}

	public static Color getCodeMethodColor() {
		return Color.WHITE;
	}

	public static Color getColor(Node node) {
		if (node instanceof GitCommit) {
			return GraphSettings.getCommitColor();
		} else if (node instanceof DecisionKnowledgeElement) {
			return GraphSettings.getDecisionKnowledgeElementColor();
		} else if (node instanceof JiraIssue) {
			return GraphSettings.getJiraIssueColor();
		} else if (node instanceof CodeClass) {
			return GraphSettings.getChangedFilesColor();
		} else if (node instanceof CodeMethod) {
			return GraphSettings.getCodeMethodColor();
		}
		return Color.PINK;
	}
}
