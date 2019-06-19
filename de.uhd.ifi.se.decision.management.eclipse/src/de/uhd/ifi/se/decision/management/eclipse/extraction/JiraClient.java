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

	/**
	 * Retrieves an existing JiraClient instance or creates a new instance if there
	 * is no instance for the given URI yet.
	 * 
	 * @return JiraClient instance.
	 */
	public static JiraClient getOrCreate() {
		URI uri = ConfigPersistenceManager.getJiraURI();
		JiraClient jiraClient;
		if (JiraClient.instances.containsKey(uri)) {
			jiraClient = JiraClient.instances.get(uri);
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
	 * 
	 * @see JiraRestClient
	 * @return true of JiraClient was closed.
	 */
	public boolean close();

	/**
	 * Retrieves all JIRA issues of the project given in the settings.
	 * 
	 * @see ConfigPersistenceManager
	 * @return all JIRA issues of the project given in the settings.
	 */
	public Set<JiraIssue> getAllJiraIssues();

	/**
	 * Retrieves the JIRA issue for the given key.
	 * 
	 * @param jiraIssueKey
	 *            JIRA issue key as a String.
	 * @return JIRA issue for the given key.
	 */
	public JiraIssue getJiraIssue(String jiraIssueKey);

	/**
	 * Retrieves keys of the JIRA issues linked to a JIRA issue at link distance 1.
	 * 
	 * @param jiraIssue
	 *            JIRA issue.
	 * @return keys of linked JIRA issues as a list of Strings.
	 */
	public List<String> getKeysOfNeighborJiraIssues(Issue jiraIssue);

	/**
	 * Retrieves the issues linked to a given issue with a certain link distance
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return Linked issues mapped to link distance
	 */
	public Map<Issue, Integer> getLinkedJiraIssues(Issue issue, int distance);

	/**
	 * Determines whether the JIRA REST client is authenticated and the project is
	 * accessable.
	 * 
	 * @return true if the JIRA REST client is authenticated and the project is
	 *         accessable
	 */
	public boolean isWorking();

	/**
	 * Returns the JiraRestClient instance.
	 * 
	 * @see JiraRestClient
	 * @return JiraRestClient instance.
	 */
	public JiraRestClient getJiraRestClient();

	/**
	 * Sets the JiraRestClient instance.
	 * 
	 * @see JiraRestClient
	 * @param JiraRestClient
	 *            instance.
	 */
	public void setJiraRestClient(JiraRestClient jiraRestClient);
}