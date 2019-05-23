package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

public class TestOpenWebbrowser {

	@Test
	@Ignore
	public void test() {
		try {
			URL url = new URL("https://www.google.com");
			boolean webbrowserOpened = OpenWebbrowser.openWebpage(url);
			assertTrue(webbrowserOpened);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

}
