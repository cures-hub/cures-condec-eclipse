package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.ui.history.IHistoryPage;
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
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;

/**
 * Contains static methods to jump from a node to the corresponding knowledge element
 *
 */
public class JumpToUtils {
	
	/**
     * Calls the correct jumpTo-method for the node.
     * @param node
     * 		the node to be jumped to
     */
	public static boolean jumpTo(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
    	if (node instanceof JiraIssueImpl) {
			return jumpToJiraIssue((JiraIssue) node);
		}
		else if (node instanceof GitCommit) {
			return jumpToGitCommit((GitCommit) node);
		}
		else if (node instanceof ChangedFile) {
			return jumpToChangedFile((ChangedFile) node);
		}
		else if (node instanceof CodeMethod) {
			return jumpToMethod((CodeMethod) node);
		}
		else if (node instanceof DecisionKnowledgeElement) {
			return jumpToDecisionKnowledgeElement((DecisionKnowledgeElement) node);
		}
    	
    	return false;
    }
	
	/**
	 * Opens a JIRA issue in the web browser.
	 * @return
	 * 		whether the issue was opened in the web browser without error.
	 */
	private static boolean jumpToJiraIssue(JiraIssue issue) {
		return OpenWebbrowser.openWebpage(issue);
	}
	
	/**
	 * Opens a git commit message in the history view.
	 * @return
	 * 		whether the view was opened without error.
	 */
	private static boolean jumpToGitCommit(GitCommit commit) {
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				RevCommit revCommit = commit.getRevCommit();
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IHistoryView view = (IHistoryView) page.showView(IHistoryView.VIEW_ID);
					IHistoryPage ipage = view.getHistoryPage();
					ipage.setInput(revCommit);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		return true;
	}
	
	/**
	 * Opens a changed file in an editor.
	 * @return
	 * 		whether the file was opened in the editor.
	 */
	private static boolean jumpToChangedFile(ChangedFile file) {
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				IPath ipath = file.getPath();
				for(int i = 0; i < ipath.segmentCount(); ++i) {
					if(ipath.segment(i).matches("src")) {
						ipath = ipath.removeFirstSegments(i-1);
						break;
					}
				}
				IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(ifile.getName());
				try {
					page.openEditor(new FileEditorInput(ifile), desc.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		return true;
	}
	
	/**
	 * Opens a method in an editor.
	 * @return
	 * 		whether the method was opened in the editor.
	 */
	private static boolean jumpToMethod(CodeMethod method) {
		ChangedFile javaClass = method.getJavaClass();
		jumpToChangedFile(javaClass);
		return true;
	}
	
	/**
	 * Opens the git commit message the decision knowledge element is from in the history view.
	 * @return
	 * 		whether the commit message was opened in the view.
	 */
	private static boolean jumpToDecisionKnowledgeElement(DecisionKnowledgeElement element) {
		if(element.getCommit() != null) {
			GitCommit commit = element.getCommit();
			jumpToGitCommit(commit);
		}
		return true;
	}

}
