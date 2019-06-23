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
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient);
		assertNotNull(knowledgeGraph);
		assertTrue(knowledgeGraph.vertexSet().size() > 0);
		assertNull(knowledgeGraph.getStartNode());

		assertEquals(gitClient, knowledgeGraph.getGitClient());
		assertEquals(jiraClient, knowledgeGraph.getJiraClient());
	}

	@Test
	public void testKnowledgeGraphForSubGraphFromWorkItem() {
		JiraIssue workItem = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		assertEquals(1, workItem.getLinkedJiraIssues().size());
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient, workItem, 1);
		assertEquals(7, knowledgeGraph.vertexSet().size());
	}

	@Test
	public void testKnowledgeGraphForSubGraphFromFile() {
		IPath path = gitClient.getPath().removeLastSegments(1).append("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient, file, 1);

		assertTrue(knowledgeGraph.vertexSet().size() >= 10);
	}

	@Test
	public void testKnowledgeGraphImplicitGitAndJiraClients() {
		assertNotNull(new KnowledgeGraphImpl());
		assertNotNull(new KnowledgeGraphImpl(null, 0));
	}

	@Test
	public void testToString() {
		IPath path = gitClient.getPath().removeLastSegments(1).append("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient, file, 0);
		assertEquals(file, knowledgeGraph.getStartNode());
		assertTrue(
				knowledgeGraph.toString().startsWith("The start node for knowledge exploration is the File: pom.xml\n\n"
						+ "At distance 1 the following nodes are linked:\n"));
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
