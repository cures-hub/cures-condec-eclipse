package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestPreferencePage {
	
	@Test
	public void testConstructor() {
		PreferencePage page = new PreferencePage();
		assertNotNull(page);		
	}
}
