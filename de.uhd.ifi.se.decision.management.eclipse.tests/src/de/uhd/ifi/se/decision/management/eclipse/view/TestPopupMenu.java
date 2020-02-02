package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.Path;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.Test;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;

public class TestPopupMenu {
	
	@Test
	public void testPopupMenu() {
		KnowledgeGraphImpl.clear();
		
		GitClient gitClient = TestGitClient.initGitClient();
		JiraClient jiraClient = TestJiraClient.initJiraClient();
		KnowledgeGraph knowledgeGraph =  KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile node = new ChangedFileImpl(new Path("./file1"));
		
		knowledgeGraph.addVertex(node);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
        
        assertNotNull(new PopupMenu(gephiNode));
	}
	
	@Test
	public void testPopupMenuMethod() {
		KnowledgeGraphImpl.clear();
		
		GitClient gitClient = TestGitClient.initGitClient();
		JiraClient jiraClient = TestJiraClient.initJiraClient();
		KnowledgeGraph knowledgeGraph =  KnowledgeGraphImpl.getInstance(gitClient, jiraClient);
		
		ChangedFile file = new ChangedFileImpl(new Path("./file1"));
		CodeMethod node = new CodeMethodImpl("testMethod()", file);
		
		knowledgeGraph.addVertex(node);
		
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
        
        org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
        gephiNode.setLabel("[0] test");
        
        assertNotNull(new PopupMenu(gephiNode));
	}
	
	@Test
	public void testPopupMenuNull() {
        assertNotNull(new PopupMenu(null));
	}

}
