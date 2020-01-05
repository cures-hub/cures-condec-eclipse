package de.uhd.ifi.se.decision.management.eclipse.view.impl;

import java.util.Iterator;
import java.util.Set;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.NodeIterable;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.jgrapht.traverse.DepthFirstIterator;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.persistence.GraphSettings;
import de.uhd.ifi.se.decision.management.eclipse.view.GephiGraph;
import de.uhd.ifi.se.decision.management.eclipse.view.LayoutType;

/**
 * Class to create a gephi graph from the knowledge graph. Used in the
 * KnowledgeGraphView class.
 * 
 * @issue How can we use the filters that come with the gephi library instead of
 *        building our own filters? Would using the gephi filters increase the
 *        performance?
 */
public class GephiGraphImpl implements GephiGraph {

	private Workspace workspace;
	private GraphModel graphModel;
	private DirectedGraph directedGraph;
	private LayoutType layoutType;

	public GephiGraphImpl(KnowledgeGraph graph) {
		this(graph, LayoutType.YIFAN_HU);
	}

	public GephiGraphImpl(KnowledgeGraph graph, LayoutType layoutType) {
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		workspace = projectController.getCurrentWorkspace();

		this.graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		this.directedGraph = graphModel.getDirectedGraph();

		GraphView graphView = graphModel.getGraph().getView();
		this.graphModel.setVisibleView(graphView);
		this.layoutType = layoutType;
		this.createGephiGraph(graph);
	}

	@Override
	public void createGephiGraph(KnowledgeGraph graph) {
		if (graph == null) {
			return;
		}
		Set<Node> nodes = graph.vertexSet();
		float positionOffset = (float) Math.sqrt(nodes.size());

		Iterator<Node> iterator = new DepthFirstIterator<Node, Link>(graph, nodes.iterator().next());
		while (iterator.hasNext()) {
			Node node = iterator.next();
			org.gephi.graph.api.Node gephiNode = createNode(node);
			setPosition(gephiNode, positionOffset);
			directedGraph.addNode(gephiNode);

			Set<Link> outgoingEdges = graph.outgoingEdgesOf(node);
			for (Link outgoingEdge : outgoingEdges) {
				Edge edge = initEdge(node, outgoingEdge.getTarget());
				if (edge != null) {
					directedGraph.addEdge(edge);
				}
			}

			Set<Link> ingoingEdges = graph.incomingEdgesOf(node);
			for (Link ingoingEdge : ingoingEdges) {
				Edge edge = initEdge(ingoingEdge.getSource(), node);
				if (edge != null) {
					directedGraph.addEdge(edge);
				}
			}
		}
		this.layoutType.generateLayout(graphModel, nodes.size());
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

	@Override
	public void setSizeOfAllNodes(float size) {
		for (org.gephi.graph.api.Node gephiNode : getNodes()) {
			gephiNode.setSize(0f);
		}
	}

	@Override
	public void setSizeOfNode(org.gephi.graph.api.Node gephiNode, float size) {
		if (gephiNode != null) {
			gephiNode.setSize(size);
		}
	}

	@Override
	public void setSizeOfNode(long nodeId, float size) {
		org.gephi.graph.api.Node gephiNode = getGephiNode(nodeId);
		setSizeOfNode(gephiNode, size);
	}

	@Override
	public void setSizeOfNode(Node node, float size) {
		org.gephi.graph.api.Node gephiNode = getGephiNode(node);
		setSizeOfNode(gephiNode, size);
	}
	
	@Override
	public Workspace getWorkspace() {
		return workspace;
	}
}
