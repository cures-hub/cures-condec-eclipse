package de.uhd.ifi.se.decision.management.eclipse.persistence;

import java.net.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.uhd.ifi.se.decision.management.eclipse.Activator;

/**
 * Manages the persistence of the preferences and properties.
 */
public class ConfigPersistenceManager extends AbstractPreferenceInitializer {

	public static QualifiedName PATH_TO_GIT = new QualifiedName("PATH_TO_GIT", "PATH_TO_GIT");
	public static QualifiedName BRANCH = new QualifiedName("BRANCH", "BRANCH");
	public static QualifiedName LINK_DISTANCE = new QualifiedName("LINK_DISTANCE", "LINK_DISTANCE");
	public static QualifiedName DECREASE_FACTOR = new QualifiedName("DECREASE_FACTOR", "DECREASE_FACTOR");
	public static QualifiedName JIRA_URL = new QualifiedName("JIRA_URL", "JIRA_URL");
	public static QualifiedName JIRA_PROJECT_KEY = new QualifiedName("JIRA_PROJECT_KEY", "JIRA_PROJECT_KEY");
	public static QualifiedName JIRA_USER = new QualifiedName("JIRA_USER", "JIRA_USER");
	public static QualifiedName JIRA_PASSWORD = new QualifiedName("JIRA_PASSWORD", "JIRA_PASSWORD");

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault("PATH_TO_GIT", new Path(".git").toString());
		store.setDefault("BRANCH", "HEAD");

		store.setDefault("JIRA_URL", "http://jira-se.ifi.uni-heidelberg.de");
		store.setDefault("JIRA_USER", "");
		store.setDefault("JIRA_PASSWORD", "");
		store.setDefault("JIRA_PROJECT_KEY", "ConDec");

		store.setDefault("LINK_DISTANCE", 4);
		store.setDefault("DECREASE_FACTOR", "1.5");
	}

	public static IPath getPathToGit() {
		return new Path(getPreference(PATH_TO_GIT, ""));
	}

	public static String getBranch() {
		return getPreference(BRANCH, "HEAD");
	}

	public static URI getJiraURI() {
		return URI.create(getPreference(JIRA_URL, ""));
	}

	public static String getJiraUser() {
		return getPreference(JIRA_USER, "");
	}

	public static String getJiraPassword() {
		return getPreference(JIRA_PASSWORD, "");
	}

	public static String getProjectKey() {
		return getPreference(JIRA_PROJECT_KEY, "");
	}

	public static int getLinkDistance() {
		return Activator.getDefault().getPreferenceStore().getInt("LINK_DISTANCE");
	}

	public static float getDecreaseFactor() {
		return Float.parseFloat(getPreference(DECREASE_FACTOR, "1.1"));
	}

	public static String getPreference(QualifiedName key, String defaultValue) {
		String preference = defaultValue;
		try {
			preference = Activator.getDefault().getPreferenceStore().getString(key.getQualifier());
		} catch (NullPointerException e) {
			System.err.print(e);
		}
		return preference;
	}
}