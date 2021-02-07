package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public class TestGitClient {

	private GitClient gitClient;
	private IPath path;

	@Before
	public void setUp() {
		path = initPathToGitRepo();
		gitClient = initGitClient(path);
	}

	public static GitClient initGitClient() {
		return initGitClient(TestGitClient.initPathToGitRepo());
	}

	public static GitClient initGitClient(IPath path) {
		GitClient gitClient = GitClient.getOrCreate(path, "HEAD", "ECONDEC");
		GitClient.instances.clear();
		GitClient.instances.put(ConfigPersistenceManager.getPathToGit(), gitClient);
		return gitClient;
	}

	public static IPath initPathToGitRepo() {
		File file = new File(".");
		String canonicalPath = "";
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IPath path = new Path(canonicalPath);
		path = path.removeLastSegments(1);
		path = path.append(".git");
		System.out.println(path);
		return path;
	}

	@Test
	public void testGetGit() {
		assertNotNull(gitClient.getGit());
	}

	@Test
	public void testGetRepository() {
		assertNotNull(gitClient.getRepository());
	}

	@Test
	public void testSetReferenceNullOrEmpty() {
		GitClient gitClient = new GitClientImpl(path, null, "");
		assertNotNull(gitClient.getReference());

		gitClient = new GitClientImpl(path, "", "");
		assertNotNull(gitClient.getReference());
		gitClient.setReference("Evidently, this is not a valid git tag.");

		gitClient = new GitClientImpl(new Path(""), "", "");
		assertEquals("HEAD", gitClient.getReference());
	}

	@Test
	public void testGetCommits() {
		List<GitCommit> commits = gitClient.getCommits();
		assertTrue(commits.size() > 0);
	}

	@Test
	public void testGetCommitsForJiraIssue() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		assertTrue(commits.size() == 5);
	}

	@Test
	public void testGetCommitForLine() {
		GitCommit commit = gitClient.getCommitForLine(null, 1);
		assertNull(commit);

		commit = gitClient.getCommitForLine(new Path("pom.xml"), 1);
		assertNotNull(commit);
	}

	@Test
	public void testGetCommitMessageForLine() {
		String commitMessage = gitClient.getCommitMessageForLine(new Path("pom.xml"), 1);
		assertTrue(commitMessage.toUpperCase(Locale.ENGLISH).startsWith("ECONDEC"));
	}

	@Test
	public void testGetParentCommit() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		GitCommit parentCommit = gitClient.getParent(commits.get(3));
		assertEquals(commits.get(4), parentCommit);
	}

	@Test
	public void testGitClientCreationWithSettings() {
		assertNotNull(new GitClientImpl());
		assertNotNull(GitClient.getOrCreate());
	}

	@Test
	public void testGetDiff() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		assertNotNull(gitClient.getDiff(commits.get(0)));
	}

	@Test
	public void testGetChangedFiles() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		Set<ChangedFile> changedFiles = gitClient.getChangedFiles(commits.get(0));
		assertEquals(5, changedFiles.size());
	}

	@After
	public void tearDown() {
		GitClient.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
	}
}
