package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;

public class DecisionKnowledgeElementImpl extends NodeImpl implements DecisionKnowledgeElement {
	private String summary;
	private KnowledgeType type;

	public DecisionKnowledgeElementImpl(KnowledgeType knowledgeType, String summary) {
		this.type = knowledgeType;
		this.summary = summary;
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
		return this.type.name() + ": " + this.summary;
	}
}
