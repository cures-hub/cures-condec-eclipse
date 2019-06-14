package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;

public class TestGitClient {

	private GitClient gitClient;
	private IPath path;

	@Before
	public void setUp() {
		File file = new File(".");
		String canonicalPath = "";
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		path = new Path(canonicalPath);
		path = path.removeLastSegments(1);
		path = path.append(".git");

		gitClient = GitClient.getOrCreate(path, "HEAD", "ECONDEC");
	}

	@Test
	public void testGetOrCreate() {
		assertEquals(gitClient, GitClient.getOrCreate(path, "HEAD", "ECONDEC"));
	}

	@Test
	public void testSetReferenceNullOrEmpty() {
		GitClient gitClient = new GitClientImpl(path, null, "");
		assertNotNull(gitClient.getReference());

		gitClient = new GitClientImpl(path, "", "");
		assertNotNull(gitClient.getReference());
	}

	@Test
	public void testGetCommits() {
		Set<GitCommit> commits = gitClient.getCommits();
		assertTrue(commits.size() > 0);
	}

	@Test
	public void testGetCommitsForJiraIssue() {
		Set<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		assertTrue(commits.size() == 5);
	}

	@AfterClass
	public static void tearDown() {
		GitClient.instances.clear();
		GitCommit.instances.clear();
	}
}
