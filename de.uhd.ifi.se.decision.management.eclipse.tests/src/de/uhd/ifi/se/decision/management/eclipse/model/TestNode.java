package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		assertEquals(element.toString(), Node.getNodeById(0).toString());
	}
}
