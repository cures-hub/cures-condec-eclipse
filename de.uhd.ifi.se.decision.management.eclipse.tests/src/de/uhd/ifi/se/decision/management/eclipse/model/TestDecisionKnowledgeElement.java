package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class TestDecisionKnowledgeElement {
	
	private static GitClient gitClient;
	
	@BeforeClass
	public static void setUp() {
		gitClient = TestGitClient.initGitClient();
	}
	
	@Test
	public void testSetAndGetKnowledgeType() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
		element.setType(KnowledgeType.DECISION);
		assertEquals(KnowledgeType.DECISION, element.getType());
	}
	
	@Test
	public void testSetAndGetSummary() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"How to ...?");
		element.setSummary("This is a decision!");
		assertEquals("This is a decision!", element.getSummary());
	}
	
	@Test
	public void testToString() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"This is a decision!");
		assertEquals("Decision: This is a decision!", element.toString());
	}
	
	@Test
	public void testSetGetCommit() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"This is a decision!");
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		element.setCommit(commits.get(0));
		assertEquals(commits.get(0), element.getCommit());
	}
	
	@AfterClass
	public static void tearDown() {
		GitClient.instances.clear();
		GitCommit.instances.clear();
	}

}
