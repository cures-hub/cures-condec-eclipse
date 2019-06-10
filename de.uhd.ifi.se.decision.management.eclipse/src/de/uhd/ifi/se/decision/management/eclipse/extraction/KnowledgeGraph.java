package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.Set;

import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public interface KnowledgeGraph {

	Graph<Node, Link> createGraph();

	/**
	 * @param node
	 *            The node, which should be analyzed for further links.
	 * @param How
	 *            deep is the function allowed to go to through the nodes.
	 * @return Returns a Set of all visited nodes.
	 * 
	 * @issue There is no known API for getting all Commits touching a given
	 *        codeline. Only the last commit, which changed a specific line, can be
	 *        retrieved.
	 */
	Set<Node> createGraph(Node node, int maxDepth);

	GitClient getGitClient();

	JiraClient getJiraClient();

	void setGitClient(GitClient gitManager);

	void setJiraClient(JiraClient jiraManager);

}