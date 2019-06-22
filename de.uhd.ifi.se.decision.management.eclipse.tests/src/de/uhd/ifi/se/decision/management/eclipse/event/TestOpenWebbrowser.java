package de.uhd.ifi.se.decision.management.eclipse.event;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.event.OpenWebbrowser;

public class TestOpenWebbrowser {

	@Test
	@Ignore
	public void testWithURL() {
		try {
			URL url = new URL("https://www.linux.org/");
			boolean webbrowserOpened = OpenWebbrowser.openWebpage(url);
			assertTrue(webbrowserOpened);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
}
