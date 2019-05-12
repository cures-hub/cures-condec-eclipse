package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

public abstract class Node implements INode {
	private long id = 0;
	private boolean isVisible = true;
	private HashSet<INode> linkedNodes = new HashSet<INode>();
	private static long nextId = 0;
	private static Map<Long, INode> nodes = new HashMap<Long, INode>();
	private static HashSet<INode> allNodes = new HashSet<INode>();
	
	public Node() {
		this.id = nextId;
		nextId++;
		nodes.put(this.id, this);
		allNodes.add(this);
	}
	
	
	/**
	 * Get a INode object by its id.
	 * @param id is the id which needs to be looked for.
	 * @return returns null, if the object wasn't found. Otherwise the node-object
	 * will be returned.
	 */
	public static INode getNodeById(long id) {
		if(nodes.containsKey(id)) {
			return nodes.get(id);
		} else {
			return null;
		}
	}
	
	public static HashSet<INode> getAllNodes(){
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
	public boolean addLinkedNode(INode node) {
		if(this.linkedNodes.contains(node) || this.equals(node)) {
			return false;
		} else {
			this.linkedNodes.add(node);
			return true;
		}
	}


	@Override
	public boolean removeLinkedNode(INode node) {
		if(this.linkedNodes.contains(node)) {
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
	public boolean isLinkedToNode(INode node) {
		if(this.linkedNodes.contains(node)) {
			return true;
		} else {
			return false; 
		}
	}


	@Override
	public HashSet<INode> getLinks() {
		return this.linkedNodes;
	}
}
