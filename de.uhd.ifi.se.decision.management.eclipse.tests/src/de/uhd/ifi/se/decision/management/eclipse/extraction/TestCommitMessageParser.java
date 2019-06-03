package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;

public class TestCommitMessageParser {
	
	@Test
	public void testEmptyMessages() {
		String message = "";
		List<DecisionKnowledgeElement> elements = CommitMessageParser.extractDecisionKnowledge(message);
		assertEquals(0, elements.size());
	}
	
	@Test
	public void testFilledMessage() {
		String message = "[decision] This is a decision! [/decision]";
		List<DecisionKnowledgeElement> elements = CommitMessageParser.extractDecisionKnowledge(message);
		assertEquals(1, elements.size());
		assertEquals("This is a decision!", elements.get(0).getDescription());
	}

}
