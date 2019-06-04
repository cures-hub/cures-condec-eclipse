package de.uhd.ifi.se.decision.management.eclipse.view;

import java.util.Map;
import java.util.Set;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.persistence.GraphSettings;

public class GephiGraph {
	
	private GraphModel graphModel;
	public DirectedGraph directedGraph;
	private Map<Node, Set<Node>> graph;
	
	public GephiGraph() {
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		
		this.graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
		this.directedGraph = graphModel.getDirectedGraph();
		
		GraphView graphView = graphModel.getGraph().getView();
		this.graphModel.setVisibleView(graphView);
	}
	
	public void createGephiGraph(Map<Node, Set<Node>> graph) {
		this.graph = graph;
		generateGraph(graph.keySet());
		GraphSettings.getLayoutType().generateLayout(graphModel, graph.size());
	}
	
	public void generateGraph(Set<Node> nodes) {
		for (Node node : nodes) {
			org.gephi.graph.api.Node gephiNode = createNode(node);
			setPosition(gephiNode, nodes.size());
			directedGraph.addNode(gephiNode);
		}
		createEdges();
	}
	

	private org.gephi.graph.api.Node createNode(Node node) {
		org.gephi.graph.api.Node gephiNode = graphModel.factory().newNode(String.valueOf(node.getId()));
		gephiNode.setLabel("[" + String.valueOf(node.getId()) + "] " + node.toString());
		GraphSettings.getColor(node);
		gephiNode.setColor(GraphSettings.getColor(node));
		return gephiNode;
	}
	

	private void setPosition(org.gephi.graph.api.Node gephiNode, int numberOfNodes) {
		gephiNode.setX((float) Math.random() * 100f * (float) Math.sqrt(numberOfNodes));
		gephiNode.setY((float) Math.random() * 100f * (float) Math.sqrt(numberOfNodes));
	}

	private void createEdges() {
		for (Map.Entry<Node, Set<Node>> entry : graph
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

}
