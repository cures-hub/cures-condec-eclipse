package de.uhd.ifi.se.decision.management.eclipse.mock;

import java.net.URI;
import java.util.Set;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class MockJiraIssue implements JiraIssue {

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public boolean addLinkedNode(Node node) {
		return false;
	}

	@Override
	public Set<Node> getLinkedNodes() {
		return null;
	}

	@Override
	public Issue getIssue() {
		Issue issue = createIssue("WI: Create empty Eclipse plugin", "ECONDEC-1");
		return issue;
	}

	@Override
	public String getJiraIssueKey() {
		return getIssue().getKey();
	}

	@Override
	public URI getUri() {
		return null;
	}

	public static Issue createIssue(String summary, String key) {
		return new Issue(summary, null, key, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
}
