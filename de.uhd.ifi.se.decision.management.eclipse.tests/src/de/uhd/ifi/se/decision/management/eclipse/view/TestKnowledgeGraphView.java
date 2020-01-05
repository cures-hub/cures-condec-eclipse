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
	private static KnowledgeGraph knowledgeGraph;

	@BeforeClass
	public static void setUp() {
		knowledgeGraph = initKnowledgeGraph();
		knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
	}

	public static KnowledgeGraph initKnowledgeGraph() {
		KnowledgeGraphImpl.clear();
		
		GitClient gitClient = TestGitClient.initGitClient();
		JiraClient jiraClient = TestJiraClient.initJiraClient();
		return KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
	}

	@Test
	public void testConstructor() {
		assertNotNull(knowledgeGraphView);
		knowledgeGraphView.highlightNode(null);
		knowledgeGraphView.highlightSelectedNode();
	}
	
	@Test
	public void testGetInstance() {
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
		
		assertNotNull(knowledgeGraphViewTest);
	}
	
	@Test
	public void testUpdate() {
		knowledgeGraphView.update(knowledgeGraph);
		
		assertNotNull(knowledgeGraphView);
		
	}
	
	public static void main(String[] args) {
		setUp();
	}
}
