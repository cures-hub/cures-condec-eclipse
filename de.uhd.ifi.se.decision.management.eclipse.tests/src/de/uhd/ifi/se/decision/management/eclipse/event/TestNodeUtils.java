package de.uhd.ifi.se.decision.management.eclipse.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;

public class TestNodeUtils {
	
	private static GitClient gitClient;
	private static JiraClient jiraClient;

	@BeforeClass
	public static void setUp() {
		gitClient = TestGitClient.initGitClient();
		jiraClient = TestJiraClient.initJiraClient();
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient);
	}
	
	@Test
	public void testConvertNode() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node selectedNode = graphModel.factory().newNode("0");
        selectedNode.setLabel("[0] Node");
        
        assertNotNull(NodeUtils.convertNode(selectedNode));
	}
	
	@Test
	public void testConvertNodeNull() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node selectedNode = graphModel.factory().newNode("100000");
        selectedNode.setLabel("[100000] Node");
        
        assertNull(NodeUtils.convertNode(selectedNode));
	}
	
	@Test
	public void testConvertNodeException() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node selectedNode = graphModel.factory().newNode("node");
        selectedNode.setLabel("Node");
        
        assertNull(NodeUtils.convertNode(selectedNode));
	}
	
	@Test
	public void testClickInNodeTrue() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node node = graphModel.factory().newNode("node");
        node.setLabel("Node");
        node.setX(100);
        node.setY(-100);
        node.setSize(5);
        
        assertTrue(NodeUtils.clickInNode(node, 102, 102));
	}
	
	@Test
	public void testClickInNodeFalse() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node node = graphModel.factory().newNode("node");
        node.setLabel("Node");
        node.setX(100);
        node.setY(-100);
        node.setSize(5);
        
        assertFalse(NodeUtils.clickInNode(node, 4, 4));
	}
	
	@Ignore
	@Test
	public void testCreateLink() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        Node node1 = graphModel.factory().newNode("node1");
        node1.setLabel("[0] Node");
        Node node2 = graphModel.factory().newNode("node2");
        node2.setLabel("[1] Node");
        
        NodeUtils.createLink(node1, node2);
        
        KnowledgeGraph graph = new KnowledgeGraphImpl();
        assertTrue(graph.containsEdge(NodeUtils.convertNode(node1), NodeUtils.convertNode(node2)));
	}
	
	@AfterClass
	public static void tearDown() {
		de.uhd.ifi.se.decision.management.eclipse.model.Node.nodes.clear();
		GitClient.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
		JiraIssue.instances.clear();
		JiraClient.instances.clear();
	}

}
