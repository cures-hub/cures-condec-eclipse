package de.uhd.ifi.se.decision.management.eclipse.model;

import com.atlassian.jira.rest.client.domain.Issue;

public interface JiraIssue extends Node {

	Issue getJiraIssue();

	String getJiraIssueKey();
}