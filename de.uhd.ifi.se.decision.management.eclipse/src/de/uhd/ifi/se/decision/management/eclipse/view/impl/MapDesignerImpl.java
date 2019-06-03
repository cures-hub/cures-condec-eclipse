package de.uhd.ifi.se.decision.management.eclipse.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
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

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.OpenWebbrowser;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.GitCommitImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.persistence.MapDesignerSettingsProvider;
import de.uhd.ifi.se.decision.management.eclipse.view.MapDesigner;
import de.uhd.ifi.se.decision.management.eclipse.view.PreviewSketch;

public class MapDesignerImpl implements MapDesigner {
	private String searchString = "";
	private long interactionID = -1;
	private boolean bShowCommits = true;
	private boolean bShowKnowledgeItems = true;
	private boolean bShowKIDecision = true;
	private boolean bShowKIIssue = true;
	private boolean bShowKIAlternative = true;
	private boolean bShowKICon = true;
	private boolean bShowKIPro = true;
	private boolean bShowKIGoal = true;
	private boolean bShowKIOther = true;
	private boolean bShowIssues = true;
	private boolean bShowMethods = true;
	private boolean bShowFiles = true;
	private boolean bShowCFClasses = true;
	private boolean bShowCFOther = true;
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
	private Map<de.uhd.ifi.se.decision.management.eclipse.model.Node, Set<de.uhd.ifi.se.decision.management.eclipse.model.Node>> map;
	private ProjectController projectController;
	private Workspace workspace;
	private GraphModel graphModel;
	private DirectedGraph directedGraph;
	private GraphView graphView;
	private PreviewController previewController;
	private G2DTarget target;
	private final PreviewSketch previewSketch;
	private JFrame frame;
	private PreviewModel previewModel;
	private Linker linker = null;

