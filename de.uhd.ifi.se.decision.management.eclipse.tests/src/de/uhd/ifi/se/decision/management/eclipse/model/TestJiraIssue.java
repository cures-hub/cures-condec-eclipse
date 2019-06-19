package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockIssueRestClient;
import de.uhd.ifi.se.decision.management.eclipse.mock.MockJiraRestClient;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public class TestJiraIssue {

	private JiraClient jiraClient;

	@Before
	public void setUp() {
		jiraClient = new JiraClientImpl();
		jiraClient.setJiraRestClient(new MockJiraRestClient());
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
		Issue newIssue = MockIssueRestClient.createIssue("WI: Add preference/settings page for Eclipse plugin",
				"ECONDEC-2");
		assertNotNull(JiraIssue.getOrCreate(newIssue));
	}

	@Test
	public void testToString() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(jiraClient, JiraClient.getOrCreate());
		assertNotNull(jiraIssue);
		assertEquals("ECONDEC-1:WI: Create empty Eclipse plugin", jiraIssue.toString());
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
		IPath path = TestGitClient.initPathToGitRepo();
		GitClient gitClient = GitClient.getOrCreate(path, "HEAD", "ECONDEC");
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");

		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(commits.size(), jiraIssue.getCommits().size());
		assertEquals(5, commits.size());
	}

	@AfterClass
	public static void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
		GitClient.instances.clear();
	}

}
