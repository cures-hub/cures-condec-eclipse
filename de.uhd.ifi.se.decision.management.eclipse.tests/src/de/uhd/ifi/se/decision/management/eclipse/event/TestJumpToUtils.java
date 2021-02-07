package de.uhd.ifi.se.decision.management.eclipse.event;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;

public class TestJumpToUtils {

	private static JiraClient jiraClient;
	private static GitClient gitClient;

	@BeforeClass
	public static void setUp() {
		jiraClient = TestJiraClient.initJiraClient();
		gitClient = TestGitClient.initGitClient();
	}

	@Ignore
	// there is a invalid thread access exception on Travis CI
	@Test
	public void testJumpToJiraIssue() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		try {
			boolean webbrowserOpened = JumpToUtils.jumpTo(jiraIssue);
			assertTrue(webbrowserOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	@Ignore
	public void testJumpToGitCommit() {
		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
		try {
			boolean commitOpened = JumpToUtils.jumpTo(commits.get(0));
			assertTrue(commitOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	@Ignore
	public void testJumpToChangedFile() {
		IPath path = new Path("src/de/uhd/ifi/se/decision/management/eclipse/model/TestJumpToCommand.java");
		ChangedFile file = new ChangedFileImpl(path);
		try {
			boolean editorOpened = JumpToUtils.jumpTo(file);
			assertTrue(editorOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	@Ignore
	public void testJumpToMethod() {
		IPath path = new Path("GodClass.java");
		ChangedFile file = new ChangedFileImpl(path);
		CodeMethod codeMethod = new CodeMethodImpl("createGraph()", file);
		try {
			boolean methodOpened = JumpToUtils.jumpTo(codeMethod);
			assertTrue(methodOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testJumpToDecisionKnowledgeElement() {
		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
		element.setType(KnowledgeType.DECISION);
		try {
			boolean elementOpened = JumpToUtils.jumpTo(element);
			assertTrue(elementOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void tearDown() {
		JiraClient.instances.clear();
		GitClient.instances.clear();
		JiraIssue.instances.clear();
		GitCommit.instances.clear();
		ChangedFile.instances.clear();
		CodeMethod.instances.clear();
	}
}
