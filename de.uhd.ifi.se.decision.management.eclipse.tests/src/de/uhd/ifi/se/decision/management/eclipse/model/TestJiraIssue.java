package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public class TestJiraIssue {

	private JiraClient jiraClient;

	@Before
	public void setUp() {
		jiraClient = new MockJiraClient();
		JiraClient.instances.clear();
		JiraClient.instances.put(ConfigPersistenceManager.getJiraURI(), jiraClient);
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
		Issue newIssue = MockJiraClient.createIssue("WI: Add preference/settings page for Eclipse plugin", "ECONDEC-2");
		assertNotNull(JiraIssue.getOrCreate(newIssue));
	}
	
	@Test
	public void testToString() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals("ECONDEC-1:WI: Create empty Eclipse plugin", jiraIssue.toString());
	}

	@Test
	public void testGetOrCreateKeyValidNoJiraClient() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1");
		assertEquals(jiraClient, JiraClient.getOrCreate());
		assertEquals("ECONDEC-1", jiraIssue.getJiraIssueKey());
	}
	
	@Test
	public void testGetUri() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals("https://my-raspberry.pi/projects/ECONDEC/issues/ECONDEC-1", jiraIssue.getUri().toString());
	}

	@AfterClass
	public static void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
	}

}
