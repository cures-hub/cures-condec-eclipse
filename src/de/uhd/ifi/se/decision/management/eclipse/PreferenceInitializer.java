package de.uhd.ifi.se.decision.management.eclipse;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initializes the plug-ins' preferences
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault("PATH_TO_GIT", new Path("\\.git").toString());
		store.setDefault("BRANCH", "HEAD");

		store.setDefault("JIRA_URL", "http://jira-se.ifi.uni-heidelberg.de");
		store.setDefault("JIRA_USER", "");
		store.setDefault("JIRA_PASSWORD", "");
		store.setDefault("JIRA_PROJECT_KEY", "ConDec");

		store.setDefault("LINK_DISTANCE", 4);
	}

	public static IPath getPathToGit() {
		return new Path(Activator.getDefault().getPreferenceStore().getString("PATH_TO_GIT"));
	}

	public static String getBranch() {
		return Activator.getDefault().getPreferenceStore().getString("BRANCH");
	}

	public static String getJiraUrl() {
		return Activator.getDefault().getPreferenceStore().getString("JIRA_URL");
	}

	public static String getJiraUser() {
		return Activator.getDefault().getPreferenceStore().getString("JIRA_USER");
	}

	public static String getJiraPassword() {
		return Activator.getDefault().getPreferenceStore().getString("JIRA_PASSWORD");
	}

	public static String getProjectKey() {
		return Activator.getDefault().getPreferenceStore().getString("JIRA_PROJECT_KEY");
	}

	public static int getLinkDistance() {
		return Activator.getDefault().getPreferenceStore().getInt("LINK_DISTANCE");
	}

}