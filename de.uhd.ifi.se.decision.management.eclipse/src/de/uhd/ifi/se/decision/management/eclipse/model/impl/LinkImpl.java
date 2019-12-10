package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jgrapht.graph.DefaultWeightedEdge;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for links (=edges) in the knowledge graph.
 * 
 * @see KnowledgeGraph
 * @see Node
 */
@JsonIgnoreProperties({"source", "target", "sourceNode", "targetNode"})
public class LinkImpl extends DefaultWeightedEdge implements Link {

	private static final long serialVersionUID = 1L;
	
	private Node sourceNode;
	private Node targetNode;

	private String sourceID;
	private String targetID;

	public LinkImpl() {
	}
	
	public LinkImpl(Node source, Node target) {
		this.sourceNode = source;
		if (this.sourceNode != null) {
			this.sourceID = sourceNode.toString();
		}
		this.targetNode = target;
		if (this.targetNode != null) {
			this.targetID = targetNode.toString();
		}
	}
	
	@Override
	public Node getSource() {
		return (Node) super.getSource();
	}
	
	@Override
	public Node getSourceNode() {
		return this.sourceNode;
	}
	
	@Override
	public void setSourceNode(Node source) {
		this.sourceNode = source;
		if (this.sourceNode != null) {
			this.sourceID = sourceNode.toString();
		}
	}
	
	@Override
	public String getSourceID() {
		return this.sourceID;
	}

	@Override
	public Node getTarget() {
		return (Node) super.getTarget();
	}
	
	@Override
	public Node getTargetNode() {
		return this.targetNode;
		
	}
	
	@Override
	public void setTargetNode(Node target) {
		this.targetNode = target;
		if (this.targetNode != null) {
			this.targetID = targetNode.toString();
		}
	}

	@Override
	public String getTargetID() {
		return this.targetID;
	}
}