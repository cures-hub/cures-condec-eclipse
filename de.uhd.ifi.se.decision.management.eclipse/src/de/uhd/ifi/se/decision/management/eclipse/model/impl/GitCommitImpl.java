package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;

import com.atlassian.jira.rest.client.domain.Issue;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;
import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeType;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class GitCommitImpl extends NodeImpl implements Node, GitCommit {
	private List<DecisionKnowledgeElementImpl> decisionKnowledgeElements = new ArrayList<DecisionKnowledgeElementImpl>();
	private List<String> issueKeys = new ArrayList<String>();
	private List<Issue> referencedIssues = new ArrayList<Issue>();
	private List<CodeClassImpl> changedClasses = new ArrayList<CodeClassImpl>();
	private RevCommit revCommit;
	private String issueKeyBase = "";
	private static Map<RevCommit, GitCommitImpl> instances = new HashMap<RevCommit, GitCommitImpl>();

	public static Set<GitCommitImpl> getInstances() {
		Set<GitCommitImpl> output = new HashSet<GitCommitImpl>();
		for (Map.Entry<RevCommit, GitCommitImpl> entry : instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	public static GitCommitImpl getOrCreate(RevCommit commit, String issueKeyBase) {
		if (instances.containsKey(commit)) {
			return instances.get(commit);
		} else {
			return new GitCommitImpl(commit, issueKeyBase);
		}
	}

	private GitCommitImpl(RevCommit commit) {
		this(commit, null);
	}

	private GitCommitImpl(RevCommit commit, String issueKeyBase) {
		instances.put(commit, this);
		this.revCommit = commit;
		this.issueKeyBase = issueKeyBase;
		extractIssuesFromCommit(commit);
		this.issueKeys = GitClientImpl.getAllMentionedIssueKeys(commit.getFullMessage(), this.issueKeyBase);
	}

	@Override
	public void setIssueKeyBase(String issueKeyBase) {
		this.issueKeyBase = issueKeyBase;
	}

	// ReferencedIssues-Section
	@Override
	public List<Issue> getReferencedJiraIssues() {
		return this.referencedIssues;
	}

	@Override
	public void setReferencedJiraIssues(List<Issue> referencedIssues) {
		this.referencedIssues = referencedIssues;
	}

	@Override
	public boolean addReferencedIssue(Issue issue) {
		if (this.referencedIssues.contains(issue)) {
			return false;
		} else {
			return this.referencedIssues.add(issue);
		}
	}

	@Override
	public boolean removeReferencedIssue(Issue issue) {
		return this.referencedIssues.remove(issue);
	}
	// END ReferencedIssues-Section

	// CommitDecision-Section
	@Override
	public List<DecisionKnowledgeElementImpl> getCommitDecisions() {
		return this.decisionKnowledgeElements;
	}

	@Override
	public void setCommitDecisions(List<DecisionKnowledgeElementImpl> decisionKnowledgeElements) {
		this.decisionKnowledgeElements = decisionKnowledgeElements;
	}

	@Override
	public boolean addCommitDecision(DecisionKnowledgeElementImpl decisionKnowledgeElement) {
		return this.decisionKnowledgeElements.add(decisionKnowledgeElement);
	}

	@Override
	public boolean removeCommitDecision(DecisionKnowledgeElementImpl decisionKnowledgeElement) {
		return this.decisionKnowledgeElements.remove(decisionKnowledgeElement);
	}
	// END CommitDecision-Section

	// ChangedClasses-Section
	@Override
	public List<CodeClassImpl> getChangedClasses() {
		return this.changedClasses;
	}

	@Override
	public void setChangedClasses(List<CodeClassImpl> changedClasses) {
		this.changedClasses = changedClasses;
	}

	@Override
	public boolean addChangedClass(CodeClassImpl changedClass) {
		return this.changedClasses.add(changedClass);
	}

	@Override
	public boolean removeChangedClass(CodeClassImpl changedClass) {
		return this.changedClasses.remove(changedClass);
	}
	// END CHangedClasses-Section

	// IssueKey-Section
	@Override
	public List<String> getJiraIssueKeys() {
		return this.issueKeys;
	}

	@Override
	public Set<String> getIssueKeysAsSet() {
		Set<String> output = new HashSet<String>();
		for (String ik : this.issueKeys) {
			output.add(ik);
		}
		return output;
	}

	@Override
	public void setJiraIssueKeys(List<String> issueKeys) {
		this.issueKeys = issueKeys;
	}

	@Override
	public boolean addJiraIssueKey(String issueKey) {
		return this.issueKeys.add(issueKey);
	}

	@Override
	public boolean removeIssueKey(String issueKey) {
		return this.issueKeys.remove(issueKey);
	}
	// END IssueKey-Section

	@Override
	public RevCommit getRevCommit() {
		return this.revCommit;
	}

	@Override
	public String toString() {
		return this.revCommit.getShortMessage();
	}

	@Override
	public void extractChangedClasses(GitClient gm) {
		this.changedClasses = gm.getDiffEntries(this);
		for (NodeImpl n : this.changedClasses) {
			this.addLinkedNode(n);
			n.addLinkedNode(this);
		}
	}

	private void extractIssuesFromCommit(RevCommit commit) {
		String commitMessage = commit.getFullMessage();
		// replace("\r\n") is necessary for supporting both Windows-Lineendings AND
		// Unix-Lineendings
		if (commitMessage.contains("[") && commitMessage.contains("[/")) {
			List<String> list = new ArrayList<String>();
			String[] split = commitMessage.split("\\[");
			for (String i : split) {
				for (String j : i.split("\\]")) {
					list.add(j);
				}
			}
			boolean recordDescription = false;
			String description = "";
			KnowledgeType dt = null;
			for (String s : list) {
				String lower_s = s.replace(" ", "").toLowerCase();
				// Already recording?
				// Yes -> Record until there is a trigger to stop
				if (recordDescription) {
					switch (dt) {
					case ALTERNATIVE:
						if (lower_s.equals("/alternative")) {
							decisionKnowledgeElements
									.add(new DecisionKnowledgeElementImpl(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case CON:
						if (lower_s.equals("/con")) {
							decisionKnowledgeElements
									.add(new DecisionKnowledgeElementImpl(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case DECISION:
						if (lower_s.equals("/decision")) {
							decisionKnowledgeElements
									.add(new DecisionKnowledgeElementImpl(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case GOAL:
						if (lower_s.equals("/goal")) {
							decisionKnowledgeElements
									.add(new DecisionKnowledgeElementImpl(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case ISSUE:
						if (lower_s.equals("/issue")) {
							decisionKnowledgeElements
									.add(new DecisionKnowledgeElementImpl(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case PRO:
						if (lower_s.equals("/pro")) {
							decisionKnowledgeElements
									.add(new DecisionKnowledgeElementImpl(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					default:
						// This case should never happen.
						continue;
					}
				} else {
					// No -> Check if there is a trigger to start recording
					if (lower_s.equals("decision")) {
						dt = KnowledgeType.DECISION;
						recordDescription = true;
					} else if (lower_s.equals("issue")) {
						dt = KnowledgeType.ISSUE;
						recordDescription = true;
					} else if (lower_s.equals("alternative")) {
						dt = KnowledgeType.ALTERNATIVE;
						recordDescription = true;
					} else if (lower_s.equals("con")) {
						dt = KnowledgeType.CON;
						recordDescription = true;
					} else if (lower_s.equals("pro")) {
						dt = KnowledgeType.PRO;
						recordDescription = true;
					} else if (lower_s.equals("goal")) {
						dt = KnowledgeType.GOAL;
						recordDescription = true;
					}
				}
				for (DecisionKnowledgeElementImpl cd : decisionKnowledgeElements) {
					this.addLinkedNode(cd);
					cd.addLinkedNode(this);
				}
			}
		}
	}

	/**
	 * This function only removes spacebars at the beginning and at the end of the
	 * given String
	 */
	private String formatDescription(String description) {
		while (description.startsWith(" ") && description.length() > 1) {
			description = description.substring(1);
		}
		while (description.endsWith(" ") && description.length() > 1) {
			description = description.substring(0, description.length() - 1);
		}
		return description;
	}

}