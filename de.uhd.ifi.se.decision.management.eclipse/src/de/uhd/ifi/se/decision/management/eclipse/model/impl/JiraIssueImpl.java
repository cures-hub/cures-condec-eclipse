package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class JiraIssueImpl extends NodeImpl implements Node, JiraIssue {
	private static Map<Issue, JiraIssue> instances = new HashMap<Issue, JiraIssue>();
	private static Map<String, JiraIssue> instances_alternative = new HashMap<String, JiraIssue>();
	private Issue issue;
	private String issueKey;

	public static Set<JiraIssue> getInstances() {
		Set<JiraIssue> output = new HashSet<JiraIssue>();
		for (Map.Entry<Issue, JiraIssue> entry : instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	public static JiraIssue getOrCreate(Issue issue) {
		if (instances.containsKey(issue)) {
			return instances.get(issue);
		} else {
			return new JiraIssueImpl(issue);
		}
	}

	/**
	 * 
	 * @param issueKey
	 *            The IssueKey which should be resolved to a JiraIssue-instance.
	 * @param jiraManager
	 *            The JiraManager which might be needed to pull the issue from the
	 *            server and store it in a JiraIssue-object.
	 * @return Might be null, if the given IssueKey couldn't be resolved to a
	 *         JiraIssue-object.
	 */
	public static JiraIssue getOrCreate(String issueKey, JiraClient jiraManager) {
		if (issueKey == null) {
			return null;
		}
		if (instances_alternative.containsKey(issueKey)) {
			return instances_alternative.get(issueKey);
		} else {
			try {
				Issue pulled_issue = jiraManager.getIssue(issueKey);
				if (pulled_issue != null) {
					return new JiraIssueImpl(pulled_issue);
				} else {
					return null;
				}
			} catch (Exception e) {
				return null;
			}
		}
	}

	private JiraIssueImpl(Issue issue) {
		this.issue = issue;
		this.issueKey = this.issue.getKey();
		instances.put(this.issue, this);
		instances_alternative.put(this.issue.getKey().toLowerCase(), this);
	}

	@Override
	public Issue getJiraIssue() {
		return this.issue;
	}

	@Override
	public String getJiraIssueKey() {
		return this.issueKey;
	}

	@Override
	public String toString() {
		return this.issueKey + ":" + this.issue.getSummary();
	}
}
