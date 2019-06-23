package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class TestKnowledgeGraphView {

	private KnowledgeGraph knowledgeGraph;
	private KnowledgeGraphView knowledgeGraphView;

	@Before
	public void setUp() {
		knowledgeGraph = initKnowledgeGraph();
		knowledgeGraphView = new KnowledgeGraphViewImpl(knowledgeGraph);
	}

	public static KnowledgeGraph initKnowledgeGraph() {
		GitClient gitClient = TestGitClient.initGitClient();
		JiraClient jiraClient = TestJiraClient.initJiraClient();
		return new KnowledgeGraphImpl(gitClient, jiraClient);
	}

	@Test
	public void testConstructor() {
		assertNotNull(knowledgeGraphView);
	}
	
	@Test
	public void testHightlightNode() {
		knowledgeGraphView.highlightNode(null);
	}
}
