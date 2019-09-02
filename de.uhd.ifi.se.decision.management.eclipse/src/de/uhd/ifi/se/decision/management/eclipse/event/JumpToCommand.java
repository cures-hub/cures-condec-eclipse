package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;

public class JumpToCommand {
	
	/**
	 * Opens a JIRA issue in the web browser.
	 * @return
	 * 		whether the issue was opened in the web browser without error.
	 */
	public static boolean jumpToJiraIssue(JiraIssue issue) {
		return OpenWebbrowser.openWebpage(issue);
	}
	
	/**
	 * Opens a git commit message in the history view.
	 * @return
	 * 		whether the view was opened without error.
	 */
	public static void jumpToGitCommit(GitCommit commit) {
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				ObjectId commitID = commit.getRevCommit().getId();
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IHistoryView view = (IHistoryView) page.showView(IHistoryView.VIEW_ID);
					view.showHistoryFor(commitID);
				} catch (PartInitException e) {
					e.printStackTrace();
				};
			}
		});
	}
	
	/**
	 * Opens a changed file in an editor.
	 * @return
	 * 		whether the filed was opened in the editor.
	 */
	public static void jumpToChangedFile(ChangedFile file) {
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(file.getPath());
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(ifile.getName());
				
				try {
					page.openEditor(new FileEditorInput(ifile), desc.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Opens a method in an editor.
	 * @return
	 * 		whether the method was opened in the editor.
	 */
	public static void jumpToMethod(CodeMethod method) {
		ChangedFile javaClass = method.getJavaClass();
		jumpToChangedFile(javaClass);
	}
	
	/**
	 * Opens the git commit message the decision knowledge element is from in the history view.
	 * @return
	 * 		whether the commit message was opened in the view.
	 */
	public static void jumpToDecisionKnowledgeElement(DecisionKnowledgeElement element) {
		if(element.getCommit() != null) {
			GitCommit commit = element.getCommit();
			jumpToGitCommit(commit);
		}
	}

}
