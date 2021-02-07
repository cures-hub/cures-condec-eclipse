package de.uhd.ifi.se.decision.management.eclipse.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.junit.After;
import org.junit.Before;
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
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class TestKnowledgePersistenceManager {

	final private static String KNOWLEDGE_LOCATION_FOLDER = "target";
	final private static String KNOWLEDGE_LOCATION_FILE = "knowledge.json";

	private GitClient gitClient;
	private JiraClient jiraClient;

	@Before
	public void setUp() {
		gitClient = TestGitClient.initGitClient();
		jiraClient = TestJiraClient.initJiraClient();
	}

	@Test
	@Ignore
	public void testReadLinksFromJSON() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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

		KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2);

		KnowledgeGraphImpl.clear();

		knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node1);
		knowledgeGraph.addVertex(node2);

		KnowledgePersistenceManager.readLinksFromJSON();

		assertNotNull(knowledgeGraphView);
		assertTrue(knowledgeGraph.linkExists(node1, node2));
	}

	@Test
	@Ignore
	public void testReadLinksFromJSONNoSourceNode() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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

		KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2);

		KnowledgeGraphImpl.clear();

		knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node2);

		KnowledgePersistenceManager.readLinksFromJSON();

		assertNotNull(knowledgeGraphView);
		assertFalse(knowledgeGraph.linkExists(node1, node2));
	}

	@Test
	@Ignore
	public void testReadLinksFromJSONNoTargetNode() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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

		KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2);

		KnowledgeGraphImpl.clear();

		knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node2);

		KnowledgePersistenceManager.readLinksFromJSON();

		assertNotNull(knowledgeGraphView);
		assertFalse(knowledgeGraph.linkExists(node1, node2));
	}

	@Test
	public void testReadLinksFromJSONEmpty() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		KnowledgePersistenceManager.readLinksFromJSON();

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testInsertLink() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.insertLink(null, null));

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testInsertLinkNull1() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));

		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

		org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node1);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.insertLink(gephiNode1, null));

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testInsertLinkNull2() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		ChangedFile node2 = new ChangedFileImpl(new Path("./file1"));

		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

		org.gephi.graph.api.Node gephiNode2 = graphModel.factory().newNode(String.valueOf(node2.getId()));

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node2);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.insertLink(null, gephiNode2));

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testInsertLinkAlreadyExists() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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

		file.delete();

		assertFalse(KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2));

		assertNotNull(knowledgeGraphView);
		assertTrue(knowledgeGraph.linkExists(node1, node2));
	}

	@Test
	public void testInsertLinkLoop() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));

		org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node1);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode1));

		assertNotNull(knowledgeGraphView);
		assertFalse(knowledgeGraph.linkExists(node1, node1));
	}

	@Test
	@Ignore
	public void testRemoveLink() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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

		KnowledgePersistenceManager.insertLink(gephiNode1, gephiNode2);

		assertTrue(KnowledgePersistenceManager.removeLink(gephiNode1, gephiNode2));

		assertNotNull(knowledgeGraphView);
		assertFalse(knowledgeGraph.linkExists(node1, node2));
	}

	@Test
	public void testRemoveLinkNull() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.removeLink(null, null));

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testRemoveLinkNull1() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));

		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

		org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node1);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.removeLink(gephiNode1, null));

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testRemoveLinkNull2() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		ChangedFile node2 = new ChangedFileImpl(new Path("./file1"));

		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

		org.gephi.graph.api.Node gephiNode2 = graphModel.factory().newNode(String.valueOf(node2.getId()));

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node2);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.removeLink(null, gephiNode2));

		assertNotNull(knowledgeGraphView);
	}

	@Test
	public void testRemoveLinkDoesntExists() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

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

		assertFalse(KnowledgePersistenceManager.removeLink(gephiNode1, gephiNode2));

		assertNotNull(knowledgeGraphView);
		assertFalse(knowledgeGraph.linkExists(node1, node2));
	}

	@Test
	public void testRemoveLinkLoop() {
		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();

		KnowledgeGraphImpl.clear();

		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));

		org.gephi.graph.api.Node gephiNode1 = graphModel.factory().newNode(String.valueOf(node1.getId()));

		KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(gitClient, jiraClient);

		knowledgeGraph.addVertex(node1);

		KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);

		assertFalse(KnowledgePersistenceManager.removeLink(gephiNode1, gephiNode1));

		assertNotNull(knowledgeGraphView);
		assertFalse(knowledgeGraph.linkExists(node1, node1));
	}

	@Test
	@Ignore
	public void testCreateDecisionKnowledgeElementInJira() {
		String type = "DECISION";
		String summary = "Test";
		String description = "This is a test issue.";

		assertTrue(KnowledgePersistenceManager.createDecisionKnowledgeElementInJira(type, summary, description));
	}

	@After
	public void tearDown() {
		KnowledgeGraphImpl.clear();
		Node.nodes.clear();
		GitClient.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
		JiraIssue.instances.clear();
		JiraClient.instances.clear();

		File folder = new File(KNOWLEDGE_LOCATION_FOLDER);
		File file = new File(folder, KNOWLEDGE_LOCATION_FILE);
		file.delete();
	}

}
