package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueLink;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;

/**
 * Connects with JIRA
 */
public class JiraClientImpl implements JiraClient {

	private AsynchronousJiraRestClientFactory factory;
	private JiraRestClient jiraRestClient;
	private boolean isAuthenticated = false;
	private static Map<String, JiraClient> instances = new HashMap<String, JiraClient>();

	public static JiraClient getOrCreate() {
		String url = ConfigPersistenceManager.getJiraUrl().toLowerCase();
		if (instances.containsKey(url)) {
			return instances.get(url);
		} else {
			JiraClient jm = new JiraClientImpl();
			instances.put(url, jm);
			return jm;
		}
	}

	public JiraClientImpl() {
		this.factory = new AsynchronousJiraRestClientFactory();
	}

	public static Map<String, JiraClient> getInstances() {
		return instances;
	}

	@Override
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

	@Override
	public int authenticate() {
		return authenticate(ConfigPersistenceManager.getJiraUrl(), ConfigPersistenceManager.getJiraUser(),
				ConfigPersistenceManager.getJiraPassword());
	}

	@Override
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
			this.getJiraRestClient().getProjectClient().getProject(ConfigPersistenceManager.getProjectKey());
		} catch (Exception e) {
			System.err.println("The project is unknown.");
			System.err.println(e.getMessage());
			return 3;
		}
		return 0;
	}

	@Override
	public Set<JiraIssue> getAllIssues() {
		Set<JiraIssue> allIssues = new HashSet<JiraIssue>();
		try {
			for (BasicIssue issue : this.getJiraRestClient().getSearchClient()
					.searchJql("project=\"" + ConfigPersistenceManager.getProjectKey() + "\"", -1, 0).claim()
					.getIssues()) {
				allIssues.add(JiraIssueImpl.getOrCreate(issue.getKey(), this));
			}
		} catch (Exception e) {

		}
		return allIssues;
	}

	@Override
	public Issue getIssue(String issueKey) {
		// if (issueKey.getKeynumber() == 0) {
		// return null;
		// }
		Issue issue = null;
		try {
			issue = this.getJiraRestClient().getIssueClient().getIssue(issueKey).get();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println(issueKey + ": " + e.getMessage());
			e.printStackTrace();
		}
		return issue;
	}

	@Override
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

	@Override
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

	@Override
	public void close() {
		try {
			// Does not work with current JIRA-Rest-API:
			// this.jiraRestClient.close();
		} catch (Exception e) {
			System.err.println("IOException during closing REST client. " + e.getMessage());
		}
	}

	@Override
	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}

	@Override
	public void setJiraRestClient(JiraRestClient jiraRestClient) {
		this.jiraRestClient = jiraRestClient;
	}

	@Override
	public JiraRestClient getJiraRestClient() {
		return this.jiraRestClient;
	}
}