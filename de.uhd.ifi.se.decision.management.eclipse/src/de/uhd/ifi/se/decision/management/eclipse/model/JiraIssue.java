package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

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
		if (key == null) {
			return null;
		}
		if (instances.containsKey(key)) {
			return instances.get(key);
		}

		Issue issue = jiraClient.getJiraIssue(key);
		if (issue != null) {
			return new JiraIssueImpl(issue);
		}
		return null;
	}

	Issue getJiraIssue();

	String getJiraIssueKey();
}