package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Class to connect to a JIRA project associated with this Eclipse project.
 * Retrieves JIRA issues.
 */
public class JiraClientImpl implements JiraClient {

	private JiraRestClient jiraRestClient;
	private boolean isWorking;

	public JiraClientImpl() {
		boolean isValidUser = this.authenticate();
		boolean isValidProject = isValidProject();
		this.isWorking = isValidUser && isValidProject;
	}

	@Override
	public boolean authenticate() {
		return authenticate(ConfigPersistenceManager.getJiraURI(), ConfigPersistenceManager.getJiraUser(),
				ConfigPersistenceManager.getJiraPassword());
	}

	@Override
	public boolean authenticate(URI jiraURI, String username, String password) {
		this.jiraRestClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(jiraURI,
				username, password);
		return isValidUser(username);
	}

	private boolean isValidUser(String username) {
		boolean isValidUser = true;
		try {
			this.getJiraRestClient().getUserClient().getUser(username).get().getSelf();
		} catch (ExecutionException | InterruptedException e) {
			isValidUser = false;
			System.err.println("The JIRA username seems to be wrong. Message: " + e.getMessage());
		}

		try {
			this.getJiraRestClient().getSessionClient().getCurrentSession().get().getUserUri().getPath();
		} catch (Exception e) {
			isValidUser = false;
			System.err.println("There is a problem with establishing the session. Message: " + e.getMessage());
		}
		return isValidUser;
	}

	private boolean isValidProject() {
		return isValidProject(ConfigPersistenceManager.getProjectKey());
	}

	private boolean isValidProject(String projectKey) {
		boolean isValidProject = true;
		try {
			this.getJiraRestClient().getProjectClient().getProject(projectKey);
		} catch (Exception e) {
			isValidProject = false;
			System.err.println("The JIRA project is unknown. Message: " + e.getMessage());
		}
		return isValidProject;
	}

	@Override
	public Set<JiraIssue> getAllJiraIssues() {
		Set<JiraIssue> jiraIssues = new HashSet<JiraIssue>();
		for (BasicIssue jiraIssue : this.getJiraRestClient().getSearchClient()
				.searchJql("project=\"" + ConfigPersistenceManager.getProjectKey() + "\"", -1, 0, null).claim()
				.getIssues()) {
			jiraIssues.add(JiraIssue.getOrCreate(jiraIssue.getKey(), this));
		}
		return jiraIssues;
	}

	@Override
	public Issue getJiraIssue(String jiraIssueKey) {
		Issue jiraIssue = null;
		try {
			jiraIssue = this.getJiraRestClient().getIssueClient().getIssue(jiraIssueKey).get();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println(jiraIssueKey + ": " + e.getMessage());
		}
		return jiraIssue;
	}

	@Override
	public Map<Issue, Integer> getLinkedIssues(Issue jiraIssue, int distance) {
		Map<Issue, Integer> linkedIssuesAtDistance = new HashMap<Issue, Integer>();

		if (jiraIssue == null || distance == 0) {
			return linkedIssuesAtDistance;
		}

		List<String> analyzedIssueKeys = new ArrayList<String>();
		analyzedIssueKeys.add(jiraIssue.getKey());
		for (int i = 1; i <= distance; i++) {
			List<String> neighborIssueKeys = getKeysOfNeighborJiraIssues(jiraIssue);
			for (String issueKey : neighborIssueKeys) {
				if (!analyzedIssueKeys.contains(issueKey)) {
					analyzedIssueKeys.add(issueKey);
					Issue linkedJiraIssue = this.getJiraIssue(issueKey);
					linkedIssuesAtDistance.put(linkedJiraIssue, i);
				}
			}
		}
		return linkedIssuesAtDistance;
	}

	@Override
	public List<String> getKeysOfNeighborJiraIssues(Issue jiraIssue) {
		List<String> neighborIssueKeys = new ArrayList<String>();

		if (jiraIssue == null) {
			return neighborIssueKeys;
		}

		Iterable<IssueLink> issueLinkIterable = jiraIssue.getIssueLinks();
		Iterator<IssueLink> issueLinkIterator = issueLinkIterable.iterator();

		while (issueLinkIterator.hasNext()) {
			IssueLink link = issueLinkIterator.next();

			String neighborIssueKey = link.getTargetIssueKey();
			neighborIssueKeys.add(neighborIssueKey);
		}

		return neighborIssueKeys;
	}

	@Override
	public void close() {
		try {
			this.jiraRestClient.close();
		} catch (IOException e) {
			System.err.println("JIRA REST client could not be closed. Message: " + e.getMessage());
		}
	}

	@Override
	public void setJiraRestClient(JiraRestClient jiraRestClient) {
		this.jiraRestClient = jiraRestClient;
	}

	@Override
	public JiraRestClient getJiraRestClient() {
		return this.jiraRestClient;
	}

	@Override
	public boolean isWorking() {
		return isWorking;
	}
}