package de.uhd.ifi.se.decision.management.eclipse.persistence;

import java.awt.Color;

import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;

import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.view.LayoutType;

public class GraphSettings {
	public static LayoutType getLayoutType() {
		return LayoutType.NOVERLAP;
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
		} else if (node instanceof ChangedFile) {
			return GraphSettings.getChangedFilesColor();
		} else if (node instanceof CodeMethod) {
			return GraphSettings.getCodeMethodColor();
		}
		return Color.PINK;
	}
	
	public static PreviewModel initPreviewModel(PreviewController previewController) {
		PreviewModel previewModel = previewController.getModel();
		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(Color.WHITE));
		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.BLACK);
		return previewModel;
	}
}
