package de.uhd.ifi.se.decision.management.eclipse.model;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

/**
 * Interface for JIRA issue as part of the knowledge graph.
 */
public interface JiraIssue extends Node {

	/**
	 * Instances of JiraIssue that are identified by the JIRA issue key.
	 */
	public Map<String, JiraIssue> instances = new HashMap<String, JiraIssue>();

	/**
	 * Retrieves an existing JiraIssue instance or creates a new instance if there
	 * is no instance for the given key.
	 * 
	 * @param key
	 *            JIRA issue key.
	 * @param jiraClient
	 *            JiraClient that might be needed to retrieve the JIRA issue from
	 *            the server.
	 * @return instance of JiraIssue or null, if the given key could not be resolved
	 *         to a JIRA issue instance.
	 */
	static JiraIssue getOrCreate(String key, JiraClient jiraClient) {
		if (key == null || key.isEmpty()) {
			return null;
		}

		JiraIssue jiraIssue = jiraClient.getJiraIssue(key);
		if (jiraIssue != null) {
			instances.put(key, jiraIssue);
		}
		return jiraIssue;
	}

	static JiraIssue getOrCreate(Issue issue) {
		if (issue == null) {
			return null;
		}

		String key = issue.getKey();
		if (instances.containsKey(key)) {
			return instances.get(key);
		}

		JiraIssue jiraIssue = new JiraIssueImpl(issue);
		instances.put(issue.getKey(), jiraIssue);
		return jiraIssue;
	}

	/**
	 * Retrieves an existing JiraIssue instance or creates a new instance if there
	 * is no instance for the given key.
	 * 
	 * @param key
	 *            JIRA issue key.
	 * @return instance of JiraIssue or null, if the given key could not be resolved
	 *         to a JIRA issue instance.
	 */
	static JiraIssue getOrCreate(String key) {
		return getOrCreate(key, JiraClient.getOrCreate());
	}

	/**
	 * Returns the associated Issue object provided by the Java REST JIRA client.
	 * 
	 * @see Issue
	 * @return associated Issue object provided by the Java REST JIRA client.
	 */
	Issue getIssue();

	/**
	 * Returns the JIRA issue key as a String. Note that the key is different to the
	 * id of a JIRA issue.
	 * 
	 * @return JIRA issue key as a String.
	 */
	String getJiraIssueKey();

	/**
	 * Returns the URL to the JIRA issue on the JIRA server as a URI object.
	 * 
	 * @return URL to the JIRA issue on the JIRA server as a URI object.
	 */
	URI getUri();
}