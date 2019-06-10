package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public abstract class NodeImpl implements Node {
	private long id;
	private boolean isVisible;
	private Set<Node> linkedNodes;

	public NodeImpl() {
		this.id = nodes.size();
		nodes.put(this.id, this);

		this.isVisible = true;
		this.linkedNodes = new HashSet<Node>();
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean getVisibility() {
		return this.isVisible;
	}

	@Override
	public void setVisibility(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public boolean addLinkedNode(Node node) {
		if (this.linkedNodes.contains(node) || this.equals(node)) {
			return false;
		}
		this.linkedNodes.add(node);
		return true;
	}

	@Override
	public boolean removeLinkedNode(Node node) {
		if (this.linkedNodes.contains(node)) {
			this.linkedNodes.remove(node);
			return true;
		}
		return false;
	}

	@Override
	public void removeLinkedNodes() {
		this.linkedNodes.clear();
	}

	@Override
	public boolean isLinkedToNode(Node node) {
		return this.linkedNodes.contains(node);
	}

	@Override
	public Set<Node> getLinkedNodes() {
		return this.linkedNodes;
	}
}
