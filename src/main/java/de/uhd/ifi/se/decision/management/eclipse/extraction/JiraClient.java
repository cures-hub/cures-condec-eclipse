package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

public interface JiraClient {

	/**
	 * Login to JIRA
	 */
	public int authenticate(String jiraURL, String username, String password);

	public int authenticate();

	public int getAuthenticationResponse(String username);

	public Issue getIssue(String issueKey);

	public Set<JiraIssueImpl> getAllIssues();

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
}
