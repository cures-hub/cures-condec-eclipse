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
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.RenderTarget;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.event.OpenWebbrowser;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.JiraIssueImpl;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.persistence.GraphSettings;
import de.uhd.ifi.se.decision.management.eclipse.view.Filter;
import de.uhd.ifi.se.decision.management.eclipse.view.GephiGraph;
import de.uhd.ifi.se.decision.management.eclipse.view.GraphFiltering;
import de.uhd.ifi.se.decision.management.eclipse.view.KnowledgeGraphView;
import de.uhd.ifi.se.decision.management.eclipse.view.PreviewSketch;

/**
 * Class to create a view for the knowledge graph model class.
 */
public class KnowledgeGraphViewImpl implements KnowledgeGraphView {
	private String searchString;
	private JTextField searchTextField;

	private long selectedNodeId = -1;
	private JTextField selectedNodeTextField;

	private PreviewController previewController;
	private PreviewSketch previewSketch;
	private GraphFiltering graphFiltering;
	private GephiGraph gephiGraph;

	private int linkDistance;
	private float decreaseFactor;

	public KnowledgeGraphViewImpl(KnowledgeGraph graph) {
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
	public KnowledgeGraphViewImpl(KnowledgeGraph graph, String frameTitle) {
		this.gephiGraph = new GephiGraphImpl(graph);

		this.previewController = Lookup.getDefault().lookup(PreviewController.class);
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		this.previewSketch = new PreviewSketch(target);

		GraphSettings.initPreviewModel(previewController);
		this.graphFiltering = new GraphFiltering();
		for (Filter filter : graphFiltering.filters.values()) {
			filter.getCheckBox().addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						filter.setActivated(false);
					} else if (e.getStateChange() == ItemEvent.SELECTED) {
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

	private void createView(String frameTitle) {
		updateNodeSizes();
		initJFrame(frameTitle);
		refresh();
	}

	private void initJFrame(String title) {
		JFrame frame = new JFrame(title);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(previewSketch, BorderLayout.CENTER);
		JPanel panel = initJPanel();
		frame.add(panel, BorderLayout.WEST);
		frame.setSize(1600, 900);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				previewSketch.resetZoom();
			}
		});
		frame.setVisible(true);
	}

	private JPanel initJPanel() {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(400, 1000));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		// Search Field
		searchTextField = createTextField("Search...");
		panel.add(searchTextField);

		// Search Button
		panel.add(initSearchButton());

		// Interaction
		selectedNodeTextField = createTextField("ID");
		panel.add(selectedNodeTextField);

		// Highlight Node Button
		JButton highlightNodeButton = createHighlightNodeButton();
		panel.add(highlightNodeButton);

		// JumpTo Button
		JButton jumpToButton = createJumpToButton();
		panel.add(jumpToButton);

		// Reset Button
		JButton resetButton = createResetButton();
		panel.add(resetButton);

		// Filters
		JPanel filterPanel = createFilterPanel();
		panel.add(filterPanel);
		return panel;
	}

	private JButton createHighlightNodeButton() {
		JButton selectedNodeButton = createButton("Highlight Node");
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
				} catch (Exception ex) {
					selectedNodeId = -1;
				}
				updateNodeSizes();
			}
		});
		return selectedNodeButton;
	}

	private JButton createJumpToButton() {
		JButton jumpToButton = createButton("Jump to");
		jumpToButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					selectedNodeId = Long.parseLong(selectedNodeTextField.getText());
				} catch (Exception ex) {
					selectedNodeId = -1;
				}
				if (selectedNodeId <= 0) {
					return;
				}
				Node node = Node.getNodeById(selectedNodeId);
				if (node == null) {
					return;
				}
				if (node instanceof JiraIssueImpl) {
					OpenWebbrowser.openWebpage((JiraIssue) node);
				}
			}
		});
		return jumpToButton;
	}

	private JButton createResetButton() {
		JButton resetButton = createButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetFilters();
				updateNodeSizes();
			}
		});
		return resetButton;
	}

	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Filters:");
		filterPanel.add(label);
		for (Filter filter : graphFiltering.filters.values()) {
			filterPanel.add(filter.getCheckBox());
		}
		return filterPanel;
	}

	private JTextField createTextField(String text) {
		JTextField textField = new JTextField();
		textField.setSize(400, 40);
		textField.setVisible(true);
		textField.setMargin(new Insets(5, 5, 5, 5));
		textField.setText(text);
		return textField;
	}

	private JButton createButton(String text) {
		JButton button = new JButton();
		button.setSize(400, 40);
		button.setText(text);
		button.setVisible(true);
		button.setMargin(new Insets(5, 5, 5, 5));
		return button;
	}

	private JButton initSearchButton() {
		JButton searchButton = createButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchString = searchTextField.getText();
				if (searchString == null || searchString.isEmpty()) {
					searchTextField.setText("Search...");
				}
				updateNodeSizes();
			}
		});
		return searchButton;
	}

	private void refresh() {
		this.previewController.refreshPreview();
		this.previewSketch.refresh();
	}

	private void resetFilters() {
		searchString = "";
		searchTextField.setText("Search...");

		selectedNodeId = -1;
		selectedNodeTextField.setText("ID");

		resetFilterCheckboxes();
	}

	private void resetFilterCheckboxes() {
		for (Filter filter : graphFiltering.filters.values()) {
			filter.getCheckBox().setSelected(true);
		}
	}

	/**
	 * Resets the sizes of all nodes depending on their amount of links.
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
	 * Resets the size of the given node depending on its amount of links.
	 * 
	 * @param gephiNode
	 *            node which size should be set.
	 */
	public void updateNodeSize(org.gephi.graph.api.Node gephiNode) {
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
	public void highlightSelectedNode() {
		Node node = Node.getNodeById(selectedNodeId);
		highlightNode(node);
	}

	@Override
	public void highlightNode(Node node) {
		if (node == null) {
			return;
		}

		Set<Node> visitedNodes = new HashSet<Node>();
		visitedNodes.add(node);

		gephiGraph.setSizeOfAllNodes(0f);
		gephiGraph.setSizeOfNode(selectedNodeId, 5f);

		highlightNode(node, 1, linkDistance, 5f / decreaseFactor, visitedNodes);
	}

	private void highlightNode(Node node, int currentDepth, int maxDepth, float size, Set<Node> visitedNodes) {
		for (Node linkedNode : node.getLinkedNodes()) {
			if (!visitedNodes.contains(linkedNode)) {
				gephiGraph.setSizeOfNode(linkedNode, size);
				visitedNodes.add(linkedNode);
			}
		}
		if (currentDepth + 1 < maxDepth) {
			for (Node n : node.getLinkedNodes()) {
				highlightNode(n, currentDepth + 1, maxDepth, size / decreaseFactor, visitedNodes);
			}
		}
	}
}
