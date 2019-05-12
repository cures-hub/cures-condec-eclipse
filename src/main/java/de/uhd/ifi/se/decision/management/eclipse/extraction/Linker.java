package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.Map;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.model.INode;

public interface Linker {

	GitClient getGitClient();

	void setGitClient(GitClient gitManager);

	JiraClient getJiraClient();

	void setJiraClient(JiraClient jiraManager);

	Map<INode, Set<INode>> createFullMap();

	/**
	 * @param node The node, which should be analyzed for further links.
	 * @param How  deep is the function allowed to go to through the nodes.
	 * @return Returns a Set of all visited nodes.
	 * 
	 * @issue There is no known API for getting all Commits touching a given
	 *        codeline. Only the last commit, which changed a specific line, can be
	 *        retrieved.
	 */
	Set<INode> createLinks(INode node, int maxDepth);

}