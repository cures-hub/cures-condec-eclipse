package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;

public class JumpToCommand {
	
	/**
	 * Opens a JIRA issue in the web browser.
	 * @return
	 * 		whether the issue could be opened in the web browser without error.
	 */
	public static boolean jumpToJiraIssue(JiraIssue issue) {
		return OpenWebbrowser.openWebpage(issue);
	}
	
	/**
	 * Opens a git commit message in the history view.
	 * @return
	 * 		whether the view could be opened without error.
	 */
	public static boolean jumpToGitCommit(GitCommit commit) {
		RevCommit revCommit = commit.getRevCommit();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			IHistoryView view = (IHistoryView) page.showView(IHistoryView.VIEW_ID);
			view.showHistoryFor(revCommit);
			return true;
		} catch (PartInitException e) {
			e.printStackTrace();
		};
		return false;
	}
	
	/**
	 * Opens a changed file in an editor.
	 * @return
	 * 		whether the filed could be opened in the editor.
	 */
	public static boolean jumpToChangedFile(ChangedFile file) {
		IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(file.getPath());
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try{
			IDE.openEditor(page, ifile, true);
			return true;
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Opens a method in an editor.
	 * @return
	 * 		whether the method could be opened in the editor.
	 */
	public static boolean jumpToMethod(CodeMethod method) {
		ChangedFile javaClass = method.getJavaClass();
		return jumpToChangedFile(javaClass);
	}
	
	/**
	 * Opens the git commit message the decision knowledge element is from in the history view.
	 * @return
	 * 		whether the commit message could be opened in the view.
	 */
	public static boolean jumpToDecisionKnowledgeElement(DecisionKnowledgeElement element) {
		if(element.getCommit() != null) {
			GitCommit commit = element.getCommit();
			return jumpToGitCommit(commit);
		}
		return false;
	}

}
