package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;

public class TestGitClient {

	@Before
	public void setUp() {
		new GitClientImpl("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\.git", "HEAD");
		new File("");
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testMessagesExists() {

		IPath repositoryPathMS = new Path("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\");

		GitClient myExtractor = new GitClientImpl(repositoryPathMS + ".git", "HEAD");

		Set<GitCommit> commitSet = myExtractor.getCommitsForIssueKey("BMS-49");

		String messageGlob = "";

		Iterator<GitCommit> iter = commitSet.iterator();
		while (iter.hasNext()) {
			GitCommit commit = iter.next();
			messageGlob = messageGlob + commit.getBindedRevCommit().getFullMessage() + "\n";

		}

		assertThat(0, not(messageGlob.length()));

	}

	@Test
	public void testMessagesAndIDExist() {

		IPath repositoryPathMS = new Path("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\");

		GitClient myExtractor = new GitClientImpl(repositoryPathMS + ".git", "HEAD");

		Set<GitCommit> commitSet = myExtractor.getCommitsForIssueKey("BMS-52");

		String messageGlob = "";

		Iterator<GitCommit> iter = commitSet.iterator();
		while (iter.hasNext()) {
			GitCommit commit = iter.next();

			messageGlob = messageGlob + commit.getBindedRevCommit().getFullMessage() + commit.getId() + "\n";

		}

		assertThat(0, not(messageGlob.length()));

	}

}
