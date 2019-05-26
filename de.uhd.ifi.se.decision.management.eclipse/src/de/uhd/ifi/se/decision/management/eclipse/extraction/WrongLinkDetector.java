package de.uhd.ifi.se.decision.management.eclipse.extraction;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Analyzes commit messages for hints, whether or not the corresponding commit
 * is tangled
 */
public class WrongLinkDetector {

	/**
	 * Judges a commit message, untangledness resembles 1, tangling/wrong link
	 * resembles 0
	 * 
	 * @param commitMessage
	 *            the commit message to be analyzed
	 * @param projectKey
	 *            the project key of the JIRA project
	 * @return String returns the verdict
	 */
	public static String tanglednessToString(String commitMessage, String projectKey) {
		String tangledness = "heavily tangled";

		double allVerdictsCombined = assessTangledness(commitMessage, projectKey);
		allVerdictsCombined = allVerdictsCombined / 4;

		if (allVerdictsCombined >= 0.5) {
			tangledness = "tangled";
		}

		if (allVerdictsCombined >= 0.75) {
			tangledness = "slightly tangled";
		}

		if (allVerdictsCombined >= 0.95) {
			tangledness = "untangled";
		}

		return tangledness;
	}
	
	public static String tanglednessToString(RevCommit commit, String projectKey) {
		return tanglednessToString(commit.getFullMessage(), projectKey);
	}

	/**
	 * Judges a commit message, untangledness resembles 1, tangling/wrong link
	 * resembles 0
	 * 
	 * @param commitMessage
	 *            the commit message to be analyzed
	 * @param projectKey
	 *            the project key of the JIRA project
	 * @return String returns the verdict
	 */
	public static double assessTangledness(String commitMessage, String projectKey) {
		return hasMultipleIds(commitMessage, projectKey) + includesAnd(commitMessage) + includesComma(commitMessage);
	}

	/**
	 * Looks if the commit message contains multiple issue keys, weight is 2/4
	 * 
	 * @param message
	 *            the commit message to be analyzed
	 * @param projectKey
	 *            the project key currently used in the JIRA project
	 * @return value that predicts whether the the link is wrong
	 */
	public static double hasMultipleIds(String message, String projectKey) {

		// guess: is not tangled
		double judge = 2;

		int numberOfIssueKeys = message.split(projectKey).length;

		if (numberOfIssueKeys > 2) {
			judge = 0.5;
		}

		if (numberOfIssueKeys > 3) {
			judge = 0;
		}

		return judge;
	}

	/**
	 * Looks if the commit message contains the word "and", weight is 1/4
	 * 
	 * @param message
	 *            the commit message
	 * @return double returns a value that can be interpreted
	 */
	public static double includesAnd(String message) {

		// guess is: not tangled
		double judge = 1;

		if (message.toLowerCase().contains(" and".toLowerCase())) {
			judge = 0;
		}

		return judge;
	}

	/**
	 * Looks if the commit message contains a comma, weight is 1/4
	 * 
	 * @param message
	 *            the commit message
	 * @return double returns a value that can be interpreted
	 */
	public static double includesComma(String message) {

		double judge = 1;

		if (message.contains(",")) {
			judge = 0;
		}

		return judge;
	}
}
