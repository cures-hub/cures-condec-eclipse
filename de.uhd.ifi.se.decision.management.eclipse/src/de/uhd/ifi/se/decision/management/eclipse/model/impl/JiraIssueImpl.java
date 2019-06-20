package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;

import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;

/**
 * Class for JIRA issue as part of the knowledge graph.
 */
public class JiraIssueImpl extends NodeImpl implements JiraIssue {
	private Issue issue;
	private String jiraIssueKey;

	private Set<GitCommit> commits;
	private Set<JiraIssue> linkedJiraIssues;

	public JiraIssueImpl(Issue issue) {
		this.issue = issue;
		this.jiraIssueKey = issue.getKey();
		this.commits = new HashSet<GitCommit>();
		this.linkedJiraIssues = new HashSet<JiraIssue>();
	}

	@Override
	public Issue getIssue() {
		return issue;
	}

	@Override
	public String getJiraIssueKey() {
		return jiraIssueKey;
	}

	@Override
	public String toString() {
		return getJiraIssueKey() + ":" + issue.getSummary();
	}

	@Override
	public URI getUri() {
		URI restUri = issue.getSelf();
		String jiraIssueUri = "";
		for (String uriPart : restUri.toString().split("/")) {
			if ("rest".equals(uriPart)) {
				jiraIssueUri += "projects/" + issue.getProject().getKey() + "/issues/" + issue.getKey();
				break;
			}
			jiraIssueUri += uriPart + "/";
		}
		return URI.create(jiraIssueUri);
	}

	/**
	 * @issue How to retrieve the commits linked to a JIRA issue?
	 * @decision Iterate over GitCommit objects and check whether they contain the
	 *           key!
	 * @alternative GitClient.getOrCreate().getCommitsForJiraIssue(jiraIssueKey);
	 */
	@Override
	public Set<GitCommit> getCommits() {
		if (commits.isEmpty()) {
			List<GitCommit> allCommits = new ArrayList<GitCommit>(GitCommit.instances.values());
			for (GitCommit gitCommit : allCommits) {
				if (gitCommit.getJiraIssueKeys().contains(jiraIssueKey)) {
					addCommit(gitCommit);
				}
			}
		}
		return commits;
	}

	@Override
	public void addCommit(GitCommit gitCommit) {
		this.commits.add(gitCommit);
	}

	@Override
	public Set<JiraIssue> getLinkedJiraIssues() {
		return linkedJiraIssues;
	}

	@Override
	public Set<String> getKeysOfLinkedJiraIssues() {
		Set<String> neighborJiraIssueKeys = new HashSet<String>();

		Iterable<IssueLink> issueLinkIterable = getIssue().getIssueLinks();
		Iterator<IssueLink> issueLinkIterator = issueLinkIterable.iterator();

		while (issueLinkIterator.hasNext()) {
			IssueLink link = issueLinkIterator.next();
			String neighborIssueKey = link.getTargetIssueKey();
			neighborJiraIssueKeys.add(neighborIssueKey);
		}

		return neighborJiraIssueKeys;
	}
}
