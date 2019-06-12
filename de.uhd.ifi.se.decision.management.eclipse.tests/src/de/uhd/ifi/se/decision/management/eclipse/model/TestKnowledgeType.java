package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestKnowledgeType {

	@Test
	public void testGetKnowledgeType() {
		assertEquals(KnowledgeType.ISSUE, KnowledgeType.getKnowledgeType("issue"));
	}

	@Test
	public void testGetUnknownKnowledgeType() {
		assertEquals(KnowledgeType.OTHER, KnowledgeType.getKnowledgeType("wicked problem"));
	}

}
