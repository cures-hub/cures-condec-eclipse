package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;

public class TestLink {

	@Test
	public void testConstructor() {
		Link link = new LinkImpl();
		assertNotNull(link);
		assertNull(link.getSource());
		assertNull(link.getTarget());
		assertTrue(link.getWeight() == 1);
	}
}
