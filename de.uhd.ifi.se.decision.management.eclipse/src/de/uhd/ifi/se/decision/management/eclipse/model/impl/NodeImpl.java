package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for nodes of the knowledge graph. This class is abstract and cannot be
 * instantiated.
 * 
 * @see KnowledgeGraph
 * @see Link
 */
public abstract class NodeImpl implements Node {
	private long id;
	private Set<Node> linkedNodes;

	public NodeImpl() {
		this.id = nodes.size();
		nodes.put(this.id, this);
		this.linkedNodes = new HashSet<Node>();
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public boolean addLinkedNode(Node node) {
		if (this.equals(node)) {
			return false;
		}
		return linkedNodes.add(node);
	}

	@Override
	public Set<Node> getLinkedNodes() {
		return this.linkedNodes;
	}
}
