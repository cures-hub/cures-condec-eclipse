package de.uhd.ifi.se.decision.management.eclipse.view.impl;

import java.util.Map;
import java.util.Set;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.NodeIterable;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.persistence.GraphSettings;
import de.uhd.ifi.se.decision.management.eclipse.view.GephiGraph;

public class GephiGraphImpl implements GephiGraph {

	private GraphModel graphModel;
	private DirectedGraph directedGraph;

	public GephiGraphImpl() {
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();

		this.graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		this.directedGraph = graphModel.getDirectedGraph();

		GraphView graphView = graphModel.getGraph().getView();
		this.graphModel.setVisibleView(graphView);
	}

	@Override
	public void createGephiGraph(Map<Node, Set<Node>> graph) {
		Set<Node> nodes = graph.keySet();
		float positionOffset = (float) Math.sqrt(nodes.size());
		for (Node node : nodes) {
			org.gephi.graph.api.Node gephiNode = createNode(node);
			setPosition(gephiNode, positionOffset);
			directedGraph.addNode(gephiNode);
		}
		createEdges(graph);
		GraphSettings.getLayoutType().generateLayout(graphModel, graph.size());
	}

	private org.gephi.graph.api.Node createNode(Node node) {
		org.gephi.graph.api.Node gephiNode = initNode(node);
		gephiNode.setLabel("[" + String.valueOf(node.getId()) + "] " + node.toString());
		gephiNode.setColor(GraphSettings.getColor(node));
		return gephiNode;
	}

	private org.gephi.graph.api.Node initNode(Node node) {
		return graphModel.factory().newNode(String.valueOf(node.getId()));
	}

	private void setPosition(org.gephi.graph.api.Node gephiNode, float positionOffset) {
		gephiNode.setX((float) Math.random() * 100f * positionOffset);
		gephiNode.setY((float) Math.random() * 100f * positionOffset);
	}

	private void createEdges(Map<Node, Set<Node>> graph) {
		for (Map.Entry<Node, Set<Node>> entry : graph.entrySet()) {
			for (Node node : entry.getValue()) {
				Edge edge = initEdge(entry.getKey(), node);
				if (edge != null) {
					directedGraph.addEdge(edge);
				} else {
					System.out.println(
							"Failed to link \"" + entry.getKey().toString() + "\" with \"" + node.toString() + "\"");
				}
			}
		}
	}

	private Edge initEdge(Node node1, Node node2) {
		org.gephi.graph.api.Node gephiNode1 = directedGraph.getNode(String.valueOf(node1.getId()));
		org.gephi.graph.api.Node gephiNode2 = directedGraph.getNode(String.valueOf(node2.getId()));
		return initEdge(gephiNode1, gephiNode2);
	}

	private Edge initEdge(org.gephi.graph.api.Node gephiNode1, org.gephi.graph.api.Node gephiNode2) {
		if (gephiNode1 == null || gephiNode2 == null) {
			return null;
		}
		return graphModel.factory().newEdge(gephiNode1, gephiNode2);
	}

	@Override
	public org.gephi.graph.api.Node getGephiNode(Node node) {
		if (node == null) {
			return null;
		}
		return directedGraph.getNode(String.valueOf(node.getId()));
	}

	@Override
	public org.gephi.graph.api.Node getGephiNode(long nodeId) {
		return directedGraph.getNode(String.valueOf(nodeId));
	}

	@Override
	public org.gephi.graph.api.Node getGephiNode(String nodeText) {
		return directedGraph.getNode(nodeText);
	}

	@Override
	public NodeIterable getNodes() {
		return directedGraph.getNodes();
	}
}
