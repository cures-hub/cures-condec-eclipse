package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class CommitMessageParser {

	public static List<DecisionKnowledgeElement> extractDecisionKnowledge(RevCommit commit) {
		return extractDecisionKnowledge(commit.getFullMessage());
	}

	public static List<DecisionKnowledgeElement> extractDecisionKnowledge(String message) {
		List<DecisionKnowledgeElement> decisionKnowledgeElements = new ArrayList<DecisionKnowledgeElement>();
		List<String> parts = CommitMessageParser.getTags(message);
		String description = "";
		KnowledgeType type = null;
		boolean foundType = false;
		for (String part : parts) {
			if (foundType) {
				if (part.startsWith("/")) {
					decisionKnowledgeElements.add(
							new DecisionKnowledgeElementImpl(type, description.trim()));
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

	/**
	 * Returns all Issue-Keys which are mentioned in a message.
	 * 
	 * @param commitMessage
	 *            All mentioned IssueKeys must not contain a space. Positive
	 *            Example: "Example-123"
	 * @return List of all mentioned IssueKeys. May contain duplicates; Ordered by
	 *         apperance in message.
	 */
	public static List<String> getAllMentionedIssueKeys(String commitMessage, String issueKeyBase) {
		List<String> keys = new ArrayList<String>();
		commitMessage = commitMessage.replace("\r\n", " ").replace("\n", " ");
		String[] words = commitMessage.toLowerCase().split(" ");
		if (issueKeyBase == null) {
			return keys;
		}
		String basekey = issueKeyBase.toLowerCase();
		for (String word : words) {
			if (word.contains(basekey + "-")) {
				keys.add(word);
			}
		}
		return keys;
	}

	public static List<String> getTags(String commitMessage) {
		List<String> tags = new ArrayList<String>();
		String[] split = commitMessage.split("\\[");
		for (String i : split) {
			for (String j : i.split("\\]")) {
				tags.add(j);
			}
		}
		return tags;
	}
}
