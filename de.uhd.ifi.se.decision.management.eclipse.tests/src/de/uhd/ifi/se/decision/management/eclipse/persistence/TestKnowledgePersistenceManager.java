package de.uhd.ifi.se.decision.management.eclipse.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.Path;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class TestKnowledgePersistenceManager {
	
	private static GitClient gitClient;
	private static JiraClient jiraClient;
	
	@BeforeClass
	public static void setUp() {
		gitClient = TestGitClient.initGitClient();
		jiraClient = TestJiraClient.initJiraClient();
	}
	
	@Test
	public void testInsertLink() {
		KnowledgeGraphImpl.clear();
		
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));
        org.gephi.graph.api.Node gephiNode2 = graphModel.factory().newNode(String.valueOf(node2.getId()));
        
        KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
        
        knowledgeGraph.addVertex(node1);
        knowledgeGraph.addVertex(node2);
        
        KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
        
        assertTrue(KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2));
        
        assertNotNull(knowledgeGraphView);
        assertTrue(knowledgeGraph.linkExists(node1, node2));
	}
	
	@Test
	public void testInsertLinkNull() {
		KnowledgeGraphImpl.clear();
		
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
        
        KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
        
        assertFalse(KnowledgePersistenceManager.insertLink(null, null));
        
        assertNotNull(knowledgeGraphView);
	}
	
	@Test
	public void testInsertLinkAlreadyExists() {
		KnowledgeGraphImpl.clear();
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
        
        org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));
        org.gephi.graph.api.Node gephiNode2 = graphModel.factory().newNode(String.valueOf(node2.getId()));
        
        KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
        
        knowledgeGraph.addVertex(node1);
        knowledgeGraph.addVertex(node2);
        
        KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
        
        KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2);
        
        assertFalse(KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2));
        
        assertNotNull(knowledgeGraphView);
        assertTrue(knowledgeGraph.linkExists(node1, node2));
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
