package de.uhd.ifi.se.decision.management.eclipse.view.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.RenderTarget;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.OpenWebbrowser;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.persistence.GraphSettings;
import de.uhd.ifi.se.decision.management.eclipse.view.GephiGraph;
import de.uhd.ifi.se.decision.management.eclipse.view.GraphFiltering;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.PreviewSketch;

public class KnowledgeGraphViewImpl implements KnowledgeGraphView {
	private String searchString = "";
	private long selectedNodeId = -1;
	private JTextField tfSearch;
	private JTextField tfInteraction;
	private PreviewController previewController;
	private PreviewSketch previewSketch;
	public GraphFiltering graphFiltering;
	public GephiGraph gephiGraph;
	public Map<String, JCheckBox> filterCheckBoxes;

	public KnowledgeGraphViewImpl() {
		this.gephiGraph = new GephiGraphImpl();

		this.previewController = Lookup.getDefault().lookup(PreviewController.class);
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		this.previewSketch = new PreviewSketch(target);

		GraphSettings.initPreviewModel(previewController);
		this.graphFiltering = new GraphFiltering();

		this.filterCheckBoxes = initFilterCheckBoxes();
	}

	private Map<String, JCheckBox> initFilterCheckBoxes() {
		Map<String, JCheckBox> filterCheckBoxes = new HashMap<String, JCheckBox>();

		filterCheckBoxes.put("commit", new JCheckBox("Commits"));
		filterCheckBoxes.put("jiraIssue", new JCheckBox("JIRA Issues"));

		filterCheckBoxes.put("decisionKnowledge", new JCheckBox("Decision Knowledge"));
		filterCheckBoxes.put("issue", new JCheckBox("Issues"));
		filterCheckBoxes.put("decision", new JCheckBox("Decisions"));
		filterCheckBoxes.put("alternative", new JCheckBox("Alternatives"));
		filterCheckBoxes.put("pro", new JCheckBox("Pros"));
		filterCheckBoxes.put("con", new JCheckBox("Cons"));

		filterCheckBoxes.put("file", new JCheckBox("Files"));
		filterCheckBoxes.put("nonJava", new JCheckBox("No Java-Files"));
		filterCheckBoxes.put("class", new JCheckBox("Classes"));
		filterCheckBoxes.put("method", new JCheckBox("Methods"));

		for (Map.Entry<String, JCheckBox> entry : filterCheckBoxes.entrySet()) {
			String key = entry.getKey();
			JCheckBox checkBox = entry.getValue();
			checkBox.setSelected(true);
			checkBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						graphFiltering.filters.put(key, false);
					} else if (e.getStateChange() == ItemEvent.SELECTED) {
						graphFiltering.filters.put(key, true);
					}
					updateNodeSizes();
					refresh();
				}
			});
			checkBox.setMargin(new Insets(5, 5, 5, 5));
		}

		return filterCheckBoxes;
	}

	@Override
	public void createView(Linker linker) {
		Map<Node, Set<Node>> graph = linker.createKnowledgeGraph();
		this.gephiGraph.createGephiGraph(graph);

		updateNodeSizes();
		initJFrame("Knowledge Graph for Repository \"" + ConfigPersistenceManager.getPathToGit() + "\"");
		refresh();
	}

	@Override
	public void createView(Node selectedNode, int distance, Linker linker) {
		Set<Node> nodes = linker.createLinks(selectedNode, distance);
		Map<Node, Set<Node>> graph = new HashMap<Node, Set<Node>>();
		for (Node n : nodes) {
			Set<Node> links = new HashSet<Node>();
			for (Node neighbor : n.getLinkedNodes()) {
				if (nodes.contains(neighbor)) {
					links.add(neighbor);
				}
			}
			graph.put(n, links);
		}
		this.gephiGraph.createGephiGraph(graph);

		selectedNodeId = selectedNode.getId();

		updateNodeSizes();
		initJFrame("Knowledge Graph for \"" + selectedNode.toString() + "\" with Link Distance " + distance);
		refresh();
	}

	private void initJFrame(String title) {
		JFrame frame = new JFrame(title);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(previewSketch, BorderLayout.CENTER);
		setOverlay(frame);
		frame.setSize(1600, 900);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				previewSketch.resetZoom();
			}
		});
		frame.setVisible(true);
	}

	private void highlightNodes(Node node, int currentDepth, int maxDepth, float size, Set<Node> visitedNodes) {
		for (Node linkedNode : node.getLinkedNodes()) {
			if (!visitedNodes.contains(linkedNode)) {
				org.gephi.graph.api.Node gephiNode = gephiGraph.getGephiNode(linkedNode);
				if (gephiNode != null) {
					gephiNode.setSize(size);
				}
				visitedNodes.add(linkedNode);
			}
		}
		if (currentDepth + 1 < maxDepth) {
			for (Node n : node.getLinkedNodes()) {
				highlightNodes(n, currentDepth + 1, maxDepth, size / ConfigPersistenceManager.getDecreaseFactor(),
						visitedNodes);
			}
		}
	}

	/**
	 * Resets the sizes of all nodes depending on the amount of links bound to the
	 * node.
	 * 
	 * @see updateNodeSize(Node node)
	 */
	public void updateNodeSizes() {
		if (selectedNodeId < 0) {
			for (org.gephi.graph.api.Node gephiNode : gephiGraph.getNodes()) {
				updateNodeSize(gephiNode);
			}
		} else {
			highlightSelectedNode();
		}
		refresh();
	}

	/**
	 * Resets the size of the given node.
	 * 
	 * @param gephiNode
	 *            The node which size should be reset
	 */
	public void updateNodeSize(org.gephi.graph.api.Node gephiNode) {
		Node node = Node.getNodeById(Long.valueOf(gephiNode.getId().toString()));
		// first - should the node be even visible?
		// inside the if/else:
		// is a filter active, which must be regarded?
		if (graphFiltering.shouldBeVisible(node)) {
			if (searchString == null || searchString.isEmpty()) {
				float size = (float) Math.sqrt(Double.valueOf(node.getLinkedNodes().size()));
				gephiNode.setSize((size > 0 ? size : 0.75f));
			} else {
				if (gephiNode.getLabel().toLowerCase().contains(searchString.toLowerCase())) {
					gephiNode.setSize(5f);
				} else {
					gephiNode.setSize(0.75f);
				}
			}
		} else {
			gephiNode.setSize(0f);
		}
	}

	private void refresh() {
		this.previewController.refreshPreview();
		this.previewSketch.refresh();
	}

	private void setOverlay(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(400, 1000));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		// Search-Field
		tfSearch = new JTextField();
		tfSearch.setSize(400, 40);
		tfSearch.setText("Search...");
		tfSearch.setVisible(true);
		tfSearch.setMargin(new Insets(5, 5, 5, 5));
		panel.add(tfSearch);

		// Search-Button
		JButton b = new JButton();
		b.setSize(400, 40);
		b.setText("Search");
		b.setVisible(true);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mouse Button Click Event
				searchString = tfSearch.getText();
				if (searchString == null || searchString.isEmpty()) {
					tfSearch.setText("Search...");
				}
				updateNodeSizes();
			}
		});
		b.setMargin(new Insets(5, 5, 5, 5));
		panel.add(b);

		// Combobox for Filter
		JPanel filter = new JPanel();
		filter.setLayout(new BoxLayout(filter, BoxLayout.PAGE_AXIS));
		JLabel label1 = new JLabel("Filter:");
		filter.add(label1);

		for (JCheckBox checkBox : filterCheckBoxes.values()) {
			filter.add(checkBox);
		}

		// Interaction
		tfInteraction = new JTextField();
		tfInteraction.setSize(400, 40);
		tfInteraction.setText("ID");
		tfInteraction.setVisible(true);
		tfInteraction.setMargin(new Insets(5, 5, 5, 5));
		panel.add(tfInteraction);
		// Highlight-Button
		JButton btnHighlight = new JButton();
		btnHighlight.setSize(400, 40);
		btnHighlight.setText("Highlight");
		btnHighlight.setVisible(true);
		btnHighlight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mouse Button Click Event
				try {
					String highlight = tfInteraction.getText();
					if (highlight == null || highlight.isEmpty()) {
						selectedNodeId = -1;
					} else {
						selectedNodeId = Long.parseLong(highlight);
					}
					updateNodeSizes();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		btnHighlight.setMargin(new Insets(5, 5, 5, 5));
		panel.add(btnHighlight);
		// JumpTo-Button
		JButton btnJumpTo = new JButton();
		btnJumpTo.setSize(400, 40);
		btnJumpTo.setText("Jump to");
		btnJumpTo.setVisible(true);
		btnJumpTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mouse Button Click Event
				try {
					long id = Long.parseLong(tfInteraction.getText());
					if (id > 0) {
						org.gephi.graph.api.Node n = gephiGraph.getGephiNode(tfInteraction.getText());
						if (n != null) {
							de.uhd.ifi.se.decision.management.eclipse.model.Node iN = de.uhd.ifi.se.decision.management.eclipse.model.Node
									.getNodeById(id);
							if (iN instanceof JiraIssueImpl) {
								JiraIssue ji = (JiraIssue) iN;
								OpenWebbrowser.openWebpage(ji);
							}
						}
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		btnJumpTo.setMargin(new Insets(5, 5, 5, 5));
		panel.add(btnJumpTo);
		// Reset-Button
		JButton btnReset = new JButton();
		btnReset.setSize(400, 40);
		btnReset.setText("Reset");
		btnReset.setVisible(true);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mouse Button Click Event
				resetFilters();
				updateNodeSizes();
			}
		});
		btnReset.setMargin(new Insets(5, 5, 5, 5));
		panel.add(btnReset);
		panel.add(filter);
		frame.add(panel, BorderLayout.WEST);
	}

	private void resetFilters() {
		if (tfSearch != null)
			tfSearch.setToolTipText("Search...");
		if (tfInteraction != null)
			tfInteraction.setText("ID");
		resetFilterCheckboxes();
		searchString = "";
		selectedNodeId = -1;
		graphFiltering = new GraphFiltering();
	}

	private void resetFilterCheckboxes() {
		for (JCheckBox checkBox : filterCheckBoxes.values()) {
			checkBox.setSelected(true);
		}
	}

	private void highlightSelectedNode(Node node) {
		if (node == null) {
			return;
		}
		for (org.gephi.graph.api.Node gephiNode : gephiGraph.getNodes()) {
			gephiNode.setSize(0f);
		}
		Set<Node> visitedNodes = new HashSet<Node>();
		gephiGraph.getGephiNode(selectedNodeId).setSize(15f);
		visitedNodes.add(node);
		highlightNodes(node, 1, ConfigPersistenceManager.getLinkDistance(),
				15f / ConfigPersistenceManager.getDecreaseFactor(), visitedNodes);
	}

	private void highlightSelectedNode() {
		Node node = Node.getNodeById(selectedNodeId);
		highlightSelectedNode(node);
	}
}
