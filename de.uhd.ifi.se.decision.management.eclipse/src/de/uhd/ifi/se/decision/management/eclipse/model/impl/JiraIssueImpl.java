package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class JiraIssueImpl extends NodeImpl implements Node, JiraIssue {
	private Issue issue;
	
	public JiraIssueImpl(Issue issue) {
		this.issue = issue;
		instances.put(issue.getKey(), this);
	}

	@Override
	public Issue getJiraIssue() {
		return this.issue;
	}

	@Override
	public String getJiraIssueKey() {
		return this.issue.getKey();
	}

	@Override
	public String toString() {
		return this.getJiraIssueKey() + ":" + this.issue.getSummary();
	}
}
