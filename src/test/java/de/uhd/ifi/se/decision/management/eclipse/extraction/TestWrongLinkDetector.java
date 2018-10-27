package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.WrongLinkDetector;

public class TestWrongLinkDetector {

	private String projectID;

	@Before
	public void setUp() {
		projectID = "BMS";
	}

	@Test
	public void testCheckIDsOneIssue() {
		assertEquals(2.0, WrongLinkDetector.hasMultipleIds("BMS-5 Add Java Parser", projectID), 0.01);
	}

	@Test
	public void testCheckIDsTwoIssues() {
		assertEquals(0.5, WrongLinkDetector.hasMultipleIds("BMS-5 Add Java Parser and BMS-6 Connect JGit", projectID),
				0.01);
	}

	@Test
	public void testCheckIDsMoreThanTwoIssues() {
		assertEquals(0, WrongLinkDetector
				.hasMultipleIds("BMS-5 Add Java Parser, BMS-6 Connect JGit and BMS-7 Connect JIRA", projectID), 0.01);
	}

	@Test
	public void testCheckAndNotIncluded() {
		assertEquals(1, WrongLinkDetector.includesAnd("BMS-5 Add Java Parser"), 0.01);
	}

	@Test
	public void testCheckAndIncluded() {
		assertEquals(0, WrongLinkDetector.includesAnd("BMS-5 Add Java Parser and correct Bug"), 0.01);
	}

	@Test
	public void testCheckCommaNotIncluded() {
		assertEquals(1, WrongLinkDetector.includesComma("BMS-5 Add Java Parser"), 0.01);
	}

	@Test
	public void testCheckCommaIncluded() {
		assertEquals(0, WrongLinkDetector.includesComma("BMS-5 Add Java Parser, correct some Bugs"), 0.01);
	}

	@Test
	public void testProduceVerdictUntangled() {
		assertEquals("untangled", WrongLinkDetector.tanglednessToString("BMS-5 Add Java Parser", projectID));
	}

	@Test
	public void testProduceVerdictSlightlyTangled() {
		assertEquals("slightly tangled",
				WrongLinkDetector.tanglednessToString("BMS-5 Add Java Parser and fix Bug", projectID));
	}

	@Test
	public void testProduceVerdictTangled() {
		assertEquals("tangled",
				WrongLinkDetector.tanglednessToString("BMS-5 Add Java Parser BMS-6 Connect JGit", projectID));
	}

	@Test
	public void testProduceVerdictHeavilyTangled() {
		assertEquals("heavily tangled", WrongLinkDetector
				.tanglednessToString("BMS-5 Add Java Parser, BMS-6 Connect JGit and fix Bug", projectID));
	}
}