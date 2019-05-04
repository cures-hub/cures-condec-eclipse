package de.uhd.ifi.se.decision.management.eclipse.example;

import java.util.Map;

import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;

/**
 * An example class for testing
 */
public class GetJiraIssueExample {

	public static void main(String[] args) {
		JiraClientImpl jiraClient = new JiraClientImpl();

		jiraClient.authenticate("http://jira-se.ifi.uni-heidelberg.de", "akleebaum", "D3c1s0N");

		Issue issue = jiraClient.getIssue("ConDec-195");
		System.out.println(issue.getKey());
		System.out.println(issue.getSummary());
		System.out.println(issue.getIssueLinks());

		int linkDistance = 4;

		Map<Issue, Integer> linkedIssuesAtDistance = jiraClient.getLinkedIssues(issue, linkDistance);

		for (Map.Entry<Issue, Integer> linkedIssueAtDistance : linkedIssuesAtDistance.entrySet()) {
			System.out.println(linkedIssueAtDistance.getKey().getKey());
			System.out.println(linkedIssueAtDistance.getValue());
		}
		
		jiraClient.close();
	}
}
