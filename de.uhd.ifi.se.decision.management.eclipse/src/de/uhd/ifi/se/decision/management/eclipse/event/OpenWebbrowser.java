package de.uhd.ifi.se.decision.management.eclipse.event;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public class OpenWebbrowser {
	
	public static boolean openWebpage(JiraIssue jiraIssue) {
		return openWebpage(jiraIssue.getUri());
	}

	public static boolean openWebpage(IPath path) {
		if (ConfigPersistenceManager.getJiraUri().toString().equals("") || 
				ConfigPersistenceManager.getProjectKey().toString().equals("")) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			MessageDialog.openError(window.getShell(), "Project Configuration misses important information", 
					"Please enter the Jira Server URL and the Jira Project Key in the Project Settings first.");
			return false;
		}
		return openWebpage(URI.create(ConfigPersistenceManager.getJiraUri().toString()
				+ "/projects/" + ConfigPersistenceManager.getProjectKey()
				+ "?selectedItem=decision-knowledge-page&codeFileName="
				+ path.lastSegment()));
	}

	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}	
	
	public static boolean openWebpage(URL url) {
		try {
			return openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}
}
