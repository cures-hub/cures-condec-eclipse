package de.uhd.ifi.se.decision.management.eclipse.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;

public class TestCommitMessageParser {
	
	@Test
	public void testConstructor() {
		assertNotNull(new CommitMessageParser());
	}

	@Test
	public void testEmptyMessages() {
		String message = "";
		Set<DecisionKnowledgeElement> elements = CommitMessageParser.extractDecisionKnowledge(message);
		assertEquals(0, elements.size());
	}

	@Test
	public void testFilledMessage() {
		String message = "[decision] This is a decision! [/decision]";
		Set<DecisionKnowledgeElement> elements = CommitMessageParser.extractDecisionKnowledge(message);
		assertEquals(1, elements.size());
		assertEquals("This is a decision!", elements.iterator().next().getSummary());
	}

	@Test
	public void testGetJiraIssueKeys() {
		String message = "ConDec-1: Improve almost everything... ConDec-2 even this!";
		Set<String> keys = CommitMessageParser.getJiraIssueKeys(message, "condec");
		assertEquals(2, keys.size());
		Iterator<String> iterator = keys.iterator();
		assertEquals("CONDEC-1", iterator.next());
		assertEquals("CONDEC-2", iterator.next());
	}

	@Test
	public void testGetJiraIssueKeysProjectKeyNull() {
		String message = "ConDec-1: Improve almost everything... ConDec-2 even this!";
		Set<String> keys = CommitMessageParser.getJiraIssueKeys(message, null);
		assertEquals(0, keys.size());
	}

}
