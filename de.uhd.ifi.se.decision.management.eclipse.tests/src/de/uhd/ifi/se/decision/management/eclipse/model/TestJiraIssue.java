package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;

public class TestJiraIssue {

	@Test
	public void testNonWorkingJiraClient() {
		JiraClient jiraClient = new JiraClientImpl(URI.create(""), "", "", "");
		JiraIssue jiraIssue = JiraIssue.getOrCreate("", jiraClient);
		assertNull(jiraIssue);
	}

}
