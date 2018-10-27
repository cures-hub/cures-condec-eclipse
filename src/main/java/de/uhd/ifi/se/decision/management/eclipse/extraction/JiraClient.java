package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import de.uhd.ifi.se.decision.management.eclipse.PreferenceInitializer;

/**
 * Connects with JIRA
 */
public class JiraClient {

	private AsynchronousJiraRestClientFactory factory;
	private JiraRestClient jiraRestClient;
	private boolean isAuthenticated = false;

	public JiraClient() {
		this.factory = new AsynchronousJiraRestClientFactory();
	}

	/**
	 * Login to JIRA
	 */
	public int authenticate(String jiraURL, String username, String password) {
		int response = 0;

		try {
			this.jiraRestClient = this.factory.createWithBasicHttpAuthentication(new URI(jiraURL), username, password);
			response = this.getAuthenticationResponse(username);

			if (response == 0)
				this.isAuthenticated = true;

		} catch (URISyntaxException e) {
			System.err.println("Authentication failed.");
			e.printStackTrace();
		}
		return response;
	}

	public int authenticate() {
		return authenticate(PreferenceInitializer.getJiraUrl(), PreferenceInitializer.getJiraUser(),
				PreferenceInitializer.getJiraPassword());
	}

	public int getAuthenticationResponse(String username) {
		try {
			this.getJiraRestClient().getUserClient().getUser(username).get().getSelf();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("The JIRA username seems to be wrong.");
			System.err.println(e.getMessage());
			return 1;
		}

		try {
			this.getJiraRestClient().getSessionClient().getCurrentSession().get().getUserUri().getPath();
		} catch (Exception e) {
			System.err.println("There is a problem with establishing the session.");
			System.err.println(e.getMessage());
			return 2;
		}

		try {
			this.getJiraRestClient().getProjectClient().getProject(PreferenceInitializer.getProjectKey());
		} catch (Exception e) {
			System.err.println("The project is unknown.");
			System.err.println(e.getMessage());
			return 3;
		}
		return 0;
	}

	public Issue getIssue(String issueKey) {
		Issue issue = null;
		try {
			issue = this.getJiraRestClient().getIssueClient().getIssue(issueKey).get();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return issue;
	}

	/**
	 * Retrieves the issues linked to a given issue with a certain link distance
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return Linked issues mapped to link distance
	 */
	public Map<Issue, Integer> getLinkedIssues(Issue issue, int distance) {
		Map<Issue, Integer> linkedIssuesAtDistance = new HashMap<Issue, Integer>();

		if (issue == null || distance == 0) {
			return linkedIssuesAtDistance;
		}

		List<String> analyzedIssueKeys = new ArrayList<String>();
		analyzedIssueKeys.add(issue.getKey());

		for (int i = 1; i <= distance; i++) {
			List<String> neighborIssueKeys = getKeysOfNeighborIssues(issue);
			for (String issueKey : neighborIssueKeys) {
				if (!analyzedIssueKeys.contains(issueKey)) {
					analyzedIssueKeys.add(issueKey);
					issue = this.getIssue(issueKey);
					linkedIssuesAtDistance.put(issue, i);
				}
			}
		}
		return linkedIssuesAtDistance;
	}

	/**
	 * Retrieves keys of linked issue to an issue at link distance 1
	 * 
	 * @param issue
	 *            JIRA issue
	 * @return keys of linked issues
	 */
	public List<String> getKeysOfNeighborIssues(Issue issue) {

		if (issue == null) {
			return null;
		}

		List<String> neighborIssueKeys = new ArrayList<String>();

		Iterable<IssueLink> issueLinkIterable = issue.getIssueLinks();
		Iterator<IssueLink> issueLinkIterator = issueLinkIterable.iterator();

		while (issueLinkIterator.hasNext()) {
			IssueLink link = issueLinkIterator.next();

			String neighborIssueKey = link.getTargetIssueKey();
			neighborIssueKeys.add(neighborIssueKey);
		}

		return neighborIssueKeys;
	}

	/**
	 * Closes the client when finished
	 */
	public void close() {
		try {
			this.jiraRestClient.close();
		} catch (IOException e) {
			System.err.println("IOException during closing REST client. " + e.getMessage());
		}
	}

	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}

	public JiraRestClient getJiraRestClient() {
		return this.jiraRestClient;
	}

	public void setJiraRestClient(JiraRestClient jiraRestClient) {
		this.jiraRestClient = jiraRestClient;
	}
}