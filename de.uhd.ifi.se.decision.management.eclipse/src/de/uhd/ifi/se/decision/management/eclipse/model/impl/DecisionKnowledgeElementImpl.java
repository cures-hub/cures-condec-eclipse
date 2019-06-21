package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

/**
 * Class for decision knowledge elements as part of the knowledge graph.
 */
public class DecisionKnowledgeElementImpl extends NodeImpl implements DecisionKnowledgeElement {
	private String summary;
	private KnowledgeType type;
	private Set<DecisionKnowledgeElement> linkedDecisionKnowledgeElements;

	public DecisionKnowledgeElementImpl(KnowledgeType knowledgeType, String summary) {
		this.type = knowledgeType;
		this.summary = summary;
		this.linkedDecisionKnowledgeElements = new HashSet<DecisionKnowledgeElement>();
	}

	@Override
	public KnowledgeType getType() {
		return type;
	}

	@Override
	public void setType(KnowledgeType type) {
		this.type = type;
	}

	@Override
	public String getSummary() {
		return this.summary;
	}

	@Override
	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return this.type.getName() + ": " + this.summary;
	}

	@Override
	public Set<DecisionKnowledgeElement> getLinkedDecisionKnowledgeElements() {
		return linkedDecisionKnowledgeElements;
	}

	@Override
	public boolean addLinkedDecisionKnowledgeElement(DecisionKnowledgeElement element) {
		if (this.equals(element)) {
			return false;
		}
		return linkedDecisionKnowledgeElements.add(element);
	}

	@Override
	public Set<Node> getLinkedNodes() {
		Set<Node> linkedNodes = new HashSet<Node>();
		linkedNodes.addAll(this.getLinkedDecisionKnowledgeElements());
		return linkedNodes;
	}
}
