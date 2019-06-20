package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.io.IOException;

import com.atlassian.jira.rest.client.api.AuditRestClient;
import com.atlassian.jira.rest.client.api.ComponentRestClient;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.MetadataRestClient;
import com.atlassian.jira.rest.client.api.MyPermissionsRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.ProjectRolesRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.SessionRestClient;
import com.atlassian.jira.rest.client.api.UserRestClient;
import com.atlassian.jira.rest.client.api.VersionRestClient;

public class MockJiraRestClient implements JiraRestClient {

	@Override
	public void close() throws IOException {
		// Not used.
	}

	@Override
	public AuditRestClient getAuditRestClient() {
		// Not used.
		return null;
	}

	@Override
	public ComponentRestClient getComponentClient() {
		// Not used.
		return null;
	}

	@Override
	public IssueRestClient getIssueClient() {
		return new MockIssueRestClient();
	}

	@Override
	public MetadataRestClient getMetadataClient() {
		// Not used.
		return null;
	}

	@Override
	public MyPermissionsRestClient getMyPermissionsRestClient() {
		// Not used.
		return null;
	}

	@Override
	public ProjectRestClient getProjectClient() {
		// Not used.
		return null;
	}

	@Override
	public ProjectRolesRestClient getProjectRolesRestClient() {
		// Not used.
		return null;
	}

	@Override
	public SearchRestClient getSearchClient() {
		return new MockSearchRestClient();
	}

	@Override
	public SessionRestClient getSessionClient() {
		// Not used.
		return null;
	}

	@Override
	public UserRestClient getUserClient() {
		// Not used.
		return null;
	}

	@Override
	public VersionRestClient getVersionRestClient() {
		// Not used.
		return null;
	}
}
