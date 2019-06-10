package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Interface for nodes of the knowledge graph.
 */
public interface Node {

	/**
	 * Instances of Node class that are identified by their unique id.
	 */
	static Map<Long, Node> nodes = new HashMap<Long, Node>();
	
	/**
	 * Get a node object by its id.
	 * 
	 * @param id
	 *            that is looked for.
	 * @return node-object or null, if the object was not found.
	 */
	public static Node getNodeById(long id) {
		if (id < 0) {
			return null;
		}
		return nodes.get(id);
	}

	/**
	 * Get the id of the node.
	 *
	 * @return id of the node.
	 */
	long getId();

	/**
	 * Set the id of the node. This function should only be used when creating a new
	 * Node-instance.
	 *
	 * @param id
	 *            of the node.
	 */
	void setId(long id);

	/**
	 * Get the visibility of the current node.
	 * 
	 * @return true if node is visible, otherwise false.
	 */
	boolean getVisibility();

	/**
	 * Set the node to visible or invisible.
	 * 
	 * @param visible
	 *            true if node is visible and false if node is invisible.
	 */
	void setVisibility(boolean isVisible);

	/**
	 * Add a reference to another node to create a link/an edge.
	 * 
	 * @param node
	 *            is the node, where the link is pointing to.
	 * @return is true, if linking was successful.
	 */
	boolean addLinkedNode(Node node);

	/**
	 * Remove a reference to another node which is linked.
	 * 
	 * @param node
	 *            node that is removed from the linked list.
	 */
	boolean removeLinkedNode(Node node);

	/**
	 * Removes all outgoing links from a node. Other nodes might still point at this
	 * node.
	 */
	void removeLinkedNodes();

	/**
	 * Checks if the current node has a link to a given node.
	 * 
	 * @param node
	 *            node that might be linked.
	 * @return true if there is a link to the node, otherwise false.
	 */
	boolean isLinkedToNode(Node node);

	/**
	 * Get the full list of all linked nodes.
	 * 
	 * @return The ArrayList is never null. It contains all linked nodes.
	 */
	Set<Node> getLinkedNodes();
}
