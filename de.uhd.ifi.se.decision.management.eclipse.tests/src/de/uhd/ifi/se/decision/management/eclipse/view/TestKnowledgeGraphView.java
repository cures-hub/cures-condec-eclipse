package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
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
		KnowledgeGraphViewImpl.clear();
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
	public void testUpdate() {
		knowledgeGraphView.update(knowledgeGraph);
		
		assertNotNull(knowledgeGraphView);
	}
	
	@Test
	public void testGetInstance() {
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
		
		assertNotNull(knowledgeGraphViewTest);
	}
	
	@Test
	public void testGetInstanceNull() {
		KnowledgeGraphViewImpl.clear();
		
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
		
		assertNotNull(knowledgeGraphViewTest);
	}
	
	@Test
	public void testGetInstanceFrameTitle() {
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph, "Knowledge Graph");
		
		assertNotNull(knowledgeGraphViewTest);
	}
	
	@Test
	public void testGetInstanceFrameTitleNull() {
		KnowledgeGraphViewImpl.clear();
		
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph, "Knowledge Graph");
		
		assertNotNull(knowledgeGraphViewTest);
	}
	
	@AfterClass
	public static void tearDown() {
		KnowledgeGraphImpl.clear();
		KnowledgeGraphViewImpl.clear();
	}
	
	public static void main(String[] args) {
		setUp();
	}
}
