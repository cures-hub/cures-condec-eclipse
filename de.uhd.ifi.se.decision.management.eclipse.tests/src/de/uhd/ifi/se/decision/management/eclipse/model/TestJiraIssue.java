package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockIssueRestClient;

public class TestJiraIssue {

	private JiraClient jiraClient;
	private GitClient gitClient;

	@Before
	public void setUp() {
		jiraClient = TestJiraClient.initJiraClient();
		gitClient = TestGitClient.initGitClient();
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
	public void testGetOrCreateKeyWrong() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-0", jiraClient);
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
		Issue newIssue = MockIssueRestClient.createIssue("WI: Add preference/settings page for Eclipse plugin",
				"ECONDEC-2", null);
		assertNotNull(JiraIssue.getOrCreate(newIssue));
	}

	@Test
	public void testToString() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(jiraClient, JiraClient.getOrCreate());
		assertNotNull(jiraIssue);
		assertEquals("JIRA Issue ECONDEC-1: WI: Create empty Eclipse plugin", jiraIssue.toString());
	}

	@Test
	public void testGetOrCreateKeyValidNoJiraClient() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1");
		assertNotNull(jiraIssue);
		assertEquals("ECONDEC-1", jiraIssue.getJiraIssueKey());
	}

	@Test
	public void testGetUri() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals("https://my-raspberry.pi/projects/ECONDEC/issues/ECONDEC-1", jiraIssue.getUri().toString());
	}

	@Test
	public void testGetCommits() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");

		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(commits.size(), jiraIssue.getCommits().size());
		assertEquals(5, commits.size());
	}

	@Test
	public void testGetKeysOfLinkedJiraIssues() {
		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(1, workItem.getKeysOfLinkedJiraIssues().size());

		JiraIssue systemFunction = JiraIssue.getOrCreate("ECONDEC-5", jiraClient);
		assertEquals(1, systemFunction.getKeysOfLinkedJiraIssues().size());
	}

	@Test
	public void testGetLinkedJiraIssues() {
		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(1, workItem.getLinkedJiraIssues().size());
	}

	@Test
	public void testGetLinkedNodes() {
		gitClient.getCommitsForJiraIssue("ECONDEC-1");

		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(6, workItem.getLinkedNodes().size());
	}

	@Test
	public void testGetNodeId() {
		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);

		assertTrue(workItem.getNodeId().equals("ECONDEC-1"));
	}

	@After
	public void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
		GitClient.instances.clear();
	}
}
