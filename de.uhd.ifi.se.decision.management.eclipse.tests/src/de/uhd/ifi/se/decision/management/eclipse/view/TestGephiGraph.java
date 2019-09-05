package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.view.impl.GephiGraphImpl;

public class TestGephiGraph {

	@Test
	public void testKnowledgeGraphNull() {
		assertNotNull(new GephiGraphImpl(null));
		assertNotNull(new GephiGraphImpl(null, LayoutType.NOVERLAP));
	}

}
