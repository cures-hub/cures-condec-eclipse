package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.IssueKey;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class JiraIssueImpl extends NodeImpl implements Node, JiraIssue {
	private static Map<Issue, JiraIssueImpl> instances = new HashMap<Issue, JiraIssueImpl>();
	private static Map<String, JiraIssueImpl> instances_alternative = new HashMap<String, JiraIssueImpl>();
	private Issue issue;
	private IssueKey issueKey;

	public static Set<JiraIssueImpl> getInstances() {
		Set<JiraIssueImpl> output = new HashSet<JiraIssueImpl>();
		for (Map.Entry<Issue, JiraIssueImpl> entry : instances.entrySet()) {
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
	public static JiraIssueImpl getOrCreate(IssueKey issueKey, JiraClient jiraManager) {
		if (issueKey == null) {
			return null;
		}
		String key = issueKey.getFullIssueKey().toLowerCase();
		if (instances_alternative.containsKey(key)) {
			return instances_alternative.get(key);
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
		this.issueKey = IssueKey.getOrCreate(this.issue.getKey());
		instances.put(this.issue, this);
		instances_alternative.put(this.issue.getKey().toLowerCase(), this);
	}

	@Override
	public Issue getJiraIssueIssue() {
		return this.issue;
	}

	@Override
	public IssueKey getJiraIssueKey() {
		return this.issueKey;
	}

	@Override
	public String toString() {
		return this.issueKey.getFullIssueKey() + ":" + this.issue.getSummary();
	}
}
