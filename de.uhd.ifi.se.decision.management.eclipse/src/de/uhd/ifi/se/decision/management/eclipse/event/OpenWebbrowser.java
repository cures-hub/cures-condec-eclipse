package de.uhd.ifi.se.decision.management.eclipse.event;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;

import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

public class OpenWebbrowser {
	
	public static boolean openWebpage(JiraIssue jiraIssue) {
		return openWebpage(jiraIssue.getUri());
	}

	public static boolean openWebpage(IPath path) {
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
