package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class CommitMessageParser {

	public static List<DecisionKnowledgeElement> extractDecisionKnowledge(RevCommit commit) {
		return extractDecisionKnowledge(commit.getFullMessage());
	}

	/**
	 * Returns all decision knowledge elements explicitly marked in a message, for
	 * example, [issue] How to ...? [/issue] [decision] Let's do ...! [/decision].
	 * 
	 * @param message
	 *            commit message that is parsed for explicitly marked decision
	 *            knowledge elements.
	 * @return list of all decision knowledge elements explicitly marked in a
	 *         message.
	 */
	public static List<DecisionKnowledgeElement> extractDecisionKnowledge(String message) {
		List<DecisionKnowledgeElement> decisionKnowledgeElements = new ArrayList<DecisionKnowledgeElement>();
		List<String> partsOfMessage = CommitMessageParser.getPartsOfMessage(message);
		String description = "";
		KnowledgeType type = null;
		boolean foundType = false;
		for (String part : partsOfMessage) {
			if (foundType) {
				if (part.startsWith("/")) {
					decisionKnowledgeElements.add(new DecisionKnowledgeElementImpl(type, description.trim()));
					description = "";
					foundType = false;
				} else {
					description += part + " ";
				}
			} else {
				type = KnowledgeType.getKnowledgeType(part);
				if (type != KnowledgeType.OTHER) {
					foundType = true;
				}
			}
		}
		return decisionKnowledgeElements;
	}

	private static List<String> getPartsOfMessage(String message) {
		List<String> parts = new ArrayList<String>();
		String[] split = message.split("\\[");
		for (String i : split) {
			for (String j : i.split("\\]")) {
				parts.add(j);
			}
		}
		return parts;
	}

	public static List<String> getJiraIssueKeys(RevCommit revCommit, String projectKey) {
		return getJiraIssueKeys(revCommit.getFullMessage(), projectKey);
	}

	/**
	 * Returns all JIRA issue keys mentioned in a message.
	 * 
	 * @param message
	 *            commit message that is parsed for JIRA issue keys.
	 * @param projectKey
	 *            key of the JIRA project that every JIRA issue key starts with.
	 * @return list of all mentioned JIRA issue keys in upper case (might contain
	 *         duplicates and is ordered by their appearance in the message).
	 */
	public static List<String> getJiraIssueKeys(String message, String projectKey) {
		List<String> keys = new ArrayList<String>();
		if (projectKey == null) {
			return keys;
		}
		String[] words = message.split("[\\s,:]+");
		String baseKey = projectKey.toUpperCase(Locale.ENGLISH);
		for (String word : words) {
			word = word.toUpperCase(Locale.ENGLISH);
			if (word.contains(baseKey + "-")) {
				keys.add(word);
			}
		}
		return keys;
	}
	
	/**
	 * Retrieves the issue key from a commit message
	 * 
	 * @param commitMessage
	 *            a commit message that should contain an issue key
	 * @return extracted issue key
	 */
	public static String getIssueKey(String commitMessage) {
		if (commitMessage.contains(" ")) {
			String[] split = commitMessage.split("[\\s,:]+");
			return split[0];
		} else {
			return "";
		}
	}
}
