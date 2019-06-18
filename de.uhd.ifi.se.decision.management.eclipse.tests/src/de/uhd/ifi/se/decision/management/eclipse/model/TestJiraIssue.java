package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockJiraIssue;

public class TestJiraIssue {

	JiraClient jiraClient;

	@Before
	public void setUp() {
		jiraClient = new MockJiraClient();
		JiraClient.instances.put(URI.create(""), jiraClient);
	}

	@Test
	public void testGetOrCreateKeyNull() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate(null, jiraClient);
		assertNull(jiraIssue);
	}

	@Test
	public void testGetOrCreateKeyEmpty() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("", jiraClient);
		assertNull(jiraIssue);
	}

	@Test
	public void testGetOrCreateKeyValid() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		Issue issue = jiraIssue.getIssue();
		assertEquals(jiraIssue, JiraIssue.getOrCreate(issue));
	}

	@Test
	public void testGetOrCreateIssue() {
		Issue newIssue = MockJiraIssue.createIssue("WI: Add preference/settings page for Eclipse plugin", "ECONDEC-2");
		JiraIssue.getOrCreate(newIssue);
	}

	@Test
	public void testGetOrCreateKeyValidNoJiraClient() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1");
		assertEquals(jiraClient, JiraClient.getOrCreate());
		assertEquals("ECONDEC-1", jiraIssue.getJiraIssueKey());
	}

	@AfterClass
	public static void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
	}

}
