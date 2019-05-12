package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import de.uhd.ifi.se.decision.management.eclipse.model.IDecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;

public class DecisionKnowledgeElement extends NodeImpl implements IDecisionKnowledgeElement {
	private KnowledgeType knowledgeType;
	private String knowledgeMessage;
	
	public DecisionKnowledgeElement() {/* Default constructor*/ }
	
	public DecisionKnowledgeElement(KnowledgeType knowledgeType, String knowledgeMessage) {
		this.knowledgeType = knowledgeType;
		this.knowledgeMessage = knowledgeMessage;
	}
	
	public KnowledgeType getKnowledgeType() {
		return knowledgeType;
	}
	public void setKnowledgeType(KnowledgeType knowledgeType) {
		this.knowledgeType = knowledgeType;
	}
	public String getKnowledgeMessage() {
		return knowledgeMessage.replace("\r\n", " ").replace("\n", " ");
	}
	public void setKnowledgeMessage(String knowledgeMessage) {
		this.knowledgeMessage = knowledgeMessage;
	}

	@Override
	public String getSummary() {
		return this.knowledgeType.name();
	}

	@Override
	public void setSummary(String summary) {
		// Not intended to be used so far
	}

	@Override
	public String getDescription() {
		return this.knowledgeMessage;
	}

	@Override
	public void setDescription(String description) {
		// Not intended to be used so far
	}
	
	@Override
	public String toString() {
		return this.knowledgeType.name() + ": " + this.knowledgeMessage;
	}
}


