package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for JIRA issue as part of the knowledge graph.
 */
public class JiraIssueImpl extends NodeImpl implements Node, JiraIssue {
	private Issue issue;
	private String jiraIssueKey;
	
	private List<GitCommit> commits;
	private List<JiraIssue> linkedJiraIssues;

	public JiraIssueImpl(Issue issue) {
		this.issue = issue;
		this.jiraIssueKey = issue.getKey();
		this.commits = new ArrayList<GitCommit>();
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
	public List<GitCommit> getCommits() {
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
	public List<JiraIssue> getLinkedJiraIssues() {		
		return linkedJiraIssues;
	}
}
