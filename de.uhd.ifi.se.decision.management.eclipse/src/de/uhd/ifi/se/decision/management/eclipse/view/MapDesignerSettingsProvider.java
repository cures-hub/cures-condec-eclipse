package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.Color;

public class MapDesignerSettingsProvider {
	public static LayoutType getLayoutType() {
		return LayoutType.YIFAN_HU;
	}

	public static Color getCommitColor() {
		return Color.RED;
	}

	public static Color getIssueColor() {
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

	public static Color getFeatureElementColor() {
		return Color.LIGHT_GRAY;
	}
}
