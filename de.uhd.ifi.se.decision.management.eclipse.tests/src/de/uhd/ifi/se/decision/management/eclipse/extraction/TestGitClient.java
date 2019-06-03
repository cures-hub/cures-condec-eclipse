package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;

public class TestGitClient {

	@Before
	public void setUp() {
		new GitClientImpl(".git", "HEAD");
		new File("");
	}

	@Test
	@Ignore
	public void testMessagesExists() {

		IPath repositoryPathMS = new Path("");

		GitClient myExtractor = new GitClientImpl(repositoryPathMS + ".git", "HEAD");

		Set<GitCommit> commitSet = myExtractor.getCommitsForIssueKey("BMS-49");

		String messageGlob = "";

		Iterator<GitCommit> iter = commitSet.iterator();
		while (iter.hasNext()) {
			GitCommit commit = iter.next();
			messageGlob = messageGlob + commit.toString() + "\n";

		}

		assertThat(0, not(messageGlob.length()));

	}

	@Test
	@Ignore
	public void testMessagesAndIDExist() {

		IPath repositoryPathMS = new Path("");

		GitClient myExtractor = new GitClientImpl(repositoryPathMS + ".git", "HEAD");

		Set<GitCommit> commitSet = myExtractor.getCommitsForIssueKey("BMS-52");

		String messageGlob = "";

		Iterator<GitCommit> iter = commitSet.iterator();
		while (iter.hasNext()) {
			GitCommit commit = iter.next();

			messageGlob = messageGlob + commit.toString() + commit.getId() + "\n";

		}

		assertThat(0, not(messageGlob.length()));

	}

}
