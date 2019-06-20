package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.extraction.KnowledgeGraph;

/**
 * Interface for nodes of the knowledge graph.
 * 
 * @see KnowledgeGraph
 * @see Link
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
	 * Get a set of all linked nodes.
	 * 
	 * @return set of all linked nodes. The set is never null.
	 */
	Set<Node> getLinkedNodes();
}
