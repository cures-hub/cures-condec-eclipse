package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;

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
		
		KnowledgeGraphImpl.clear();
		
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
	public void testGetInstanceStartNode() {
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
		
		KnowledgeGraphImpl.clear();
		
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient, node1, 1);
		
		assertNotNull(knowledgeGraph);
		assertEquals(gitClient, knowledgeGraph.getGitClient());
		assertEquals(jiraClient, knowledgeGraph.getJiraClient());
		assertTrue(knowledgeGraph.vertexSet().size() > 0);
		assertTrue(knowledgeGraph.getStartNode().equals(node1));
		
		KnowledgeGraphImpl.clear();
		
		KnowledgeGraph knowledgeGraphTest = KnowledgeGraphImpl.getInstance(node1, 1);
		
		assertNotNull(knowledgeGraphTest);
		assertEquals(gitClient, knowledgeGraphTest.getGitClient());
		assertEquals(jiraClient, knowledgeGraphTest.getJiraClient());
		assertTrue(knowledgeGraphTest.vertexSet().size() > 0);
		assertTrue(knowledgeGraphTest.getStartNode().equals(node1));
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
	public void testCreateLinkNodes() {
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
	public void testCreateLinkLink() {
		KnowledgeGraphImpl.clear();
		
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
        
        KnowledgeGraph graph = KnowledgeGraphImpl.getInstance();
        
        graph.addVertex(node1);
        graph.addVertex(node2);
        Link link = new LinkImpl(node1, node2);
        
        graph.insertLink(link);
        
        assertTrue(graph.containsEdge(link.getSourceNode(), link.getTargetNode()));
	}
	
	@Test
	public void testLinkExistsNodes() {
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
	
	@Test
	public void testLinkExistsLink() {
		KnowledgeGraphImpl.clear();
		
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
        
        Link link = new LinkImpl(node1, node2);
        
        KnowledgeGraph graph = KnowledgeGraphImpl.getInstance();
        
        graph.addVertex(link.getSourceNode());
        graph.addVertex(link.getTargetNode());
        graph.addEdge(link.getSourceNode(), link.getTargetNode());
        
        assertTrue(graph.linkExists(link));
	}

	@AfterClass
	public static void tearDown() {
		KnowledgeGraphImpl.clear();
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
