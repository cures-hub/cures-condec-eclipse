package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;

public class TestKnowledgeGraph {

	private static GitClient gitClient;
	private static JiraClient jiraClient;

	@BeforeClass
	public static void setUp() {
		gitClient = TestGitClient.initGitClient();
		jiraClient = TestJiraClient.initJiraClient();
	}

	@Test
	public void testKnowledgeGraphForEntireProject() {
		KnowledgeGraphImpl.clear();
		
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		assertNotNull(knowledgeGraph);
		assertTrue(knowledgeGraph.vertexSet().size() > 0);
		assertNull(knowledgeGraph.getStartNode());

		assertEquals(gitClient, knowledgeGraph.getGitClient());
		assertEquals(jiraClient, knowledgeGraph.getJiraClient());
	}

	@Test
	public void testKnowledgeGraphForSubGraphFromWorkItem() {
		KnowledgeGraphImpl.clear();
		
		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(1, workItem.getLinkedJiraIssues().size());
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient, workItem, 1);
		assertEquals(7, knowledgeGraph.vertexSet().size());
	}

	@Test
	public void testKnowledgeGraphForSubGraphFromFile() {
		KnowledgeGraphImpl.clear();
		
		IPath path = gitClient.getPath().removeLastSegments(1).append("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient, file, 1);

		assertTrue(knowledgeGraph.vertexSet().size() >= 10);
	}

	@Test
	public void testKnowledgeGraphImplicitGitAndJiraClients() {
		KnowledgeGraphImpl.clear();
		
		assertNotNull(KnowledgeGraphImpl.getInstance());
		
		KnowledgeGraphImpl.clear();
		
		assertNotNull(KnowledgeGraphImpl.getInstance(null, 0));
	}
	
	@Test
	public void testGetInstance() {
		KnowledgeGraphImpl.clear();
		
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		assertNotNull(knowledgeGraph);
		assertTrue(knowledgeGraph.vertexSet().size() > 0);
		assertNull(knowledgeGraph.getStartNode());

		assertEquals(gitClient, knowledgeGraph.getGitClient());
		assertEquals(jiraClient, knowledgeGraph.getJiraClient());
		
		KnowledgeGraphImpl.clear();
		
		KnowledgeGraph knowledgeGraphTest = KnowledgeGraphImpl.getInstance();
		
		assertNotNull(knowledgeGraphTest);
		assertTrue(knowledgeGraphTest.vertexSet().size() > 0);
		assertNull(knowledgeGraphTest.getStartNode());

		assertEquals(gitClient, knowledgeGraphTest.getGitClient());
		assertEquals(jiraClient, knowledgeGraphTest.getJiraClient());
	}

	@Test
	public void testToString() {
		KnowledgeGraphImpl.clear();
		
		IPath path = gitClient.getPath().removeLastSegments(1).append("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient, file, 0);
		assertEquals(file, knowledgeGraph.getStartNode());
		assertTrue(
				knowledgeGraph.toString().startsWith("The start node for knowledge exploration is the File: pom.xml\n\n"
						+ "At distance 1 the following nodes are linked:\n"));
	}
	
	@Test
	public void testCreateLink() {
		KnowledgeGraphImpl.clear();
		
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
        DecisionKnowledgeElement node2 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is also a decision!");
        
        KnowledgeGraph graph = KnowledgeGraphImpl.getInstance();
        
        graph.addVertex(node1);
        graph.addVertex(node2);
        graph.insertLink(node1, node2);
        
        assertTrue(graph.containsEdge(node1, node2));
	}
	
	@Test
	public void testLinkExists() {
		KnowledgeGraphImpl.clear();
		
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
        DecisionKnowledgeElement node2 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is also a decision!");
        
        KnowledgeGraph graph = KnowledgeGraphImpl.getInstance();
        
        graph.addVertex(node1);
        graph.addVertex(node2);
        graph.addEdge(node1, node2);
        
        assertTrue(graph.linkExists(node1, node2));
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
	
	public static void main(String[] args) {
		setUp();
		IPath path = gitClient.getPath().removeLastSegments(1).append("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient, file, 3);
		System.out.println(knowledgeGraph.toString());
	}
}
