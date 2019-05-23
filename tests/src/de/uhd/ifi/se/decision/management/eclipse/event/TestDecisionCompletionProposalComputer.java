package de.uhd.ifi.se.decision.management.eclipse.event;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.event.DecisionCompletionProposalComputer;

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

	@Ignore
	public void testProposalCreation() {

		assertEquals("Creating Proposals in Progress", tester.getErrorMessage());

	}

}
