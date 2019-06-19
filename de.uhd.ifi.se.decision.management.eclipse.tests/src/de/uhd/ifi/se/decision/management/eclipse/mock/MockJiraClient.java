package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

public class MockJiraClient implements JiraClient {

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public boolean authenticate(URI jiraURI, String username, String password) {
		return true;
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public Set<JiraIssue> getAllJiraIssues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JiraIssue getJiraIssue(String jiraIssueKey) {
		Issue issue = createIssue("WI: Create empty Eclipse plugin", "ECONDEC-1");
		JiraIssue jiraIssue = new JiraIssueImpl(issue);
		return jiraIssue;
	}

	@Override
	public JiraRestClient getJiraRestClient() {
		return null;
	}

	@Override
	public List<String> getKeysOfNeighborJiraIssues(Issue jiraIssue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Issue, Integer> getLinkedJiraIssues(Issue issue, int distance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWorking() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static Issue createIssue(String summary, String key) {
		return new Issue(summary, URI.create("https://my-raspberry.pi/rest/" + key), key, null, new BasicProject(null, "ECONDEC", null, "Eclipse ConDec"), null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

}
