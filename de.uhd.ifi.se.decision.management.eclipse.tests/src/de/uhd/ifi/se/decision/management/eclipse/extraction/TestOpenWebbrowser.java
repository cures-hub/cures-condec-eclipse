package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.Test;

public class TestOpenWebbrowser {

	@Test
	public void testWithURL() {
		try {
			URL url = new URL("https://www.google.com");
			boolean webbrowserOpened = OpenWebbrowser.openWebpage(url);
			assertTrue(webbrowserOpened);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
}
