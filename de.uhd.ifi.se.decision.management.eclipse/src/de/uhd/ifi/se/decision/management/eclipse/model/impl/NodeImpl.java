package de.uhd.ifi.se.decision.management.eclipse.model.impl;

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

	public NodeImpl() {
		this.id = nodes.size();
		nodes.put(this.id, this);
	}

	@Override
	public long getId() {
		return this.id;
	}
}
