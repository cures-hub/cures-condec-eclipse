package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
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
	 * Opens a JIRA issue in the web browser
	 */
	public static void jumpToJiraIssue(JiraIssue issue) {
		OpenWebbrowser.openWebpage(issue);
	}
	
	/**
	 * Opens a git commit message in the commit viewer
	 */
	public static void jumpToGitCommit(GitCommit commit) {
		
	}
	
	/**
	 * Opens a changed file in an editor
	 */
	public static void jumpToChangedFile(ChangedFile file) {
		IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(file.getPath());
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try{
			IDE.openEditor(page, ifile, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens a method in an editor
	 */
	public static void jumpToMethod(CodeMethod method) {
		
	}
	
	/**
	 * Opens a decision knowledge element
	 */
	public static void jumpToDecisionKnowledgeElement(DecisionKnowledgeElement element) {
		
	}

}
