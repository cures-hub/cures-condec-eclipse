package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import de.uhd.ifi.se.decision.management.eclipse.view.impl.GephiGraphImpl;

public class TestGephiGraph {

	private static GitClient gitClient;
	private static JiraClient jiraClient;
	
	@BeforeClass
	public static void setUp() {
		gitClient = TestGitClient.initGitClient();
		jiraClient = TestJiraClient.initJiraClient();
	}
	
	
	@Test
	public void testKnowledgeGraphNull() {
		assertNotNull(new GephiGraphImpl(null));
		assertNotNull(new GephiGraphImpl(null, LayoutType.NOVERLAP));
	}
	
	@Test
	public void testUpdate() {
		GephiGraph gephiGraph = new GephiGraphImpl(null);
		
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		gephiGraph.update(knowledgeGraph);
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testGetGephiNodeNode() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		gephiGraph.getGephiNode(node);
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testGetGephiNodeNodeNull() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		gephiGraph.getGephiNode((Node) null);
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testGetGephiNodeId() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
        
        gephiGraph.getGephiNode(Long.valueOf((String) gephiNode.getId()).longValue());
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testGetGephiNodeText() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
        
        gephiGraph.getGephiNode(gephiNode.getLabel());
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testSetSizeofNodeGephiNode() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
        
        gephiGraph.setSizeOfNode(gephiNode, 1);
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testSetSizeofNodeGephiNodeNull() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		gephiGraph.setSizeOfNode((org.gephi.graph.api.Node) null, 1);
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testSetSizeofNodeNode() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
		
		gephiGraph.setSizeOfNode(node, 1);
		
		assertNotNull(gephiGraph);
	}
	
	@Test
	public void testSetSizeofNodeId() {
		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		GephiGraph gephiGraph = new GephiGraphImpl(knowledgeGraph);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
		
		gephiGraph.setSizeOfNode(Long.valueOf((String) gephiNode.getId()).longValue(), 1);
		
		assertNotNull(gephiGraph);
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
