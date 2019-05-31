package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public interface JiraClient {

	/**
	 * Instances of the JiraClient that are identified by the URL of the JIRA
	 * server.
	 */
	Map<String, JiraClient> instances = new HashMap<String, JiraClient>();

	/**
	 * Authenticates with JIRA.
	 * 
	 * @param jiraURL
	 *            URL to the JIRA server.
	 * @param username
	 *            JIRA username as a String.
	 * @param password
	 *            to authenticate with JIRA.
	 */
	public int authenticate(String jiraURL, String username, String password);

	public int authenticate();

	public int getAuthenticationResponse(String username);

	public Issue getIssue(String issueKey);

	public Set<JiraIssue> getAllIssues();

	/**
	 * Retrieves the issues linked to a given issue with a certain link distance
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return Linked issues mapped to link distance
	 */
	public Map<Issue, Integer> getLinkedIssues(Issue issue, int distance);

	/**
	 * Retrieves keys of linked issue to an issue at link distance 1
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return keys of linked issues
	 */
	public List<String> getKeysOfNeighborIssues(Issue issue);

	/**
	 * Closes the client when finished
	 */
	public void close();

	public boolean isAuthenticated();

	public JiraRestClient getJiraRestClient();

	public void setJiraRestClient(JiraRestClient jiraRestClient);

	static JiraClient getOrCreate() {
		String url = ConfigPersistenceManager.getJiraUrl();
		if (JiraClient.instances.containsKey(url)) {
			return JiraClient.instances.get(url);
		}
		JiraClient jiraClient = new JiraClientImpl();
		JiraClient.instances.put(url, jiraClient);
		return jiraClient;
	}
}
