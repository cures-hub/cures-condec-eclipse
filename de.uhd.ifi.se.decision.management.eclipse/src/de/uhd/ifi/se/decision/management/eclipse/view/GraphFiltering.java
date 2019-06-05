package de.uhd.ifi.se.decision.management.eclipse.view;

import java.util.HashMap;
import java.util.Map;

import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class GraphFiltering {

	public Map<String, Boolean> filters;

	public GraphFiltering() {
		this.filters = initFilters();
	}

	private Map<String, Boolean> initFilters() {
		Map<String, Boolean> filterCheckBoxes = new HashMap<String, Boolean>();

		filterCheckBoxes.put("commit", true);
		filterCheckBoxes.put("jiraIssue", true);

		filterCheckBoxes.put("decisionKnowledge", true);
		filterCheckBoxes.put("issue", true);
		filterCheckBoxes.put("decision", true);
		filterCheckBoxes.put("alternative", true);
		filterCheckBoxes.put("pro", true);
		filterCheckBoxes.put("con", true);

		filterCheckBoxes.put("file", true);
		filterCheckBoxes.put("nonJava", true);
		filterCheckBoxes.put("class", true);
		filterCheckBoxes.put("method", true);
		return filterCheckBoxes;
	}

	public boolean shouldBeVisible(Node node) {
		if (node instanceof GitCommit && filters.get("commit") || node instanceof JiraIssue && filters.get("jiraIssue")
				|| node instanceof CodeMethod && filters.get("method")) {
			return true;
		} else if (node instanceof DecisionKnowledgeElement && filters.get("decisionKnowledge")) {
			DecisionKnowledgeElement dke = (DecisionKnowledgeElement) node;
			switch (dke.getKnowledgeType()) {
			case ALTERNATIVE:
				if (filters.get("alternative"))
					return true;
				break;
			case CON:
				if (filters.get("con"))
					return true;
				break;
			case DECISION:
				if (filters.get("decision"))
					return true;
				break;
			case ISSUE:
				if (filters.get("issue"))
					return true;
				break;
			case PRO:
				if (filters.get("pro"))
					return true;
				break;
			default:
				return true;
			}
			return false;
		} else if (node instanceof CodeClass && filters.get("file")) {
			CodeClass cc = (CodeClass) node;
			if (cc.getPath().getFileExtension().equalsIgnoreCase("java")) {
				if (filters.get("class")) {
					return true;
				} else {
					return false;
				}
			} else {
				if (filters.get("nonJava")) {
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
