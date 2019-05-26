package de.uhd.ifi.se.decision.management.eclipse.view;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import de.uhd.ifi.se.decision.management.eclipse.persistence.CryptoHelper;

/**
 * Creates the plugins property page accessable via Project -> Properties ->
 * ConDec. The properties inherit from the global preferences but can be changed
 * by the user.
 * 
 * @issue Currently, only the preferences are used. The project properties don't
 *        have any effect. How can we access the project properties in the
 *        ConfigPersistenceManager class?
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

	public PropertyPage() {
		super();
	}

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
		textGitPath.setText(getProperty(ConfigPersistenceManager.PATH_TO_GIT));

		// Git branch/tag
		Label labelBranch = new Label(composite, SWT.NONE);
		labelBranch.setLayoutData(new GridData());
		labelBranch.setText("Branch/Tag of the Repository (e.g. master or HEAD):");
		textBranch = new Text(composite, SWT.BORDER);
		textBranch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textBranch.setText(getProperty(ConfigPersistenceManager.BRANCH));

		// Jira URL
		Label labelJiraUrl = new Label(composite, SWT.NONE);
		labelJiraUrl.setLayoutData(new GridData());
		labelJiraUrl.setText("JIRA URL:");
		textJiraUrl = new Text(composite, SWT.BORDER);
		textJiraUrl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraUrl.setText(getProperty(ConfigPersistenceManager.JIRA_URL));

		// Jira project key
		Label labelProjectKey = new Label(composite, SWT.NONE);
		labelProjectKey.setLayoutData(new GridData());
		labelProjectKey.setText("JIRA Project Key:");
		textJiraProjectKey = new Text(composite, SWT.BORDER);
		textJiraProjectKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraProjectKey.setText(getProperty(ConfigPersistenceManager.JIRA_PROJECT_KEY));

		// Jira username
		Label labelJiraUser = new Label(composite, SWT.NONE);
		labelJiraUser.setLayoutData(new GridData());
		labelJiraUser.setText("JIRA Username:");
		textJiraUser = new Text(composite, SWT.BORDER);
		textJiraUser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraUser.setText(getProperty(ConfigPersistenceManager.JIRA_USER));

		// Jira password
		Label labelPassword = new Label(composite, SWT.NONE);
		labelPassword.setLayoutData(new GridData());
		labelPassword.setText("Jira Password:");
		textJiraPassword = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		textJiraPassword.setEchoChar('*');
		textJiraPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textJiraPassword.setText(getPropertyDecrypted(ConfigPersistenceManager.JIRA_PASSWORD));

		// Link distance
		Label labelLinkDistance = new Label(composite, SWT.NONE);
		labelLinkDistance.setLayoutData(new GridData());
		labelLinkDistance.setText("Highlight-Distance (recommended: between 2 and 5):");
		textLinkDistance = new Text(composite, SWT.BORDER);
		textLinkDistance.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textLinkDistance.setText(getProperty(ConfigPersistenceManager.LINK_DISTANCE));

		// Decrease factor
		Label lbl4 = new Label(composite, SWT.NONE);
		lbl4.setLayoutData(new GridData());
		lbl4.setText("Highlight Decrease Factor (float, should be bigger than 1.3f):");
		textDecreaseFactor = new Text(composite, SWT.BORDER);
		textDecreaseFactor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textDecreaseFactor.setText(getProperty(ConfigPersistenceManager.DECREASE_FACTOR));

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
		setPropertyEncrypted(ConfigPersistenceManager.JIRA_PASSWORD, textJiraPassword);
		return super.performOk();
	}

	private void setProperty(QualifiedName key, Text text) {
		setProperty(key, text.getText());
	}

	private void setPropertyEncrypted(QualifiedName key, Text text) {
		setProperty(key, CryptoHelper.encrypt(text.getText()));
	}

	private void setProperty(QualifiedName key, String value) {
		if (value.equals("")) {
			return;
		}
		IResource resource = getResource();
		try {
			resource.setPersistentProperty(key, value);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public IResource getResource() {
		return getElement().getAdapter(IResource.class);
	}

	public String getProperty(QualifiedName key) {
		try {
			IResource resource = getResource();
			String property = resource.getPersistentProperty(key);
			if (property != null) {
				return property;
			}
			return ConfigPersistenceManager.getPreference(key);
		} catch (CoreException e) {
			System.err.println(e);
		}
		return "";
	}

	public String getPropertyDecrypted(QualifiedName key) {
		return CryptoHelper.decrypt(getProperty(key));
	}
}