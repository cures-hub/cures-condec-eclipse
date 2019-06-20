package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class TestKnowledgeGraph {

	private GitClient gitClient;
	private JiraClient jiraClient;

	@Before
	public void setUp() {
		IPath path = TestGitClient.initPathToGitRepo();
		gitClient = GitClient.getOrCreate(path, "HEAD", "ECONDEC");

		jiraClient = TestJiraClient.initJiraClient();
	}

	@Test
	public void testKnowledgeGraphForEntireProject() {
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient);
		assertNotNull(knowledgeGraph);
		assertTrue(knowledgeGraph.vertexSet().size() > 0);

		assertEquals(gitClient, knowledgeGraph.getGitClient());
		assertEquals(jiraClient, knowledgeGraph.getJiraClient());
	}

	@Test
	public void testKnowledgeGraphForSubGraph() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"This is a decision!");
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient, element, 0);
		assertTrue(knowledgeGraph.vertexSet().size() == 1);
	}

	@AfterClass
	public static void tearDown() {
		Node.nodes.clear();
		GitClient.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
	}
}
