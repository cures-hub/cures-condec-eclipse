package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class CommitMessageParser {

	public static List<DecisionKnowledgeElement> extractDecisionKnowledgeFromCommit(RevCommit commit) {
		String commitMessage = commit.getFullMessage();
		List<DecisionKnowledgeElement> decisionKnowledgeElements = new ArrayList<DecisionKnowledgeElement>();
		// replace("\r\n") is necessary for supporting both Windows-Lineendings AND
		// Unix-Lineendings
		List<String> tags = CommitMessageParser.getTags(commitMessage);
		boolean recordDescription = false;
		String description = "";
		KnowledgeType dt = null;
		for (String tag : tags) {
			tag = tag.replace(" ", "").toLowerCase();
			// Already recording?
			// Yes -> Record until there is a trigger to stop
			if (recordDescription) {
				switch (dt) {
				case ALTERNATIVE:
					if (tag.equals("/alternative")) {
						decisionKnowledgeElements
								.add(new DecisionKnowledgeElementImpl(dt, CommitMessageParser.formatDescription(description)));
						recordDescription = false;
						description = "";
						dt = null;
					} else {
						description += tag + " ";
					}
					continue;
				case CON:
					if (tag.equals("/con")) {
						decisionKnowledgeElements
								.add(new DecisionKnowledgeElementImpl(dt, CommitMessageParser.formatDescription(description)));
						recordDescription = false;
						description = "";
						dt = null;
					} else {
						description += tag + " ";
					}
					continue;
				case DECISION:
					if (tag.equals("/decision")) {
						decisionKnowledgeElements
								.add(new DecisionKnowledgeElementImpl(dt, CommitMessageParser.formatDescription(description)));
						recordDescription = false;
						description = "";
						dt = null;
					} else {
						description += tag + " ";
					}
					continue;
				case GOAL:
					if (tag.equals("/goal")) {
						decisionKnowledgeElements
								.add(new DecisionKnowledgeElementImpl(dt, CommitMessageParser.formatDescription(description)));
						recordDescription = false;
						description = "";
						dt = null;
					} else {
						description += tag  + " ";
					}
					continue;
				case ISSUE:
					if (tag.equals("/issue")) {
						decisionKnowledgeElements
								.add(new DecisionKnowledgeElementImpl(dt, CommitMessageParser.formatDescription(description)));
						recordDescription = false;
						description = "";
						dt = null;
					} else {
						description += tag + " ";
					}
					continue;
				case PRO:
					if (tag.equals("/pro")) {
						decisionKnowledgeElements
								.add(new DecisionKnowledgeElementImpl(dt, CommitMessageParser.formatDescription(description)));
						recordDescription = false;
						description = "";
						dt = null;
					} else {
						description += tag + " ";
					}
					continue;
				default:
					// This case should never happen.
					continue;
				}
			} else {
				// No -> Check if there is a trigger to start recording
				if (tag.equals("decision")) {
					dt = KnowledgeType.DECISION;
					recordDescription = true;
				} else if (tag.equals("issue")) {
					dt = KnowledgeType.ISSUE;
					recordDescription = true;
				} else if (tag.equals("alternative")) {
					dt = KnowledgeType.ALTERNATIVE;
					recordDescription = true;
				} else if (tag.equals("con")) {
					dt = KnowledgeType.CON;
					recordDescription = true;
				} else if (tag.equals("pro")) {
					dt = KnowledgeType.PRO;
					recordDescription = true;
				} else if (tag.equals("goal")) {
					dt = KnowledgeType.GOAL;
					recordDescription = true;
				}
			}
	
		}
		return decisionKnowledgeElements;
	}

	/**
	 * This function only removes spacebars at the beginning and at the end of the
	 * given String
	 */
	public static String formatDescription(String description) {
		while (description.startsWith(" ") && description.length() > 1) {
			description = description.substring(1);
		}
		while (description.endsWith(" ") && description.length() > 1) {
			description = description.substring(0, description.length() - 1);
		}
		return description;
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
