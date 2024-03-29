package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for links (=edges) in the knowledge graph.
 * 
 * @see KnowledgeGraph
 * @see Node
 */
@JsonIgnoreProperties({ "source", "target", "sourceNode", "targetNode" })
public class LinkImpl extends DefaultWeightedEdge implements Link {

	private static final long serialVersionUID = 1L;

	private Node sourceNode;
	private Node targetNode;

	public String sourceId;
	public String targetId;

	public LinkImpl() {
	}

	public LinkImpl(Node source, Node target) {
		this.sourceNode = source;
		if (sourceNode != null) {
			sourceId = sourceNode.getNodeId();
		}

		this.targetNode = target;
		if (targetNode != null) {
			targetId = targetNode.getNodeId();
		}
	}

	@Override
	public Node getSource() {
		return (Node) super.getSource();
	}

	@Override
	public Node getSourceNode() {
		return sourceNode;
	}

	@Override
	public void setSourceNode(Node source) {
		this.sourceNode = source;
		if (sourceNode != null) {
			sourceId = sourceNode.getNodeId();
		}
	}

	@Override
	public String getSourceId() {
		return sourceId;
	}

	@Override
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public Node getTarget() {
		return (Node) super.getTarget();
	}

	@Override
	public Node getTargetNode() {
		return targetNode;
	}

	@Override
	public void setTargetNode(Node target) {
		this.targetNode = target;
		if (targetNode != null) {
			targetId = targetNode.getNodeId();
		}
	}

	@Override
	public String getTargetId() {
		return targetId;
	}

	@Override
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Link)) {
			return false;
		}

		Link that = (Link) other;

		if (this.getSourceId() != null && this.getTargetId() != null && that.getSourceId() != null
				&& that.getTargetId() != null) {
			return this.getSourceId().equals(that.getSourceId()) && this.getTargetId().equals(that.getTargetId());
		} else {
			return this.getSource().equals(that.getSource()) && this.getTarget().equals(that.getTarget());
		}
	}
}