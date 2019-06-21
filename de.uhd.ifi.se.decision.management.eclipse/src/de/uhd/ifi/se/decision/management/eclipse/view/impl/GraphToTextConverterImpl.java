package de.uhd.ifi.se.decision.management.eclipse.view.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.EditList;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.atlassian.jira.rest.client.api.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.CommitMessageParser;
import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.WrongLinkDetector;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.Link;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;
import de.uhd.ifi.se.decision.management.eclipse.persistence.ConfigPersistenceManager;
import de.uhd.ifi.se.decision.management.eclipse.view.GraphToTextConverter;

/**
 * Class responsible to convert a textual representation of the knowledge graph.
 * Used in the DecisionExplorationView and for the ChangeImpactAnalysisView
 */
public class GraphToTextConverterImpl implements GraphToTextConverter {

	private KnowledgeGraph knowledgeGraph;

	public GraphToTextConverterImpl(KnowledgeGraph knowledgeGraph) {
		this.knowledgeGraph = knowledgeGraph;
	}

	public String produceDecisionExploration(int line) {

		Node startNode = knowledgeGraph.getStartNode();

		String graphAsString = "The start node for knowledge exploration is the ";
		int distance = 0;

		BreadthFirstIterator<Node, Link> iterator = new BreadthFirstIterator<Node, Link>(knowledgeGraph, startNode);
		while (iterator.hasNext()) {
			Node node = iterator.next();

			if (iterator.getDepth(node) > distance) {
				distance++;
				graphAsString += "\n At distance " + distance + " the following nodes are linked to "
						+ startNode.toString() + ":\n";
			}

			graphAsString += node.toString() + "\n";
		}

		return graphAsString;
	}

	/**
	 * Produces the content for the ChangeImpactAnalysisView
	 * 
	 * @return change impact String
	 */
	public static String analyzeChange(IPath pathOfFile, int line) {
		double numberOfAnalysedCommitMessages = 0;
		double numberOfCommitsFoundUntangled = 0;

		IPath pathToGit = ConfigPersistenceManager.getPathToGit();
		GitClient gitClient = GitClient.getOrCreate();

		GitCommit commitForLine = gitClient.getCommitForLine(pathOfFile.makeRelativeTo(pathToGit.removeLastSegments(1)),
				line);

		String projectKey = ConfigPersistenceManager.getProjectKey();
		numberOfAnalysedCommitMessages += 1;
		if (WrongLinkDetector.tanglednessToString(commitForLine.getRevCommit(), projectKey).equals("untangled")) {
			numberOfCommitsFoundUntangled += 1;
		}

		Map<DiffEntry, EditList> diffEntriesMappedToEditLists = gitClient.getDiff(commitForLine);

		String changedMethodsInDiff = "";

		for (Map.Entry<DiffEntry, EditList> entry : diffEntriesMappedToEditLists.entrySet()) {
			changedMethodsInDiff += "In file: " + entry.getKey().getNewPath() + "\n"
					+ gitClient.whichMethodsChanged(entry.getKey(), entry.getValue()) + "\n";
		}

		String issueKey = CommitMessageParser.getJiraIssueKey(commitForLine.getRevCommit().getFullMessage());
		JiraClient jiraClient = JiraClient.getOrCreate();

		jiraClient.authenticate();
		JiraIssue issue = jiraClient.getJiraIssue(issueKey);

		int distance = ConfigPersistenceManager.getLinkDistance();
		Map<Issue, Integer> linkedIssuesAtDistance = jiraClient.getLinkedJiraIssuesAtDistance(issue, distance);

		String outputCia2 = "";

		for (Map.Entry<Issue, Integer> linkedIssueAtDistance : linkedIssuesAtDistance.entrySet()) {

			String linkedIssueKey = linkedIssueAtDistance.getKey().getKey();
			List<GitCommit> commitsForLinkedIssue = gitClient.getCommitsForJiraIssue(linkedIssueKey);
			String commitForLinkedIssueString = "";
			if (!commitsForLinkedIssue.isEmpty()) {
				commitForLinkedIssueString = "Commit messages of the issue " + linkedIssueKey + " are:\n";

				for (GitCommit commit : commitsForLinkedIssue) {
					commitForLinkedIssueString += commitToString(commit.getRevCommit().getFullMessage()) + "\n";
					numberOfAnalysedCommitMessages += 1;
					if (WrongLinkDetector.tanglednessToString(commit.getRevCommit().getFullMessage(), projectKey)
							.equals("untangled")) {
						numberOfCommitsFoundUntangled += 1;
					}
					Map<DiffEntry, EditList> diffEntriesMappedToEditListsBroad = gitClient.getDiff(commit);

					for (Map.Entry<DiffEntry, EditList> entry : diffEntriesMappedToEditListsBroad.entrySet()) {

						String newPath = entry.getKey().getNewPath();
						commitForLinkedIssueString += "In file: " + newPath + "\n";
						String explainDiff = gitClient.whichMethodsChanged(entry.getKey(), entry.getValue());
						if (explainDiff.length() != 0) {
							commitForLinkedIssueString += explainDiff;
						} else {
							commitForLinkedIssueString += "A change, that concerns comments or imports.\n";
						}
					}
				}
			}
			outputCia2 += "\n" + "The issue with the ID " + linkedIssueAtDistance.getKey().getKey()
					+ " said at link distance " + linkedIssueAtDistance.getValue() + ":\n"
					+ linkedIssueAtDistance.getKey().getSummary() + "\n" + commitForLinkedIssueString;
		}

		double untangledness = 1.0;
		double untanglednessPrepared = 1.0;
		if (numberOfAnalysedCommitMessages != 0) {
			untangledness = numberOfCommitsFoundUntangled / numberOfAnalysedCommitMessages;

			BigDecimal bigdeci = new BigDecimal(Double.toString(untangledness));
			bigdeci = bigdeci.setScale(2, RoundingMode.HALF_UP);
			untanglednessPrepared = bigdeci.doubleValue() * 100;
		}

		String generatedForView = "That same commit also changed:\n" + changedMethodsInDiff + "\n"
				+ "Using knowledge about the commits, which were linked to the issues found during decision exploration\n"
				+ "the plugin also found out, that:\n" + "\n" + outputCia2 + "\n" + untanglednessPrepared + "%"
				+ " of the commits, that were the input in this change impact analysis were untangled.";

		return generatedForView;
	}

	public static String commitToString(String commitMessage) {
		String projectKey = ConfigPersistenceManager.getProjectKey();
		return commitMessage + " (" + WrongLinkDetector.tanglednessToString(commitMessage, projectKey) + ")\n";
	}

}
