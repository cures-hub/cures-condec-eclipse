package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TestWrongLinkDetector {

	private String projectKey;

	@Before
	public void setUp() {
		projectKey = "ECONDEC";
	}

	@Test
	public void testConstructor() {
		assertNotNull(new WrongLinkDetector());
	}

	@Test
	public void testCheckIDsOneIssue() {
		assertEquals(2.0, WrongLinkDetector.hasMultipleKeys("ECONDEC-5 Add Java Parser", projectKey), 0.01);
	}

	@Test
	public void testCheckIDsTwoIssues() {
		assertEquals(0.5,
				WrongLinkDetector.hasMultipleKeys("ECONDEC-5 Add Java Parser and ECONDEC-6 Connect JGit", projectKey),
				0.01);
	}

	@Test
	public void testCheckIDsMoreThanTwoIssues() {
		assertEquals(0,
				WrongLinkDetector.hasMultipleKeys(
						"ECONDEC-5 Add Java Parser, ECONDEC-6 Connect JGit and ECONDEC-7 Connect JIRA", projectKey),
				0.01);
	}

	@Test
	public void testCheckAndNotIncluded() {
		assertEquals(1, WrongLinkDetector.includesAnd("ECONDEC-5 Add Java Parser"), 0.01);
	}

	@Test
	public void testCheckAndIncluded() {
		assertEquals(0, WrongLinkDetector.includesAnd("ECONDEC-5 Add Java Parser and correct Bug"), 0.01);
	}

	@Test
	public void testCheckCommaNotIncluded() {
		assertEquals(1, WrongLinkDetector.includesComma("ECONDEC-5 Add Java Parser"), 0.01);
	}

	@Test
	public void testCheckCommaIncluded() {
		assertEquals(0, WrongLinkDetector.includesComma("ECONDEC-5 Add Java Parser, correct some Bugs"), 0.01);
	}

	@Test
	public void testProduceVerdictUntangled() {
		assertEquals("untangled", WrongLinkDetector.tanglednessToString("ECONDEC-5 Add Java Parser", projectKey));
	}

	@Test
	public void testProduceVerdictSlightlyTangled() {
		assertEquals("slightly tangled",
				WrongLinkDetector.tanglednessToString("ECONDEC-5 Add Java Parser and fix Bug", projectKey));
	}

	@Test
	public void testProduceVerdictTangled() {
		assertEquals("tangled",
				WrongLinkDetector.tanglednessToString("ECONDEC-5 Add Java Parser ECONDEC-6 Connect JGit", projectKey));
	}

	@Test
	public void testProduceVerdictHeavilyTangled() {
		assertEquals("heavily tangled", WrongLinkDetector
				.tanglednessToString("ECONDEC-5 Add Java Parser, ECONDEC-6 Connect JGit and fix Bug", projectKey));
	}
}