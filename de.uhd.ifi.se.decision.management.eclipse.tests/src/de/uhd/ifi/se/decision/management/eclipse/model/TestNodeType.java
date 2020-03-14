package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestNodeType {

	@Test
	public void testGetNodeType() {
		assertEquals(NodeType.CHANGED_FILE, NodeType.getNodeType("file"));
	}

	@Test
	public void testGetUnknownNodeType() {
		assertEquals(NodeType.OTHER, NodeType.getNodeType("wicked problem"));
	}

}