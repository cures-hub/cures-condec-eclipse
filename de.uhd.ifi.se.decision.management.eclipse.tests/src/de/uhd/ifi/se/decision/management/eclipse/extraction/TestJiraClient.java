package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;

public class TestJiraClient {

	private JiraClient jiraClient;

	@Before
	public void setUp() {
		jiraClient = JiraClient.getOrCreate();
	}

	@Test
	public void testGetOrCreate() {
		assertEquals(jiraClient, JiraClient.getOrCreate());
	}

	@Test
	public void testGetLinkedJiraIssues() {
		Set<JiraIssue> linkedJiraIssues = jiraClient.getLinkedJiraIssues(null);
		assertEquals(0, linkedJiraIssues.size());
	}

	@Test
	public void testClose() {
		assertTrue(jiraClient.close());
	}

	@Test
	public void testGetJiraRestClient() {
		assertNotNull(jiraClient.getJiraRestClient());
	}

	@Test
	public void testIsWorking() {
		assertFalse(jiraClient.isWorking());
	}

	@AfterClass
	public static void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
	}
}
