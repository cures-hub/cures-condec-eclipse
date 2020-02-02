package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;

public class TestGitCommit {

	private static GitClient gitClient;

	@BeforeClass
	public static void setUp() {
		gitClient = TestGitClient.initGitClient();
	}

	@Test
	public void testRevCommitNull() {
		GitCommit gitCommit = GitCommit.getOrCreate(null, "");
		assertNull(gitCommit);

		gitCommit = new GitCommitImpl(null);
		assertNull(gitCommit.getRevCommit());
		assertEquals("", gitCommit.toString());
	}

	@Test
	public void testGetJiraIssueKey() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		assertEquals("ECONDEC-1", commits.get(0).getJiraIssueKeys().iterator().next());
	}

	@Test
	public void testGetDecisionKnowledgeFromMessage() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		Set<DecisionKnowledgeElement> elements = commits.get(1).getDecisionKnowledgeFromMessage();
		assertEquals(5, elements.size());
		assertEquals("How to import dependencies for this plugin?", elements.iterator().next().getSummary());
	}

	@Test
	public void testGetChangedFiles() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		GitCommit commit = commits.get(0);
		assertEquals(5, commit.getChangedFiles().size());

		ChangedFile changedFile = commit.getChangedFiles().iterator().next();
		assertTrue(changedFile.getCommits().size() > 0);
	}

	@Test
	public void testToString() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		assertEquals("Commit: EConDec-1: Add classpath and project files", commits.get(0).toString());
	}
	
	@Test
	public void testGetNodeId() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		GitCommit commit = commits.get(0);
		
		assertTrue(commit.getNodeId().equals("commit 907be0618dbdf6640cac49e1ce52e2d349310c06 1559134638 -----p"));
	}

	@AfterClass
	public static void tearDown() {
		GitClient.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
	}
}
