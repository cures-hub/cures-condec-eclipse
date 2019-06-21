package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class TestDecisionKnowledgeElement {
	
	@Test
	public void testSetAndGetKnowledgeType() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
		element.setType(KnowledgeType.DECISION);
		assertEquals(KnowledgeType.DECISION, element.getType());
	}
	
	@Test
	public void testSetAndGetSummary() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"How to ...?");
		element.setSummary("This is a decision!");
		assertEquals("This is a decision!", element.getSummary());
	}
	
	@Test
	public void testToString() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"This is a decision!");
		assertEquals("Decision: This is a decision!", element.toString());
	}

}
