package de.uhd.ifi.se.decision.management.eclipse.view;

import org.gephi.graph.api.NodeIterable;
import org.jgrapht.Graph;

import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public interface GephiGraph {

	void createGephiGraph(Graph<Node, Link> graph);

	org.gephi.graph.api.Node getGephiNode(Node node);

	org.gephi.graph.api.Node getGephiNode(long nodeId);

	org.gephi.graph.api.Node getGephiNode(String nodeText);

	NodeIterable getNodes();

	void setSizeOfAllNodes(float size);

	void setSizeOfNode(org.gephi.graph.api.Node gephiNode, float size);

	void setSizeOfNode(long selectedNodeId, float size);

	void setSizeOfNode(Node node, float size);
}