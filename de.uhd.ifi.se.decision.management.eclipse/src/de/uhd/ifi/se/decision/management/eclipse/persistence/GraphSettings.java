package de.uhd.ifi.se.decision.management.eclipse.persistence;

import java.awt.Color;

import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
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
		if (node instanceof GitCommitImpl) {
			return GraphSettings.getCommitColor();
		} else if (node instanceof DecisionKnowledgeElementImpl) {
			return GraphSettings.getDecisionKnowledgeElementColor();
		} else if (node instanceof JiraIssueImpl) {
			return GraphSettings.getJiraIssueColor();
		} else if (node instanceof CodeClassImpl) {
			return GraphSettings.getChangedFilesColor();
		} else if (node instanceof CodeMethodImpl) {
			return GraphSettings.getCodeMethodColor();
		}
		return Color.PINK;
	}
}
