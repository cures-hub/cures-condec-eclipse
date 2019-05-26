package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public abstract class NodeImpl implements Node {
	private long id = 0;
	private boolean isVisible = true;
	private HashSet<Node> linkedNodes = new HashSet<Node>();
	public static long nextId = 0;
	public static Map<Long, Node> nodes = new HashMap<Long, Node>();
	private static HashSet<Node> allNodes = new HashSet<Node>();

	public NodeImpl() {
		this.id = nextId;
		nextId++;
		nodes.put(this.id, this);
		allNodes.add(this);
	}

	public static HashSet<Node> getAllNodes() {
		return allNodes;
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
		} else {
			this.linkedNodes.add(node);
			return true;
		}
	}

	@Override
	public boolean removeLinkedNode(Node node) {
		if (this.linkedNodes.contains(node)) {
			this.linkedNodes.remove(node);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clearLinkedNodes() {
		this.linkedNodes.clear();
	}

	@Override
	public boolean isLinkedToNode(Node node) {
		if (this.linkedNodes.contains(node)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public HashSet<Node> getLinks() {
		return this.linkedNodes;
	}
}
