package de.uhd.ifi.se.decision.management.eclipse.example;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;

/**
 * An example class for testing
 */
public class GitDiffExample {

	private static RevWalk revWalk;

	public static void main(String[] args) throws GitAPIException, IOException {

		IPath repositoryPath = new Path("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\");
		GitClient gitClient = new GitClientImpl(repositoryPath + ".git", "HEAD");
		Git git = gitClient.getGit();

		Repository repo = git.getRepository();
		revWalk = new RevWalk(repo);
		RevCommit commit = revWalk.parseCommit(repo.resolve("b5f82b0cb2878"));

		Map<DiffEntry, EditList> diffEntriesMappedToEditLists = gitClient.getDiffEntriesMappedToEditLists(commit);
		System.out.println(diffEntriesMappedToEditLists.toString());

		for (Map.Entry<DiffEntry, EditList> entry : diffEntriesMappedToEditLists.entrySet()) {
			System.out.println(entry.getKey().getNewPath());
			for (Edit edit : entry.getValue()) {
				System.out.println(edit.getType());
				System.out.println(edit.getEndA());
				
			}
		}
	}
}
