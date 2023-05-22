package de.uhd.ifi.se.decision.management.eclipse.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;

/**
 * Interface for links (=edges) in the knowledge graph.
 * 
 * @see KnowledgeGraph
 * @see Node
 */
@JsonDeserialize(as = LinkImpl.class)
public interface Link {

	/**
	 * Retrieves the source element of this link (= edge).
	 *
	 * @see Node
	 * @return source element of this link.
	 */
	Node getSource();

	/**
	 * Retrieves the source node of this link (= edge).
	 *
	 * @see Node
	 * @return source node of this link.
	 */
	Node getSourceNode();

	/**
	 * Sets the source element of this link (= edge).
	 * 
	 * @param source
	 *            the source node
	 */
	void setSourceNode(Node source);

	/**
	 * Retrieves the source id of this link (= edge).
	 *
	 * @return source id of this link.
	 */
	String getSourceId();

	/**
	 * Sets the source id of this link (= edge).
	 * 
	 * @param sourceId
	 *            the source id
	 */
	void setSourceId(String sourceId);

	/**
	 * Retrieves the target (= destination) element of this link (= edge).
	 *
	 * @see Node
	 * @return destination element of this link.
	 */
	Node getTarget();

	/**
	 * Retrieves the target (= destination) node of this link (= edge).
	 *
	 * @see Node
	 * @return destination node of this link.
	 */
	Node getTargetNode();

	/**
	 * Retrieves the target id of this link (= edge).
	 *
	 * @return target id of this link.
	 */
	String getTargetId();

	/**
	 * Sets the target id of this link (= edge).
	 * 
	 * @param targetId
	 *            the target id
	 */
	void setTargetId(String targetId);

	/**
	 * Sets the target (= destination) node of this link (= edge).
	 * 
	 * @param target
	 *            the target node
	 */
	void setTargetNode(Node target);
}
