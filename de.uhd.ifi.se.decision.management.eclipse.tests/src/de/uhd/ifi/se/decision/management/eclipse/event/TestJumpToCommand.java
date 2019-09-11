package de.uhd.ifi.se.decision.management.eclipse.event;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
//import de.uhd.ifi.se.decision.management.eclipse.extraction.TestGitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.TestJiraClient;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.JiraIssue;

public class TestJumpToCommand {
	
	private static JiraClient jiraClient;
	//private static GitClient gitClient;
	
	@BeforeClass
	public static void setUp() {
		jiraClient = TestJiraClient.initJiraClient();
		//gitClient = TestGitClient.initGitClient();
	}

	@Ignore
	@Test
	public void testJumpToJiraIssue() {
		JiraIssue jiraIssue = JiraIssue.getOrCreate("ECONDEC-1", jiraClient);
		try {
			boolean webbrowserOpened = JumpToCommand.jumpToJiraIssue(jiraIssue);
			assertTrue(webbrowserOpened);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
//	@Test
//	public void testJumpToGitCommit() {
//		List<GitCommit> commits = gitClient.getCommitsForJiraIssue("ECONDEC-1");
//		try {
//			boolean commitOpened = JumpToCommand.jumpToGitCommit(commits.get(0));
//			assertTrue(commitOpened);
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
	
//	@Test
//	public void testJumpToChangedFile() {
//		IPath path = new Path("src/de/uhd/ifi/se/decision/management/eclipse/model/TestJumpToCommand.java");
//		ChangedFile file = new ChangedFileImpl(path);
//		try {
//			boolean editorOpened = JumpToCommand.jumpToChangedFile(file);
//			assertTrue(editorOpened);
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
	
//	@Test
//	public void testJumpToMethod() {
//		IPath path = new Path("GodClass.java");
//		ChangedFile file = new ChangedFileImpl(path);
//		CodeMethod codeMethod = new CodeMethodImpl("createGraph()", file);
//		try {
//			boolean methodOpened = JumpToCommand.jumpToMethod(codeMethod);
//			assertTrue(methodOpened);
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
	
//	@Test
//	public void testJumpToDecisionKnowledgeElement() {
//		DecisionKnowledgeElement element = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
//				"This is a decision!");
//		element.setType(KnowledgeType.DECISION);
//		try {
//			boolean elementOpened = JumpToCommand.jumpToDecisionKnowledgeElement(element);
//			assertTrue(elementOpened);
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
//	}
	
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