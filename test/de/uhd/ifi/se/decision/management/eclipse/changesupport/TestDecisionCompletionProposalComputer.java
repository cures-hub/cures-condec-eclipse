package de.uhd.ifi.se.decision.management.eclipse.changesupport;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.changesupport.DecisionCompletionProposalComputer;

public class TestDecisionCompletionProposalComputer {

	private DecisionCompletionProposalComputer tester;

	@Before
	public void setUp() {
		tester = new DecisionCompletionProposalComputer();
	}

	@After
	public void tearDown() {
		tester = null;
	}

	@Test
	public void testProposalCreation() {

		assertEquals("Creating Proposals in Progress", tester.getErrorMessage());

	}

}
