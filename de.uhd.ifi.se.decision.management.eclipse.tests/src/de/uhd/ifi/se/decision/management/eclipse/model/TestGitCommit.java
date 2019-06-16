package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;

public class TestGitCommit {

	private GitClient gitClient;

	@Before
	public void setUp() {
		IPath path = TestGitClient.initPathToGitRepo();
		gitClient = GitClient.getOrCreate(path, "HEAD", "ECONDEC");
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
		assertEquals("ECONDEC-1", commits.get(0).getJiraIssueKeys().get(0));
	}

	@Test
	public void testGetDecisionKnowledgeFromMessage() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		List<DecisionKnowledgeElement> elements = commits.get(1).getDecisionKnowledgeFromMessage();
		assertEquals(5, elements.size());
		assertEquals("How to import dependencies for this plugin?", elements.get(0).getSummary());
	}

	@Test
	public void testGetChangedFiles() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		GitCommit commit = commits.get(0);

		List<ChangedFile> changedFiles = gitClient.getChangedFiles(commit);
		commit.setChangedFiles(changedFiles);

		assertEquals(5, commit.getChangedFiles().size());
	}

	@Test
	public void testToString() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		assertEquals("EConDec-1: Add classpath and project files", commits.get(0).toString());
	}

	@AfterClass
	public static void tearDown() {
		GitClient.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
	}
}
