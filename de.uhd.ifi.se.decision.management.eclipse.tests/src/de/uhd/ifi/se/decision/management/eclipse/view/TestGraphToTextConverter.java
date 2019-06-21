package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.junit.Before;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.KnowledgeGraphImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.impl.GraphToTextConverterImpl;

public class TestGraphToTextConverter {

	private GitClient gitClient;
	private JiraClient jiraClient;

	@Before
	public void setUp() {
		gitClient = TestGitClient.initGitClient();
		jiraClient = TestJiraClient.initJiraClient();
	}

	@Test
	public void createString() {
		IPath path = gitClient.getPath().removeLastSegments(1).append("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		KnowledgeGraph knowledgeGraph = new KnowledgeGraphImpl(gitClient, jiraClient, file, 0);
		GraphToTextConverter converter = new GraphToTextConverterImpl(knowledgeGraph);
		String graphAsString = converter.produceDecisionExploration(1);
		assertTrue(graphAsString.startsWith("The start node for knowledge exploration is the File: pom.xml"));
	}

}
