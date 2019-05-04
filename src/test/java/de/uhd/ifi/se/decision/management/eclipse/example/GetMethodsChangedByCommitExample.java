package de.uhd.ifi.se.decision.management.eclipse.example;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;

public class GetMethodsChangedByCommitExample {

	public static void main(String[] args) throws IOException {
		
		IPath repositoryPath = new Path("C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\");

		GitClient gitClient = new GitClientImpl(repositoryPath + ".git", "refs/heads/anja");
		
		Repository repo = gitClient.getRepository();
		RevWalk revWalk = new RevWalk(repo);
		RevCommit commit = revWalk.parseCommit(repo.resolve("HEAD"));
		revWalk.close();

		Map<DiffEntry, EditList> diffEntriesMappedToEditLists = gitClient
				.getDiffEntriesMappedToEditLists(commit);
		
		for (Map.Entry<DiffEntry, EditList> entry : diffEntriesMappedToEditLists.entrySet()) {
			System.out.println(entry.getKey().getNewPath());
			String changedMethods = gitClient.whichMethodsChanged(entry.getKey(), entry.getValue());
			System.out.println(changedMethods);
		}
	}
}
