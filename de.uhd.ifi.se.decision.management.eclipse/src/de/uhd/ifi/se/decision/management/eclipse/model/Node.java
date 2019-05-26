package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashSet;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.NodeImpl;

public interface Node {
	/**
	 * Get the id of the node.
	 *
	 * @return id of the decision knowledge element.
	 */
	long getId();

	/**
	 * Set the id of the node. This function should only be used when creating
	 * a new Node-instance.
	 *
	 * @param id
	 *            of the decision knowledge element.
	 */
	void setId(long id);
	
	/**
	 * Get the visibility of the current node.
	 *  
	 * @return True if node is visible, otherwise false.
	 */
	boolean getVisibility();
	
	/**
	 * Set the node to visible or invisible.
	 * @param visible
	 * 				is true, if node is visible and false, if node is invisible.
	 */
	void setVisibility(boolean isVisible);
	
	/**
	 * Add a reference to another node for creating a link
	 * @param node 
	 * 			is the node, where the link is pointing to.
	 * @return is true, if linking was successful.
	 */
	boolean addLinkedNode(Node node);
	
	/**
	 * Remove a reference to another node which is linked.
	 * @param node
	 * 			is the node, which is going to be removed from the linked list.
	 */
	boolean removeLinkedNode(Node node);
	
	/**
	 * Removes all links from a node. Other nodes might still point at this node.
	 */
	void clearLinkedNodes();
	
	/**
	 * Checks if the current node has a link to a given node.
	 * @param node
	 * 			is the node, which will be checked for having a link to.
	 * @return
	 * 		is true, if there is a link to the node and is false, if there is no link
	 * 		to the given node.
	 */
	boolean isLinkedToNode(Node node);
	
	/**
	 * Get the full list of all linked nodes.
	 * @return
	 * 		The ArrayList is never null. It contains all linked nodes.
	 */
	HashSet<Node> getLinks();
	
	/**
	 * Get a INode object by its id.
	 * 
	 * @param id
	 *            is the id which needs to be looked for.
	 * @return returns null, if the object wasn't found. Otherwise the node-object
	 *         will be returned.
	 */
	public static Node getNodeById(long id) {
		if (NodeImpl.nodes.containsKey(id)) {
			return NodeImpl.nodes.get(id);
		} else {
			return null;
		}
	}
}
