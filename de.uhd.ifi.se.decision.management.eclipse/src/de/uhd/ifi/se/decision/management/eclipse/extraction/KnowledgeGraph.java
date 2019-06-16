package de.uhd.ifi.se.decision.management.eclipse.extraction;

import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Interface to create a knowledge graph for the entire project or a sub-graph
 * from a given start node with a certain distance (set in the constructor). The
 * knowledge covers decision knowledge, JIRA issues such as requirements and
 * work items, commits, files (e.g., Java classes), and methods.
 * 
 * @see GitClient
 * @see JiraClient
 * @see Graph
 */
public interface KnowledgeGraph extends Graph<Node, Link> {

	/**
	 * Returns the git client to connect to a git repository associated with this
	 * Eclipse project. Retrieves commits and code changes (diffs) in git. It can be
	 * set in the constructor.
	 * 
	 * @see GitClient
	 * 
	 * @return git client to connect to a git repository.
	 */
	GitClient getGitClient();

	/**
	 * Returns the JIRA client to connect to a JIRA project associated with this
	 * Eclipse project. Retrieves JIRA issues. It can be set in the constructor.
	 * 
	 * @see JiraClient
	 * 
	 * @return JIRA client to connect to a JIRA project.
	 */
	JiraClient getJiraClient();
}