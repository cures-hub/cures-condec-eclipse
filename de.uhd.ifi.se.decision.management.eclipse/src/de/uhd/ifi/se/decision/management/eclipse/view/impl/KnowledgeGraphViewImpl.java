package de.uhd.ifi.se.decision.management.eclipse.view.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.RenderTarget;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.event.JumpToUtils;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.persistence.GraphSettings;
import de.uhd.ifi.se.decision.management.eclipse.persistence.KnowledgePersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.Filter;
import de.uhd.ifi.se.decision.management.eclipse.view.GephiGraph;
import de.uhd.ifi.se.decision.management.eclipse.view.GraphFiltering;
import de.uhd.ifi.se.decision.management.eclipse.view.HintTextField;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.LayoutType;
import de.uhd.ifi.se.decision.management.eclipse.view.PreviewSketch;

/**
 * Class to create a view for the knowledge graph model class.
 */
public class KnowledgeGraphViewImpl implements KnowledgeGraphView {
	private static KnowledgeGraphView knowledgeGraphView = null;

	private JFrame frame;

	// Search Node
	private String searchString;
	private JTextField searchTextField;

	// Enter Node ID
	private long selectedNodeId = -1;
	private JTextField selectedNodeTextField;

	// Select Distance
	private int linkDistance;
	private JSpinner distanceSpinner;

	// Create Node
	private JDialog createNode;
	private JPanel createNodePanel;
	private JTextField enterSummaryTextField;
	private JTextField enterDescriptionTextField;
	private JComboBox<String> decisionTypesComboBox;

	private PreviewController previewController;
	private PreviewSketch previewSketch;
	private GraphFiltering graphFiltering;
	private GephiGraph gephiGraph;

	private float decreaseFactor;

	private KnowledgeGraphViewImpl() {
		this(KnowledgeGraphImpl.getInstance(), "Knowledge Graph");
	}

	private KnowledgeGraphViewImpl(KnowledgeGraph graph) {
		this(graph, "Knowledge Graph");
	}

