package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestPropertyPage {

	@Test
	public void testConstructor() {
		PropertyPage page = new PropertyPage();
		assertNotNull(page);
	}
}
