package de.uhd.ifi.se.decision.management.eclipse.example;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.blame.BlameResult;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;

/**
 * An example class for testing
 */
public class GitBlameExample {

	public static void main(String[] args) {

		IPath repositoryPath = new Path("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\");
		IPath filePath = new Path(
				"C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\implementation\\de.uhd.ifi.se.decdoc.eclipse\\pom.xml");

		GitClient gitClient = new GitClientImpl(repositoryPath + ".git", "HEAD");
		BlameResult blameResult = gitClient.getGitBlameForFile(filePath.makeRelativeTo(repositoryPath));

		int size = blameResult.getResultContents().size();
		for (int i = 0; i < size; i++) {
			System.out.print("Line " + i + ": ");
			System.out.print(blameResult.getSourceAuthor(i) + ": ");
			System.out.print(blameResult.getSourceCommit(i) + "\r\n");
		}

		String commitMessage = gitClient.getCommitMessageForLine(filePath.makeRelativeTo(repositoryPath), 5);
		System.out.println(commitMessage);

		System.out.println(GitClientImpl.getIssueKey(commitMessage));
	}
}
