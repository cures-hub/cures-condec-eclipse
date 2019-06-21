package de.uhd.ifi.se.decision.management.eclipse.view;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;

import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Creates the plugins property page accessable via Project -> Properties ->
 * ConDec. The properties inherit from the global preferences. Changes to the
 * properties also affect the preferences.
 * 
 * @see ConfigPersistenceManager
 * @see PreferencePage
 * 
 * @issue Currently, only the preferences are used. The project properties are
 *        treated the same as the preferences. How can we access the project
 *        properties in the ConfigPersistenceManager class?
 */
public class PropertyPage extends org.eclipse.ui.dialogs.PropertyPage implements IWorkbenchPropertyPage {
	private Text textGitPath;
	private Text textBranch;
	private Text textLinkDistance;
	private Text textDecreaseFactor;
	private Text textJiraUrl;
	private Text textJiraProjectKey;
	private Text textJiraUser;
	private Text textJiraPassword;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		composite.setLayout(gridLayout);

		// Git path
		Label labelGitPath = new Label(composite, SWT.NONE);
		labelGitPath.setLayoutData(new GridData());
		labelGitPath.setText("Path to Git-Repository (Path must end with \"\\.git\"):");
		textGitPath = new Text(composite, SWT.BORDER);
		textGitPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textGitPath.setText(ConfigPersistenceManager.getPathToGit().toString());

		// Git branch/tag
		Label labelBranch = new Label(composite, SWT.NONE);
		labelBranch.setLayoutData(new GridData());
		labelBranch.setText("Branch/Tag of the Repository (e.g. master or HEAD):");
		textBranch = new Text(composite, SWT.BORDER);
		textBranch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textBranch.setText(ConfigPersistenceManager.getBranch());

		// Jira URL
		Label labelJiraUrl = new Label(composite, SWT.NONE);
		labelJiraUrl.setLayoutData(new GridData());
		labelJiraUrl.setText("JIRA URL:");
		textJiraUrl = new Text(composite, SWT.BORDER);
		textJiraUrl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraUrl.setText(ConfigPersistenceManager.getJiraUri().toString());

		// Jira project key
		Label labelProjectKey = new Label(composite, SWT.NONE);
		labelProjectKey.setLayoutData(new GridData());
		labelProjectKey.setText("JIRA Project Key:");
		textJiraProjectKey = new Text(composite, SWT.BORDER);
		textJiraProjectKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraProjectKey.setText(ConfigPersistenceManager.getProjectKey());

		// Jira username
		Label labelJiraUser = new Label(composite, SWT.NONE);
		labelJiraUser.setLayoutData(new GridData());
		labelJiraUser.setText("JIRA Username:");
		textJiraUser = new Text(composite, SWT.BORDER);
		textJiraUser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraUser.setText(ConfigPersistenceManager.getJiraUser());

		// Jira password
		Label labelPassword = new Label(composite, SWT.NONE);
		labelPassword.setLayoutData(new GridData());
		labelPassword.setText("Jira Password:");
		textJiraPassword = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		textJiraPassword.setEchoChar('*');
		textJiraPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraPassword.setText(ConfigPersistenceManager.getJiraPassword());

		// Link distance
		Label labelLinkDistance = new Label(composite, SWT.NONE);
		labelLinkDistance.setLayoutData(new GridData());
		labelLinkDistance.setText("Highlight-Distance (recommended: between 2 and 5):");
		textLinkDistance = new Text(composite, SWT.BORDER);
		textLinkDistance.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textLinkDistance.setText(ConfigPersistenceManager.getLinkDistance() + "");

		// Decrease factor
		Label lbl4 = new Label(composite, SWT.NONE);
		lbl4.setLayoutData(new GridData());
		lbl4.setText("Highlight Decrease Factor (float, should be bigger than 1.3f):");
		textDecreaseFactor = new Text(composite, SWT.BORDER);
		textDecreaseFactor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textDecreaseFactor.setText(ConfigPersistenceManager.getDecreaseFactor() + "");

		return composite;
	}

	@Override
	public boolean performOk() {
		setProperty(ConfigPersistenceManager.PATH_TO_GIT, textGitPath);
		setProperty(ConfigPersistenceManager.BRANCH, textBranch);
		setProperty(ConfigPersistenceManager.LINK_DISTANCE, textLinkDistance);
		setProperty(ConfigPersistenceManager.DECREASE_FACTOR, textDecreaseFactor);
		setProperty(ConfigPersistenceManager.JIRA_URL, textJiraUrl);
		setProperty(ConfigPersistenceManager.JIRA_PROJECT_KEY, textJiraProjectKey);
		setProperty(ConfigPersistenceManager.JIRA_USER, textJiraUser);
		setProperty(ConfigPersistenceManager.JIRA_PASSWORD, textJiraPassword);
		return super.performOk();
	}

	private void setProperty(QualifiedName key, Text text) {
		setProperty(key, text.getText());
	}

	private void setProperty(QualifiedName key, String value) {
		ConfigPersistenceManager.setPreference(key, value);
	}
}