	/**
	 * Creates an overview of the knowledge in the knowledge graph. The knowledge
	 * covers decision knowledge, JIRA issues such as requirements and work items,
	 * commits, and files (e.g., classes and methods).
	 * 
	 * @see KnowledgeGraph
	 * @param graph
	 *            graph of commits, changed files, decision knowledge, and JIRA
	 *            issues such as requirements and work items.
	 * @param frameTitle
	 *            frame title of the view.
	 */
	private KnowledgeGraphViewImpl(KnowledgeGraph graph, String frameTitle) {

		this.gephiGraph = new GephiGraphImpl(graph);

		this.previewController = Lookup.getDefault().lookup(PreviewController.class);
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		this.previewSketch = new PreviewSketch(target, this.gephiGraph.getWorkspace());

		GraphSettings.initPreviewModel(previewController);
		this.graphFiltering = new GraphFiltering();
		for (Filter filter : graphFiltering.filters.values()) {
			filter.getCheckBox().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						filter.setActivated(false);
					}
					else if (e.getStateChange() == ItemEvent.SELECTED) {
						filter.setActivated(true);
					}
					updateNodeSizes();
					refresh();
				}
			});
		}

		this.linkDistance = ConfigPersistenceManager.getLinkDistance();
		this.decreaseFactor = ConfigPersistenceManager.getDecreaseFactor();
		this.searchString = "";
		createView(frameTitle);
	}

	/**
	 * Returns the instance of KnowledgeGraphView.
	 * 
	 * @return the instance of the knowledge graph view
	 */
	public static KnowledgeGraphView getInstance(KnowledgeGraph graph, String frameTitle) {
		if (knowledgeGraphView == null) {
			knowledgeGraphView = new KnowledgeGraphViewImpl(graph, frameTitle);
		}
		return knowledgeGraphView;
	}

	/**
	 * Returns the instance of KnowledgeGraphView.
	 * If no KnowledgeGraphView exists, return a new one.
	 * 
	 * 
	 * @return the instance of the knowledge graph view
	 */
	public static KnowledgeGraphView getInstance() {
		if (knowledgeGraphView == null) {
			knowledgeGraphView = new KnowledgeGraphViewImpl();
		}
		return knowledgeGraphView;
	}

	/**
	 * Returns the instance of KnowledgeGraphView.
	 * If no KnowledgeGraphView exists, create a new one.
	 * 
	 * @return the instance of the knowledge graph view
	 */
	public static KnowledgeGraphView getInstance(KnowledgeGraph graph) {
		if (knowledgeGraphView == null) {
			knowledgeGraphView = new KnowledgeGraphViewImpl(graph);
		}
		return knowledgeGraphView;
	}

	public static void clear() {
		knowledgeGraphView = null;
	}

	@Override
	public void update(KnowledgeGraph graph) {
		this.gephiGraph = new GephiGraphImpl(graph);

		GraphSettings.initPreviewModel(previewController);

		updateNodeSizes();
		refresh();
	}

	private void createView(String frameTitle) {
		updateNodeSizes();
		initJFrame(frameTitle);
		refresh();
	}

	private void initJFrame(String title) {
		frame = new JFrame(title);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(initJPanel(), BorderLayout.WEST);
		frame.add(previewSketch, BorderLayout.CENTER);
		frame.setSize(1600, 900);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				previewSketch.resetZoom();
			}
		});
		frame.setVisible(true);
		frame.requestFocus();
	}

	private JPanel initJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Search Field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 0, 10);
		searchTextField = createHintTextField("Search...");
		panel.add(searchTextField, c);

		// Search Button
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 5, 10);
		panel.add(createSearchButton(), c);

		// Select Node Field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 10, 0, 10);
		selectedNodeTextField = createHintTextField("Enter ID...");
		panel.add(selectedNodeTextField, c);

		// Highlight Node Button
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(0, 10, 5, 0);
		panel.add(createHighlightNodeButton(), c);

		// JumpTo Button
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(0, 0, 5, 10);
		panel.add(createJumpToButton(), c);

		// Filters
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 10, 0, 10);
		panel.add(createFilterPanel(), c);

		// Reset FilterButton
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(0, 10, 5, 10);
		panel.add(createResetButton(), c);

		// Show Full Graph Button
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 10, 5, 10);
		panel.add(createShowFullGraphButton(), c);

		// Select Distance Field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(5, 10, 5, 0);
		distanceSpinner = new JSpinner(new SpinnerNumberModel(ConfigPersistenceManager.getLinkDistance(), 0, null, 1));
		JComponent editor = distanceSpinner.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		panel.add(distanceSpinner, c);

		// Select Distance Button
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 7;
		c.insets = new Insets(5, 0, 5, 10);
		panel.add(createDistanceButton(), c);

		// Layout Type Combo Box
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 8;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(5, 10, 10, 10);
		panel.add(createLayoutPanel(), c);

		return panel;
	}

	private JButton createSearchButton() {
		JButton searchButton = createButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchString = searchTextField.getText();
				if (searchString == null || searchString.isEmpty()) {
					searchTextField.setText(null);
				}
				updateNodeSizes();
			}
		});

		return searchButton;
	}

	private JButton createJumpToButton() {
		JButton jumpToButton = createButton("Jump to");
		jumpToButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					selectedNodeId = Long.parseLong(selectedNodeTextField.getText());
				}
				catch (Exception ex) {
					selectedNodeId = -1;
				}
				if (selectedNodeId < 0) {
					return;
				}
				Node node = Node.getNodeById(selectedNodeId);
				if (node == null) {
					return;
				}
				else {
					JumpToUtils.jumpTo(node);
				}
			}
		});

		return jumpToButton;
	}

	private JButton createHighlightNodeButton() {
		JButton selectedNodeButton = createButton("Highlight");
		selectedNodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String highlight = selectedNodeTextField.getText();
				if (highlight == null || highlight.isEmpty()) {
					selectedNodeId = -1;
					return;
				}
				try {
					selectedNodeId = Long.parseLong(highlight);
				}
				catch (Exception ex) {
					selectedNodeId = -1;
				}
				updateNodeSizes();
			}
		});

		return selectedNodeButton;
	}

	private JButton createResetButton() {
		JButton resetButton = createButton("Reset Filter");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetFilters();
				updateNodeSizes();
			}
		});

		return resetButton;
	}

	private JButton createShowFullGraphButton() {
		JButton showFullGraphButtonButton = createButton("Show Full Graph");
		showFullGraphButtonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				KnowledgeGraphImpl.clear();
				KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance();
				knowledgeGraph.updateWithPersistanceData();
				KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
				knowledgeGraphView.update(knowledgeGraph);
			}
		});

		return showFullGraphButtonButton;
	}

	private JButton createDistanceButton() {
		JButton distanceButton = createButton("<html>" + "Change" + "<br>" + "Distance" + "</html>");
		distanceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					linkDistance = (Integer) distanceSpinner.getValue();
					KnowledgeGraph knowledgeGraphOld = KnowledgeGraphImpl.getInstance();
					Node startNode = knowledgeGraphOld.getStartNode();

					if (startNode != null) {
						KnowledgeGraphImpl.clear();
						KnowledgeGraph knowledgeGraph = KnowledgeGraphImpl.getInstance(startNode, linkDistance);
						knowledgeGraph.updateWithPersistanceData();
						KnowledgeGraphView knowledgeGraphView = KnowledgeGraphViewImpl.getInstance();
						knowledgeGraphView.update(knowledgeGraph);
					}
				}
				catch (Exception ex) {
					linkDistance = ConfigPersistenceManager.getLinkDistance();
				}
			}
		});

		return distanceButton;
	}

	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel label = new JLabel("Filters:");
		filterPanel.add(label);
		for (Filter filter : graphFiltering.filters.values()) {
			filterPanel.add(filter.getCheckBox());
		}

		return filterPanel;
	}

	private JTextField createHintTextField(String hint) {
		JTextField textField = new HintTextField(hint);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);

		return textField;
	}

	private JButton createButton(String text) {
		JButton button = new JButton();
		button.setText(text);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);

		return button;
	}

	private JPanel createLayoutPanel() {
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel label = new JLabel("Layout:");
		layoutPanel.add(label);
		layoutPanel.add(createLayoutTypeComboBox());

		return layoutPanel;
	}

	private JComboBox<LayoutType> createLayoutTypeComboBox() {
		JComboBox<LayoutType> layoutTypeComboBox = new JComboBox<LayoutType>(LayoutType.values());
		layoutTypeComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		return layoutTypeComboBox;
	}

	private void refresh() {
		this.previewController.refreshPreview();
		this.previewSketch.refresh();
		this.previewSketch.refreshWorkspace(this.gephiGraph.getWorkspace());
	}

	@Override
	public boolean resetFilters() {
		searchString = "";
		searchTextField.setText(null);

		selectedNodeId = -1;
		selectedNodeTextField.setText(null);

		resetFilterCheckboxes();

		return true;
	}

	private void resetFilterCheckboxes() {
		for (Filter filter : graphFiltering.filters.values()) {
			filter.getCheckBox().setSelected(true);
		}
	}

	/**
	 * Resets the sizes of all nodes depending on their amount of links.
	 */
	private void updateNodeSizes() {
		if (selectedNodeId < 0) {
			for (org.gephi.graph.api.Node gephiNode : gephiGraph.getNodes()) {
				updateNodeSize(gephiNode);
			}
		}
		else {
			highlightSelectedNode();
		}
		refresh();
	}

	/**
	 * Resets the size of the given node depending on its amount of links.
	 * 
	 * @param gephiNode
	 *            node which size should be set.
	 */
	private void updateNodeSize(org.gephi.graph.api.Node gephiNode) {
		Node node = Node.getNodeById(Long.valueOf(gephiNode.getId().toString()));

		if (!graphFiltering.shouldBeVisible(node)) {
			gephiNode.setSize(0f);
			return;
		}

		float size = getNodeSizeByNodeDegree(node);

		if (!searchString.isEmpty() && gephiNode.getLabel().contains(searchString)) {
			gephiNode.setSize(size > 0 ? size * 3 : 0.75f);
			return;
		}

		gephiNode.setSize(size > 0 ? size : 0.75f);
	}

	private float getNodeSizeByNodeDegree(Node node) {
		int degree = node.getLinkedNodes().size();
		return (float) Math.sqrt(degree) * 2;
	}

	@Override
	public boolean highlightSelectedNode() {
		Node node = Node.getNodeById(selectedNodeId);
		return highlightNode(node);
	}

	@Override
	public boolean highlightNode(Node node) {
		if (node == null) {
			return false;
		}

		Set<Node> visitedNodes = new HashSet<Node>();
		visitedNodes.add(node);

		gephiGraph.setSizeOfAllNodes(0f);
		gephiGraph.setSizeOfNode(selectedNodeId, 5f);

		highlightNode(node, 1, linkDistance, 5f / decreaseFactor, visitedNodes);

		return true;
	}

	private void highlightNode(Node node, int currentDepth, int maxDepth, float size, Set<Node> visitedNodes) {
		for (Node linkedNode : node.getLinkedNodes()) {
			if (!visitedNodes.contains(linkedNode)) {
				if (graphFiltering.shouldBeVisible(linkedNode)) {
					gephiGraph.setSizeOfNode(linkedNode, size);
				}
				visitedNodes.add(linkedNode);
			}
		}
		if (currentDepth + 1 < maxDepth) {
			for (Node n : node.getLinkedNodes()) {
				highlightNode(n, currentDepth + 1, maxDepth, size / decreaseFactor, visitedNodes);
			}
		}
	}

	@Override
	public boolean highlightSelectedNodeAndUpdate(Node selectedNode) {
		selectedNodeId = selectedNode.getId();
		highlightSelectedNode();
		refresh();

		return true;
	}

	@Override
	public boolean createNode() {
		createNode = new JDialog(frame, "Create Decision Knowledge");
		createNode.getContentPane().setLayout(new BoxLayout(createNode.getContentPane(), BoxLayout.PAGE_AXIS));

		createNodePanel = new JPanel();
		createNodePanel.setLayout(new GridLayout(5, 1));

		createCreateNodePanelDecisionKnowledgeElement();

		createNode.pack();
		createNode.setLocationRelativeTo(frame);
		createNode.setVisible(true);

		return true;
	}

	private void createCreateNodePanelDecisionKnowledgeElement() {
		createNodePanel = new JPanel();
		createNodePanel.setLayout(new GridLayout(7, 1));

		JLabel enterDecisionTypesLabel = new JLabel("Select decision type:");
		createNodePanel.add(enterDecisionTypesLabel);
		decisionTypesComboBox = createDecisionTypeComboBox();
		createNodePanel.add(decisionTypesComboBox);

		JLabel enterSummaryLabel = new JLabel("Enter a summary:");
		createNodePanel.add(enterSummaryLabel);
		enterSummaryTextField = new JTextField();
		createNodePanel.add(enterSummaryTextField);
		JLabel enterDescriptionLabel = new JLabel("Enter a description:");
		createNodePanel.add(enterDescriptionLabel);
		enterDescriptionTextField = new JTextField();
		createNodePanel.add(enterDescriptionTextField);

		createNodePanel.add(createCreateNodeButton());

		createNode.add(createNodePanel);
		createNode.pack();
	}

	private JComboBox<String> createDecisionTypeComboBox() {
		KnowledgeType[] knowledgeTypes = KnowledgeType.values();
		int knowledgeTypesLength = knowledgeTypes.length;
		String[] knowledgeTypesStrings = new String[knowledgeTypesLength - 1];
		int i = 0;
		int j = 0;
		for (i = 0; i < knowledgeTypesLength; i++) {
			if ((knowledgeTypes[i].getName() != KnowledgeType.OTHER.getName()) &&
					(j < knowledgeTypesLength - 1)) {
				knowledgeTypesStrings[j] = knowledgeTypes[i].getName();
				j++;
			}
		}
		JComboBox<String> comboBox = new JComboBox<String>(knowledgeTypesStrings);

		return comboBox;
	}

	private JButton createCreateNodeButton() {
		JButton button = new JButton("Create Node");

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String type = (String) decisionTypesComboBox.getSelectedItem();
				String summary = enterSummaryTextField.getText();
				String description = enterDescriptionTextField.getText();
				KnowledgePersistenceManager.createDecisionKnowledgeElementInJira(type, summary, description);
				createNode.dispose();
			}
		});

		return button;
	}

	@Override
	public int getLinkDistance() {
		return linkDistance;
	}

	@Override
	public GephiGraph getGephiGraph() {
		return gephiGraph;
	}
}