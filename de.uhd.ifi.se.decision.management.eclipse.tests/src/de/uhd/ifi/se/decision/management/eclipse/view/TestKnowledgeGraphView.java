package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class TestKnowledgeGraphView {

	private static KnowledgeGraphView knowledgeGraphView;

	@BeforeClass
	public static void setUpBeforeClass() {
		KnowledgeGraph knowledgeGraph = initKnowledgeGraph();
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
		knowledgeGraphView.highlightNode(null);
		knowledgeGraphView.highlightSelectedNode();
	}
}
