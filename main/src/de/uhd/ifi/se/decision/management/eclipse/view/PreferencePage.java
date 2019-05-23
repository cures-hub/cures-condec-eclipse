package de.uhd.ifi.se.decision.management.eclipse.view;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.uhd.ifi.se.decision.management.eclipse.Activator;

/**
 * Creates the plugins preference page accessable via Window -> Preferences ->
 * ConDec.
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);
	}

	@Override
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor("PATH_TO_GIT", "Path to Git Directory:", getFieldEditorParent()));
		addField(new StringFieldEditor("BRANCH", "Branch:", getFieldEditorParent()));

		addField(new StringFieldEditor("JIRA_URL", "URL to JIRA Server:", getFieldEditorParent()));
		addField(new StringFieldEditor("JIRA_USER", "Username:", getFieldEditorParent()));
		StringFieldEditor passwordField = new StringFieldEditor("JIRA_PASSWORD", "Password:", getFieldEditorParent());
		passwordField.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(passwordField);
		addField(new StringFieldEditor("JIRA_PROJECT_KEY", "Project Key in JIRA:", getFieldEditorParent()));

		addField(new IntegerFieldEditor("LINK_DISTANCE", "Link Distance:", getFieldEditorParent()));
		addField(new StringFieldEditor("DECREASE_FACTOR", "Highlight Decrease Factor:", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Please provide the plugin with the necessary information!");
	}
}