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
	private JCheckBox fClasses;
	private JCheckBox fCFClasses;
	private JCheckBox fCFOther;
	private JCheckBox fCodeMethods;
	private JCheckBox fCommit;
	private JCheckBox fFeature;
	private JCheckBox fIssues;
	private JCheckBox fKICon;
	private JCheckBox fKIDecision;
	private JCheckBox fKIIssue;
	private JCheckBox fKIOther;
	private JCheckBox fKIPro;
	private JCheckBox fKnowledgeItems;
	private PreviewController previewController;
	private PreviewSketch previewSketch;
	private Linker linker = null;
	public GraphFiltering graphFiltering;
	public GephiGraph gephiGraph;

	public KnowledgeGraphViewImpl() {
		this.gephiGraph = new GephiGraph();

		this.previewController = Lookup.getDefault().lookup(PreviewController.class);
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		this.previewSketch = new PreviewSketch(target);

		GraphSettings.initPreviewModel(previewController);
		this.graphFiltering = new GraphFiltering();
	}

	@Override
	public void createView(Linker linker) {
		if (this.linker == null) {
			this.linker = linker;
		}
		Map<Node, Set<Node>> graph = linker.createKnowledgeGraph();
		this.gephiGraph.createGephiGraph(graph);

		updateNodeSizes();
		resetFilters();
		initJFrame("Knowledge Graph for Repository \"" + ConfigPersistenceManager.getPathToGit() + "\"");
		refresh();
	}

	@Override
	public void createView(Node rootNode, int depth, Linker linker) {
		if (this.linker == null) {
			this.linker = linker;
		}
		Set<Node> nodes = linker.createLinks(rootNode, depth);
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

		updateNodeSizes();
		resetFilters();
		initJFrame("Knowledge Graph for \"" + rootNode.toString() + "\" with Link Distance " + depth);
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

	private void highlightSelectedNode(Node node) {
		if (node == null) {
			return;
		}
		for (org.gephi.graph.api.Node gephiNode : gephiGraph.directedGraph.getNodes()) {
			gephiNode.setSize(0f);
		}
		Set<Node> visitedNodes = new HashSet<Node>();
		gephiGraph.directedGraph.getNode(String.valueOf(selectedNodeId)).setSize(15f);
		visitedNodes.add(node);
		highlightNodes(node, 1, ConfigPersistenceManager.getLinkDistance(),
				15f / ConfigPersistenceManager.getDecreaseFactor(), visitedNodes);
	}

	private void highlightSelectedNode() {
		Node node = Node.getNodeById(selectedNodeId);
		highlightSelectedNode(node);
	}

	private void highlightNodes(Node node, int currentDepth, int maxDepth, float size, Set<Node> visitedNodes) {
		for (Node linkedNode : node.getLinkedNodes()) {
			if (!visitedNodes.contains(linkedNode)) {
				gephiGraph.directedGraph.getNode(String.valueOf(linkedNode.getId())).setSize(size);
				visitedNodes.add(linkedNode);
			}
		}
		if (currentDepth + 1 < maxDepth) {
			for (de.uhd.ifi.se.decision.management.eclipse.model.Node n : node.getLinkedNodes()) {
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
			for (org.gephi.graph.api.Node gephiNode : gephiGraph.directedGraph.getNodes()) {
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
		if (graphFiltering.shouldBeVisible(this, node)) {
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
		// Filter: Commits
		fCommit = new JCheckBox("Commits");
		fCommit.setSelected(true);
		fCommit.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowCommits = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowCommits = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fCommit.setMargin(new Insets(5, 5, 5, 5));
		filter.add(fCommit);
		// Filter: Jira-Issues
		fIssues = new JCheckBox("Jira Issues");
		fIssues.setSelected(true);
		fIssues.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowIssues = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowIssues = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fIssues.setMargin(new Insets(5, 5, 5, 5));
		filter.add(fIssues);
		// Filter: DecisionKnowledgeItems
		fKnowledgeItems = new JCheckBox("Decision Knowledge");
		fKnowledgeItems.setSelected(true);
		fKnowledgeItems.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowDecisionKnowledge = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowDecisionKnowledge = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fKnowledgeItems.setMargin(new Insets(5, 5, 1, 5));
		filter.add(fKnowledgeItems);
		// Filter: DecisionKnowledgeItems -> Decisions
		fKIDecision = new JCheckBox("Decisions");
		fKIDecision.setSelected(true);
		fKIDecision.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowKIDecision = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowKIDecision = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fKIDecision.setMargin(new Insets(1, 15, 1, 5));
		filter.add(fKIDecision);
		// Filter: DecisionKnowledgeItems -> Issue
		fKIIssue = new JCheckBox("Issues");
		fKIIssue.setSelected(true);
		fKIIssue.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowKIIssue = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowKIIssue = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fKIIssue.setMargin(new Insets(1, 15, 1, 5));
		filter.add(fKIIssue);
		// Filter: DecisionKnowledgeItems -> Con
		fKICon = new JCheckBox("Cons");
		fKICon.setSelected(true);
		fKICon.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowKICon = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowKICon = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fKICon.setMargin(new Insets(1, 15, 1, 5));
		filter.add(fKICon);
		// Filter: DecisionKnowledgeItems -> Pro
		fKIPro = new JCheckBox("Pros");
		fKIPro.setSelected(true);
		fKIPro.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowKIPro = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowKIPro = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fKIPro.setMargin(new Insets(1, 15, 1, 5));
		filter.add(fKIPro);
		// Filter: DecisionKnowledgeItems -> Other
		fKIOther = new JCheckBox("Other");
		fKIOther.setSelected(true);
		fKIOther.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowKIOther = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowKIOther = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fKIOther.setMargin(new Insets(1, 15, 5, 5));
		filter.add(fKIOther);
		// Filter: Changed Files
		fClasses = new JCheckBox("Changed Files");
		fClasses.setSelected(true);
		fClasses.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowFiles = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowFiles = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fClasses.setMargin(new Insets(5, 5, 5, 5));
		filter.add(fClasses);
		// Filter: Changed Files -> Classes
		fCFClasses = new JCheckBox("Changed Classes");
		fCFClasses.setSelected(true);
		fCFClasses.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowCFClasses = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowCFClasses = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fCFClasses.setMargin(new Insets(1, 15, 1, 5));
		filter.add(fCFClasses);
		// Filter: Changed Files -> Other
		fCFOther = new JCheckBox("Other");
		fCFOther.setSelected(true);
		fCFOther.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowCFOther = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowCFOther = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fCFOther.setMargin(new Insets(1, 15, 5, 5));
		filter.add(fCFOther);
		// Filter: Coded Methods
		fCodeMethods = new JCheckBox("Coded Methods");
		fCodeMethods.setSelected(true);
		fCodeMethods.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					graphFiltering.bShowMethods = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					graphFiltering.bShowMethods = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fCodeMethods.setMargin(new Insets(5, 5, 5, 5));
		filter.add(fCodeMethods);

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
						org.gephi.graph.api.Node n = gephiGraph.directedGraph.getNode(tfInteraction.getText());
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
		if (fClasses != null)
			fClasses.setSelected(true);
		if (fCFClasses != null)
			fCFClasses.setSelected(true);
		if (fCFOther != null)
			fCFOther.setSelected(true);
		if (fCodeMethods != null)
			fCodeMethods.setSelected(true);
		if (fCommit != null)
			fCommit.setSelected(true);
		if (fFeature != null)
			fFeature.setSelected(true);
		if (fIssues != null)
			fIssues.setSelected(true);
		if (fKICon != null)
			fKICon.setSelected(true);
		if (fKIDecision != null)
			fKIDecision.setSelected(true);
		if (fKIIssue != null)
			fKIIssue.setSelected(true);
		if (fKIOther != null)
			fKIOther.setSelected(true);
		if (fKIPro != null)
			fKIPro.setSelected(true);
		if (fKnowledgeItems != null)
			fKnowledgeItems.setSelected(true);
		searchString = "";
		selectedNodeId = -1;
		graphFiltering = new GraphFiltering();
	}
}
