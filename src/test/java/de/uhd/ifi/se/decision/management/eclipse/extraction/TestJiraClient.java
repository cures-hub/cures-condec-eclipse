package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Project;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class TestJiraClient {
	private static String jiraUrl = "https://jira-se.ifi.uni-heidelberg.de/";
	private static URI uri = null;
	private static String jiraProject = "FLAVORED";
	private static String username = "<ENTER YOUR USERNAME>";
	private static String password = "<ENTER YOUR PASSWORD IN PLAN TEXT>";
	private static JiraRestClient client = null;

	@Before
	public void setUp() {
		try {
			uri = new URI(jiraUrl);
			assertNotNull(uri);
			client = (new AsynchronousJiraRestClientFactory()).createWithBasicHttpAuthentication(uri, username,
					password);
			assertNotNull(client);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	@Ignore
	public void testLoadUserObject() {
		try {
			// Try to load user-object for the given username
			User user = client.getUserClient().getUser("ebrotzmann").claim();
			assertNotNull(user);
			System.out.println("E-Mail-Adress of " + user.getDisplayName() + ": " + user.getEmailAddress());
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	@Ignore
	public void testLoadProjectObject() {
		try {
			// Try to load project-object for the given project-name
			Project project = client.getProjectClient().getProject(jiraProject).claim();
			assertNotNull(project);
			System.out.println("Received following Project: " + project.getName());
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	@Ignore
	public void testLoadIssueObject() {
		try {
			// Try to get Issue
			Issue issue = client.getIssueClient().getIssue(jiraProject + "-22").claim();
			assertNotNull(issue);
			System.out.println("Received following Issue: \"" + issue.getSummary() + "\", assigned by "
					+ issue.getAssignee().getDisplayName());
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	@Ignore
	public void testObjectReferenceForPulledIssues() {
		try {
			// Pull issue...
			Issue issue = client.getIssueClient().getIssue(jiraProject + "-22").claim();
			assertNotNull(issue);
			// Pull same issue again...
			Issue issue2 = client.getIssueClient().getIssue(jiraProject + "-22").claim();
			assertNotNull(issue2);
			// Are both issues referenced to the same Java-Object?
			assertSame(issue, issue2);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	@Ignore
	public void testReceiveListOfAllExistingIssues() {
		try {
			// Try to get information about the current project
			SearchResult sr = client.getSearchClient().searchJql("project=\"" + jiraProject + "\"", -1, 0).claim();
			assertNotNull(sr);
			System.out.println("Received following SearchResult:");
			System.out.println("Total Results: " + sr.getTotal());
			System.out.println("Total MaxResults: " + sr.getMaxResults());
			System.out.println("Total StartIndex: " + sr.getStartIndex());

			// Try to get all Issues from the Server
			System.out.println("Existing issues in Jira-Project: ");
			for (BasicIssue bi : sr.getIssues()) {
				System.out.println(bi.getKey());
			}
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
}
