package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.Issue;

public class TestJiraClient {
	
	JiraClient jiraClient;
	
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
		Map<Issue, Integer> linkedJiraIssuesAtDistance = jiraClient.getLinkedJiraIssues(null, 1);
		assertEquals(0, linkedJiraIssuesAtDistance.size());
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

}
