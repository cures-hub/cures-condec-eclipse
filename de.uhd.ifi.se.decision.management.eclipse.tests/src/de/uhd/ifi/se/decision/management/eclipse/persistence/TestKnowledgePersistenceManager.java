package de.uhd.ifi.se.decision.management.eclipse.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
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
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
        DecisionKnowledgeElement node2 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is also a decision!");
        
        org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));
        org.gephi.graph.api.Node gephiNode2 = graphModel.factory().newNode(String.valueOf(node2.getId()));
        
        KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient);
        
        knowledgeGraph.addVertex(node1);
        knowledgeGraph.addVertex(node2);
        
        KnowledgeGraphView knowledgeGraphView = new KnowledgeGraphViewImpl(knowledgeGraph);
        
        KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2);
        
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
