package de.uhd.ifi.se.decision.management.eclipse.example;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;

/**
 * An example class for testing
 */
public class GetCommitsByIssueIDExample {

	public static void main(String[] args) {

		IPath repositoryPath = new Path("C:\\Users\\anja\\Documents\\gits\\cures\\cures-condec-jira\\");
		GitClient gitClient = new GitClientImpl(repositoryPath + ".git", "HEAD");

		System.out.println("All commit messages will be shown now:");
		Set<RevCommit> commitSet = gitClient.getCommitsForIssueKey("ConDec-247");

		Iterator<RevCommit> iterator = commitSet.iterator();
		System.out.println(commitSet.size());
		while (iterator.hasNext()) {
			RevCommit commit = iterator.next();
			System.out.println(commit.getId());
			System.out.println(commit.getFullMessage());
		}

		System.out.println("All commit messages messages were shown!");
	}
}