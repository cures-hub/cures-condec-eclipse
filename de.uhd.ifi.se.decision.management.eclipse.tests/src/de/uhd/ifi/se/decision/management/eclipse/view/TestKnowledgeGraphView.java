package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.KnowledgeGraphViewImpl;

public class TestKnowledgeGraphView {

	private KnowledgeGraphView knowledgeGraphView;
	private KnowledgeGraph knowledgeGraph;

	@Before
	public void setUp() {
		KnowledgeGraphImpl.clear();
		KnowledgeGraphViewImpl.clear();
		knowledgeGraph = initKnowledgeGraph();
		knowledgeGraphView = KnowledgeGraphViewImpl.getInstance(knowledgeGraph);
	}

	public static KnowledgeGraph initKnowledgeGraph() {
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
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertNotNull(knowledgeGraphViewTest);
	}

	@Test
	public void testGetInstanceFrameTitleNull() {
		KnowledgeGraphViewImpl.clear();

		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertNotNull(knowledgeGraphViewTest);
	}

	@Test
	public void testConstructorNoGraph() {
		KnowledgeGraphViewImpl.clear();
		knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();

		assertNotNull(knowledgeGraphView);
		knowledgeGraphView.highlightNode(null);
		knowledgeGraphView.highlightSelectedNode();
	}

	@Test
	public void testConstructorGraph() {
		KnowledgeGraphViewImpl.clear();
		knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();

		knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();

		assertNotNull(knowledgeGraphView);
		knowledgeGraphView.highlightNode(null);
		knowledgeGraphView.highlightSelectedNode();
	}

	@Test
	public void testResetFilters() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertTrue(knowledgeGraphViewTest.resetFilters());
	}

	@Test
	public void testHighlightSelectedNode() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		ChangedFile node = new ChangedFileImpl(new Path("./file1"));

		knowledgeGraph.addVertex(node);

		knowledgeGraphViewTest.highlightSelectedNodeAndUpdate(node);

		assertTrue(knowledgeGraphViewTest.highlightSelectedNode());
	}

	@Test
	public void testHighlightSelectedNodeNull() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertFalse(knowledgeGraphViewTest.highlightSelectedNode());
	}

	@Test
	public void testHighlightNode() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		ChangedFile node = new ChangedFileImpl(new Path("./file1"));

		knowledgeGraph.addVertex(node);

		assertTrue(knowledgeGraphViewTest.highlightNode(node));
	}

	@Test
	public void testHighlightNodeNull() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertFalse(knowledgeGraphViewTest.highlightNode(null));
	}

	@Test
	public void testHighlightSelectedNodeAndUpdate() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		ChangedFile node = new ChangedFileImpl(new Path("./file1"));

		knowledgeGraph.addVertex(node);

		assertTrue(knowledgeGraphViewTest.highlightSelectedNodeAndUpdate(node));
	}

	@Test
	public void testCreateNode() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertTrue(knowledgeGraphViewTest.createNode());
	}

	@Test
	public void testGetLinkDistance() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertTrue(knowledgeGraphViewTest.getLinkDistance() == 2);
	}

	@Test
	public void testGetGephiGraph() {
		KnowledgeGraphViewImpl.clear();
		KnowledgeGraphView knowledgeGraphViewTest = KnowledgeGraphViewImpl.getInstance(knowledgeGraph,
				"Knowledge Graph");

		assertNotNull(knowledgeGraphViewTest.getGephiGraph());
	}

	@After
	public void tearDown() {
		KnowledgeGraphImpl.clear();
		KnowledgeGraphViewImpl.clear();
	}

	public static void main(String[] args) {
		new TestKnowledgeGraphView().setUp();
	}
}
