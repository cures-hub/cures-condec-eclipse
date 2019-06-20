package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import org.jgrapht.graph.DefaultWeightedEdge;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for links (=edges) in the knowledge graph.
 * 
 * @see KnowledgeGraph
 * @see Node
 */
public class LinkImpl extends DefaultWeightedEdge implements Link {

	private static final long serialVersionUID = 1L;

	@Override
	public Node getSource() {
		return (Node) super.getSource();
	}

	@Override
	public Node getTarget() {
		return (Node) super.getTarget();
	}

	@Override
	public double getWeight() {
		return super.getWeight();
	}
}