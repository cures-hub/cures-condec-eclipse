package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.net.URI;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for JIRA issue as part of the knowledge graph.
 */
public class JiraIssueImpl extends NodeImpl implements Node, JiraIssue {
	private Issue issue;

	public JiraIssueImpl(Issue issue) {
		this.issue = issue;
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

	@Override
	public URI getUri() {
		String restUri = issue.getSelf().toString();
		String jiraIssueUri = "";
		for (String uriPart : restUri.split("/")) {
			if ("rest".equals(uriPart)) {
				jiraIssueUri += "projects/" + issue.getProject().getKey() + "/issues/" + issue.getKey();
				break;
			}
			jiraIssueUri += uriPart + "/";
		}
		return URI.create(jiraIssueUri);
	}
}
