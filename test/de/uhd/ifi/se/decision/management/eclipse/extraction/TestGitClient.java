package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClientImpl;

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

		Set<RevCommit> commitSet = myExtractor.getCommitsForIssueKey("BMS-49");

		String messageGlob = "";

		Iterator<RevCommit> iter = commitSet.iterator();
		while (iter.hasNext()) {
			RevCommit commit = iter.next();
			messageGlob = messageGlob + commit.getFullMessage() + "\n";

		}

		assertThat(0, not(messageGlob.length()));

	}

	@Test
	public void testMessagesAndIDExist() {

		IPath repositoryPathMS = new Path("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\");

		GitClient myExtractor = new GitClientImpl(repositoryPathMS + ".git", "HEAD");

		Set<RevCommit> commitSet = myExtractor.getCommitsForIssueKey("BMS-52");

		String messageGlob = "";

		Iterator<RevCommit> iter = commitSet.iterator();
		while (iter.hasNext()) {
			RevCommit commit = iter.next();

			messageGlob = messageGlob + commit.getFullMessage() + commit.getId() + "\n";

		}

		assertThat(0, not(messageGlob.length()));

	}

}
