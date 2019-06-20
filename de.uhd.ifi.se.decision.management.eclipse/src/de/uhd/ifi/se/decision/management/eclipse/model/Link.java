package de.uhd.ifi.se.decision.management.eclipse.model;

/**
 * Interface for links (=edges) in the knowledge graph.
 * 
 * @see KnowledgeGraph
 * @see Node
 */
public interface Link {

	/**
	 * Retrieves the source element of this link (=edge).
	 *
	 * @see Node
	 * @return source element of this link.
	 */
	Node getSource();

	/**
	 * Retrieves the target (=destination) element of this link (=edge).
	 *
	 * @see Node
	 * @return destination element of this link.
	 */
	Node getTarget();

	/**
	 * Retrieves the weight of this link (=edge).
	 *
	 * @return weight of this link.
	 */
	double getWeight();
}
