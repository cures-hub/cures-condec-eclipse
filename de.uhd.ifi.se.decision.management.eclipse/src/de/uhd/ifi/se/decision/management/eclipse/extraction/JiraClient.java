package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Interface to connect to a JIRA project associated with this Eclipse project.
 * Retrieves JIRA issues.
 * 
 * @see JIRAIssue
 */
public interface JiraClient {

	/**
	 * Instances of the JiraClient that are identified by the Uniform Resource
	 * Identifier (URI) of the JIRA server.
	 */
	public Map<URI, JiraClient> instances = new HashMap<URI, JiraClient>();

	public static JiraClient getOrCreate() {
		URI uri = ConfigPersistenceManager.getJiraURI();
		JiraClient jiraClient;
		if (JiraClient.instances.containsKey(uri)) {
			jiraClient = JiraClient.instances.get(uri);
			jiraClient.authenticate();
		} else {
			jiraClient = new JiraClientImpl();
			JiraClient.instances.put(uri, jiraClient);
		}
		return jiraClient;
	}

	/**
	 * Authenticates with JIRA. Uses the settings stored in the
	 * ConfigPersistenceManager for authentication.
	 * 
	 * @see ConfigPersistenceManager
	 * @return true if authentication was successfull, otherwise false.
	 */
	public boolean authenticate();

	/**
	 * Authenticates with JIRA.
	 * 
	 * @param jiraURI
	 *            Uniform Resource Identifier(URI) to the JIRA server.
	 * @param username
	 *            JIRA username as a String.
	 * @param password
	 *            to authenticate with JIRA.
	 * 
	 * @return true if authentication was successfull, otherwise false.
	 */
	public boolean authenticate(URI jiraURI, String username, String password);

	/**
	 * Closes the JIRA REST client.
	 */
	public void close();

	public Set<JiraIssue> getAllJiraIssues();

	public boolean isValidProject(String projectKey);

	boolean isValidProject();

	/**
	 * Retrieves the JIRA issue for the given key.
	 * 
	 * @param jiraIssueKey
	 *            JIRA issue key as a String.
	 * @return JIRA issue for the given key.
	 */
	public Issue getJiraIssue(String jiraIssueKey);

	public JiraRestClient getJiraRestClient();

	/**
	 * Retrieves keys of linked issue to an issue at link distance 1
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return keys of linked issues
	 */
	public List<String> getKeysOfNeighborIssues(Issue issue);

	/**
	 * Retrieves the issues linked to a given issue with a certain link distance
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return Linked issues mapped to link distance
	 */
	public Map<Issue, Integer> getLinkedIssues(Issue issue, int distance);

	public void setJiraRestClient(JiraRestClient jiraRestClient);

	public boolean isAuthenticated();

}
