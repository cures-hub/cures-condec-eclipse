package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class TestKnowledgeGraph {

	private GitClient gitClient;
	private JiraClient jiraClient;

	@Before
	public void setUp() {
		gitClient = new GitClientImpl("", "HEAD", "");
		jiraClient = new JiraClientImpl(URI.create(""), "", "", "");
		Node.nodes.clear();
	}

	@Test
	public void testKnowledgeGraphForEntireProject() {
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient);
		assertNotNull(knowledgeGraph);
		assertTrue(knowledgeGraph.vertexSet().size() == 0);

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
}
