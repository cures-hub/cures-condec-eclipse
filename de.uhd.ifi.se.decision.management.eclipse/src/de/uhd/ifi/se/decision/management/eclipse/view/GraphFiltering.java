package de.uhd.ifi.se.decision.management.eclipse.view;

import java.util.LinkedHashMap;
import java.util.Map;

import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

public class GraphFiltering {

	public Map<String, Filter> filters;

	public GraphFiltering() {
		this.filters = initFilters();
	}

	private Map<String, Filter> initFilters() {
		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		filters.put("commit", new Filter("Commits", GitCommitImpl.class));
		filters.put("jiraIssue", new Filter("JIRA Issues", JiraIssueImpl.class));

		filters.put("decisionKnowledge", new Filter("Decision Knowledge", DecisionKnowledgeElementImpl.class));
		filters.put("issue", new Filter("Issues", DecisionKnowledgeElementImpl.class));
		filters.put("decision", new Filter("Decisions", DecisionKnowledgeElementImpl.class));
		filters.put("alternative", new Filter("Alternatives", DecisionKnowledgeElementImpl.class));
		filters.put("pro", new Filter("Pros", DecisionKnowledgeElementImpl.class));
		filters.put("con", new Filter("Cons", DecisionKnowledgeElementImpl.class));

		filters.put("file", new Filter("Files", ChangedFileImpl.class));
		filters.put("nonJava", new Filter("No Java-Files", ChangedFileImpl.class));
		filters.put("class", new Filter("Classes", ChangedFileImpl.class));
		filters.put("method", new Filter("Methods", CodeMethodImpl.class));
		return filters;
	}

	public boolean shouldBeVisible(Node node) {
		for (Map.Entry<String, Filter> entry : filters.entrySet()) {
			Filter filter = entry.getValue();
			if (filter.getNodeClass() != null && node.getClass().equals(filter.getNodeClass())
					&& filter.isActivated()) {
				if (filter.getNodeClass().equals(DecisionKnowledgeElementImpl.class)) {
					if (!filters.get("decisionKnowledge").isActivated()) {
						return false;
					}
					DecisionKnowledgeElement dke = (DecisionKnowledgeElement) node;
					switch (dke.getType()) {
					case ALTERNATIVE:
						if (!filters.get("alternative").isActivated())
							return false;
						break;
					case CON:
						if (!filters.get("con").isActivated())
							return false;
						break;
					case DECISION:
						if (!filters.get("decision").isActivated())
							return false;
						break;
					case ISSUE:
						if (!filters.get("issue").isActivated())
							return false;
						break;
					case PRO:
						if (!filters.get("pro").isActivated())
							return false;
						break;
					default:
						return true;
					}
				} else if (filter.getNodeClass().equals(ChangedFileImpl.class)) {
					if (!filters.get("file").isActivated()) {
						return false;
					}
					ChangedFile cc = (ChangedFile) node;
					if (cc.isJavaClass()) {
						return filters.get("class").isActivated();
					} else {
						return filters.get("nonJava").isActivated();
					}
				}
				return true;
			}
		}
		return false;
	}
}
