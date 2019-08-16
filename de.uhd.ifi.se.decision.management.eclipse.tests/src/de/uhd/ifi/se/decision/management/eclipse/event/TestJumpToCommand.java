package de.uhd.ifi.se.decision.management.eclipse.event;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;

public class TestJumpToCommand {
	
	private static JiraClient jiraClient;
	private static GitClient gitClient;
	
	@BeforeClass
	public static void setUp() {
		jiraClient = TestJiraClient.initJiraClient();
		gitClient = TestGitClient.initGitClient();
	}

	@Test
	public void testJumpToJiraIssue() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		try {
			boolean webbrowserOpened = JumpToCommand.jumpToJiraIssue(jiraIssue);
			assertTrue(webbrowserOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDown() {
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
		GitClient.instances.clear();
	}
}