	public MapDesignerImpl() {
		this.projectController = Lookup.getDefault().lookup(ProjectController.class);
		this.projectController.newProject();
		this.workspace = projectController.getCurrentWorkspace();
		this.graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		this.directedGraph = graphModel.getDirectedGraph();
		this.previewController = Lookup.getDefault().lookup(PreviewController.class);
		this.target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		this.previewSketch = new PreviewSketch(target);
		this.graphView = graphModel.getGraph().getView();
		this.graphModel.setVisibleView(graphView);
		this.previewModel = previewController.getModel();
		this.previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		this.previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(Color.WHITE));
		this.previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		this.previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
		this.previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.BLACK);
	}

	@Override
	public void createFullMap(Linker linker) {
		if (this.linker == null) {
			this.linker = linker;
		}
		this.map = linker.createKnowledgeGraph();
		generateGraph(map.keySet());
		MapDesignerSettingsProvider.getLayoutType().generateLayout(graphModel, map.size());
		resetFilters();
		initJFrame("Full map for Repository \"" + ConfigPersistenceManager.getPathToGit() + "\"");
		refresh();
	}

	@Override
	public void createSelectedMap(de.uhd.ifi.se.decision.management.eclipse.model.Node rootNode, int depth,
			Linker linker) {
		if (this.linker == null) {
			this.linker = linker;
		}
		Set<de.uhd.ifi.se.decision.management.eclipse.model.Node> nodes = linker.createLinks(rootNode, depth);
		if (map != null) {
			map.clear();
		} else {
			map = new HashMap<de.uhd.ifi.se.decision.management.eclipse.model.Node, Set<de.uhd.ifi.se.decision.management.eclipse.model.Node>>();
		}
		for (de.uhd.ifi.se.decision.management.eclipse.model.Node n : nodes) {
			Set<de.uhd.ifi.se.decision.management.eclipse.model.Node> links = new HashSet<de.uhd.ifi.se.decision.management.eclipse.model.Node>();
			for (de.uhd.ifi.se.decision.management.eclipse.model.Node neighbor : n.getLinkedNodes()) {
				if (nodes.contains(neighbor)) {
					links.add(neighbor);
				}
			}
			map.put(n, links);
		}
		generateGraph(nodes);
		MapDesignerSettingsProvider.getLayoutType().generateLayout(graphModel, map.size());
		resetFilters();
		initJFrame("Current Map for \"" + rootNode.toString() + "\" with range " + depth);
		refresh();
	}

	private void initJFrame(String title) {
		// If frame is already initialized - destroy it and create new frame
		if (this.frame != null && this.frame.isActive()) {
			this.frame.dispose();
		}
		// Add the applet to a JFrame and display
		this.frame = new JFrame(title);
		this.frame.setLayout(new BorderLayout());

		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.add(previewSketch, BorderLayout.CENTER);

		setOverlay(this.frame);

		this.frame.setSize(1600, 900);

		// Wait for the frame to be visible before painting, or the result drawing will
		// be strange
		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				previewSketch.resetZoom();
			}
		});
		this.frame.setVisible(true);
	}

	private void generateGraph(Set<de.uhd.ifi.se.decision.management.eclipse.model.Node> nodes) {
		// first, check load existing node-IDs
		// for preventing errors when re-creating a graph
		directedGraph.clear();
		directedGraph.clearEdges();
		// second, create nodes...
		for (de.uhd.ifi.se.decision.management.eclipse.model.Node node : nodes) {
			Node gephiNode = createNode(node);
			setPosition(gephiNode, nodes.size());
			directedGraph.addNode(gephiNode);
		}
		updateNodeSizes();
		// third, create edges...
		for (Map.Entry<de.uhd.ifi.se.decision.management.eclipse.model.Node, Set<de.uhd.ifi.se.decision.management.eclipse.model.Node>> entry : map
				.entrySet()) {
			for (de.uhd.ifi.se.decision.management.eclipse.model.Node n : entry.getValue()) {
				try {
					Edge e = graphModel.factory().newEdge(directedGraph.getNode(String.valueOf(entry.getKey().getId())),
							directedGraph.getNode(String.valueOf(n.getId())), 0, 1.0, true);
					directedGraph.addEdge(e);
				} catch (Exception ex) {
					if (entry.getKey() == null) {
						System.out.println(
								"NullPointerException in generateGraph(): Linker added a null-object to the Node-List.");
					}
					if (n == null) {
						System.out.println(
								"NullPointerException in generateGraph(): Linker added a null-object as a linked Node.");
					}
					if (n != null && entry.getKey() != null) {
						System.out.println(
								"Failed to link \"" + entry.getKey().toString() + "\" with \"" + n.toString() + "\"");
					}
					System.out.println("Error-Message: " + ex.getMessage());
				}
			}
		}
	}

	private Node createNode(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
		Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
		gephiNode.setLabel("[" + String.valueOf(node.getId()) + "] " + node.toString());
		if (node instanceof GitCommitImpl) {
			gephiNode.setColor(MapDesignerSettingsProvider.getCommitColor());
		} else if (node instanceof DecisionKnowledgeElementImpl) {
			gephiNode.setColor(MapDesignerSettingsProvider.getDecisionKnowledgeElementColor());
		} else if (node instanceof JiraIssueImpl) {
			gephiNode.setColor(MapDesignerSettingsProvider.getIssueColor());
		} else if (node instanceof CodeClassImpl) {
			gephiNode.setColor(MapDesignerSettingsProvider.getChangedFilesColor());
		} else if (node instanceof CodeMethodImpl) {
			gephiNode.setColor(MapDesignerSettingsProvider.getCodeMethodColor());
		} else {
			gephiNode.setColor(Color.PINK);
		}
		return gephiNode;
	}

	void setPosition(Node gephiNode, int numberOfNodes) {
		gephiNode.setX((float) Math.random() * 100f * (float) Math.sqrt(numberOfNodes));
		gephiNode.setY((float) Math.random() * 100f * (float) Math.sqrt(numberOfNodes));
	}

	private boolean shouldBeVisible(de.uhd.ifi.se.decision.management.eclipse.model.Node node) {
		if (node instanceof GitCommitImpl && bShowCommits || node instanceof JiraIssueImpl && bShowIssues
				|| node instanceof CodeMethodImpl && bShowMethods) {
			return true;
		} else if (node instanceof DecisionKnowledgeElementImpl && bShowKnowledgeItems) {
			DecisionKnowledgeElementImpl dke = (DecisionKnowledgeElementImpl) node;
			switch (dke.getKnowledgeType()) {
			case ALTERNATIVE:
				if (bShowKIAlternative)
					return true;
				break;
			case CON:
				if (bShowKICon)
					return true;
				break;
			case DECISION:
				if (bShowKIDecision)
					return true;
				break;
			case GOAL:
				if (bShowKIGoal)
					return true;
				break;
			case ISSUE:
				if (bShowKIIssue)
					return true;
				break;
			case PRO:
				if (bShowKIPro)
					return true;
				break;
			case OTHER:
				if (bShowKIOther)
					return true;
				break;
			default:
				return true;
			}
			return false;
		} else if (node instanceof CodeClassImpl && bShowFiles) {
			CodeClass cc = (CodeClass) node;
			if (cc.getPath().getFileExtension().equalsIgnoreCase("java")) {
				if (bShowCFClasses) {
					return true;
				} else {
					return false;
				}
			} else {
				if (bShowCFOther) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * Resets the size of the given node.
	 * 
	 * @param node
	 *            The node which size should be reset
	 */
	private void updateNodeSize(Node node) {
		de.uhd.ifi.se.decision.management.eclipse.model.Node iN = de.uhd.ifi.se.decision.management.eclipse.model.Node
				.getNodeById(Long.valueOf(node.getId().toString()));
		// first - should the node be even visible?
		// inside the if/else:
		// is a filter active, which must be regarded?
		if (shouldBeVisible(iN)) {
			if (searchString == null || searchString.isEmpty()) {
				float size = (float) Math.sqrt(Double.valueOf(iN.getLinkedNodes().size()));
				node.setSize((size > 0 ? size : 0.75f));
			} else {
				if (node.getLabel().toLowerCase().contains(searchString.toLowerCase())) {
					node.setSize(5f);
				} else {
					node.setSize(0.75f);
				}
			}
		} else {
			node.setSize(0f);
		}
	}

	private void highlightGraph() {
		de.uhd.ifi.se.decision.management.eclipse.model.Node inode = de.uhd.ifi.se.decision.management.eclipse.model.Node
				.getNodeById(interactionID);
		if (inode != null) {
			for (Node n : directedGraph.getNodes()) {
				n.setSize(0f);
			}
			Set<de.uhd.ifi.se.decision.management.eclipse.model.Node> visitedNodes = new HashSet<de.uhd.ifi.se.decision.management.eclipse.model.Node>();
			directedGraph.getNode(String.valueOf(interactionID)).setSize(15f);
			visitedNodes.add(inode);
			highlightNodes(inode, 1, ConfigPersistenceManager.getLinkDistance(),
					15f / ConfigPersistenceManager.getDecreaseFactor(), visitedNodes);
		}
	}

	private void highlightNodes(de.uhd.ifi.se.decision.management.eclipse.model.Node node, int currentDepth,
			int maxDepth, float size, Set<de.uhd.ifi.se.decision.management.eclipse.model.Node> visitedNodes) {
		for (de.uhd.ifi.se.decision.management.eclipse.model.Node n : node.getLinkedNodes()) {
			if (!visitedNodes.contains(n)) {
				directedGraph.getNode(String.valueOf(n.getId())).setSize(size);
				visitedNodes.add(n);
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
	 * Resets the sizes of all nodes depending on the amount of links binded to the
	 * node.
	 * 
	 * @see updateNodeSize(Node node)
	 */
	private void updateNodeSizes() {
		if (interactionID < 0) {
			for (Node gephiNode : directedGraph.getNodes()) {
				updateNodeSize(gephiNode);
			}
		} else {
			highlightGraph();
		}
		refresh();
	}

	private void refresh() {
		this.previewController.refreshPreview();
		this.target.refresh();
		this.previewSketch.refresh();
	}

	// TODO - Make the arrangement of the components more beautiful
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
					bShowCommits = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowCommits = true;
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
					bShowIssues = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowIssues = true;
				}
				updateNodeSizes();
				refresh();
			}
		});
		fIssues.setMargin(new Insets(5, 5, 5, 5));
		filter.add(fIssues);
		// Filter: DecisionKnowledgeItems
		fKnowledgeItems = new JCheckBox("Decision-Knowledge Items");
		fKnowledgeItems.setSelected(true);
		fKnowledgeItems.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					bShowKnowledgeItems = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowKnowledgeItems = true;
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
					bShowKIDecision = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowKIDecision = true;
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
					bShowKIIssue = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowKIIssue = true;
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
					bShowKICon = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowKICon = true;
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
					bShowKIPro = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowKIPro = true;
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
					bShowKIOther = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowKIOther = true;
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
					bShowFiles = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowFiles = true;
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
					bShowCFClasses = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowCFClasses = true;
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
					bShowCFOther = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowCFOther = true;
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
					bShowMethods = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					bShowMethods = true;
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
						interactionID = -1;
					} else {
						interactionID = Long.parseLong(highlight);
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
						Node n = directedGraph.getNode(tfInteraction.getText());
						if (n != null) {
							de.uhd.ifi.se.decision.management.eclipse.model.Node iN = de.uhd.ifi.se.decision.management.eclipse.model.Node
									.getNodeById(id);
							if (iN instanceof JiraIssueImpl) {
								JiraIssue ji = (JiraIssue) iN;
								OpenWebbrowser.openWebpage(new URI(extractIssueUri(ji)));
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
			tfSearch.setText("Search...");
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
		interactionID = -1;
		bShowCommits = true;
		bShowKnowledgeItems = true;
		bShowKIDecision = true;
		bShowKIIssue = true;
		bShowKIAlternative = true;
		bShowKICon = true;
		bShowKIPro = true;
		bShowKIGoal = true;
		bShowKIOther = true;
		bShowIssues = true;
		bShowMethods = true;
		bShowFiles = true;
		bShowCFClasses = true;
		bShowCFOther = true;
	}

	private String extractIssueUri(JiraIssue issue) {
		String fullRestApiUri = issue.getJiraIssue().getSelf().toString();
		String[] uriSplits = fullRestApiUri.split("/");
		String uri = "";
		for (String s : uriSplits) {
			if (s.equals("rest")) {
				uri += "projects/" + issue.getJiraIssue().getProject().getKey() + "/issues/"
						+ issue.getJiraIssue().getKey();
				return uri;
			} else {
				uri += s + "/";
			}
		}
		return "";
	}
}