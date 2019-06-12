package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class TestNode {

	@Test
	public void testGetNodeByIdNull() {
		assertNull(Node.getNodeById(-1));
	}

	@Test
	public void testGetNodeById() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"This is a decision!");
		assertEquals(element, Node.getNodeById(element.getId()));
	}

	@Test
	public void testAddLinkedNode() {
		DecisionKnowledgeElement decision = new DecisionKnowledgeElementImpl(KnowledgeType.DECISION,
				"This is a decision!");
		DecisionKnowledgeElement issue = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE, "How should we...?");
		assertFalse(decision.addLinkedNode(decision));
		assertTrue(decision.addLinkedNode(issue));
	}
}
