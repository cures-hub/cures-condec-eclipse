package de.uhd.ifi.se.decision.management.eclipse.extraction;

import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public interface KnowledgeGraph {

	Graph<Node, Link> getGraph();

	GitClient getGitClient();

	JiraClient getJiraClient();
}