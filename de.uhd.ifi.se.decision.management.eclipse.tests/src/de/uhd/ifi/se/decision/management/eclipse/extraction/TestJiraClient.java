package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Set;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockJiraRestClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public class TestJiraClient {

	private JiraClient jiraClient;

	@Before
	public void setUp() {
		jiraClient = initJiraClient();
	}

	public static JiraClient initJiraClient() {
		JiraClient jiraClient = new JiraClientImpl();
		jiraClient.setJiraRestClient(new MockJiraRestClient());
		JiraClient.instances.clear();
		JiraClient.instances.put(ConfigPersistenceManager.getJiraUri(), jiraClient);
		return jiraClient;
	}

	@Test
	public void testGetOrCreate() {
		JiraClient.instances.clear();
		JiraClient newJiraClient = JiraClient.getOrCreate();
		assertNotEquals(jiraClient, newJiraClient);

		JiraClient.instances.put(ConfigPersistenceManager.getJiraUri(), jiraClient);
		assertEquals(jiraClient, JiraClient.getOrCreate());
	}

	@Test
	public void testConstructorNotWorking() {
		JiraClient jiraClient = new JiraClientImpl(URI.create(""), "", "", "");
		assertFalse(jiraClient.isWorking());
	}

	@Test
	public void testGetAllJiraIssues() {
		assertEquals(2, jiraClient.getAllJiraIssues().size());
	}

	@Test
	public void testGetLinkedJiraIssues() {
		Set<JiraIssue> linkedJiraIssues = jiraClient.getLinkedJiraIssues(null);
		assertEquals(0, linkedJiraIssues.size());

		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		JiraIssue systemFunction = JiraIssue.getOrCreate("ECONDEC-5", jiraClient);

		linkedJiraIssues = jiraClient.getLinkedJiraIssues(workItem);
		assertFalse(linkedJiraIssues.isEmpty());
		assertEquals(systemFunction, linkedJiraIssues.iterator().next());
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

	@Test
	public void testCreateIssue() {
		JsonNodeFactory jnf = JsonNodeFactory.instance;
		ObjectNode payload = jnf.objectNode();
		{
			payload.put("projectKey", ConfigPersistenceManager.getProjectKey());
			payload.put("type", "DECISION");
			payload.put("summary", "Test");
			payload.put("description", "This is a test issue.");
			payload.put("documentationLocation", "i");
		}

		JiraClient jiraClient = JiraClient.getOrCreate();
		assertFalse(jiraClient.createIssue(payload));
	}

	@After
	public void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
	}
}
