package de.uhd.ifi.se.decision.management.eclipse.model;

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
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElement;

public class GitCommit extends Node implements INode {
	private List<DecisionKnowledgeElement> decisionKnowledgeElements = new ArrayList<DecisionKnowledgeElement>();
	private List<IssueKey> issueKeys = new ArrayList<IssueKey>();
	private List<Issue> referencedIssues = new ArrayList<Issue>();
	private List<CodeClass> changedClasses = new ArrayList<CodeClass>();
	private RevCommit revCommit;
	private String issueKeyBase = "";
	private static Map<RevCommit, GitCommit> instances = new HashMap<RevCommit, GitCommit>(); 

	public static Set<GitCommit> getInstances(){
		Set<GitCommit> output = new HashSet<GitCommit>();
		for(Map.Entry<RevCommit, GitCommit> entry:instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}
	
	public static GitCommit getOrCreate(RevCommit commit, String issueKeyBase) {
		if(instances.containsKey(commit)) {
			return instances.get(commit);
		} else {
			return new GitCommit(commit, issueKeyBase);
		}
	}
	
	private GitCommit(RevCommit commit) {
		this(commit, null);
	}
	
	private GitCommit(RevCommit commit, String issueKeyBase) {
		instances.put(commit, this);
		this.revCommit = commit;
		this.issueKeyBase = issueKeyBase;
		extractIssuesFromCommit(commit);
		this.issueKeys = GitClientImpl.getAllMentionedIssueKeys(commit.getFullMessage(), this.issueKeyBase);
	}
	
	public void setIssueKeyBase(String issueKeyBase) {
		this.issueKeyBase = issueKeyBase;
	}
	
	// ReferencedIssues-Section
	public List<Issue> getReferencedIssues(){
		return this.referencedIssues;
	}
	
	public void setReferencedIssues(List<Issue> referencedIssues) {
		this.referencedIssues = referencedIssues;
	}
	
	public boolean addReferencedIssue(Issue issue) {
		if(this.referencedIssues.contains(issue)) {
			return false;
		} else {
			return this.referencedIssues.add(issue);
		}
	}
	
	public boolean removeReferencedIssue(Issue issue) {
		return this.referencedIssues.remove(issue);
	}
	// END ReferencedIssues-Section
	
	// CommitDecision-Section
	public List<DecisionKnowledgeElement> getCommitDecisions(){
		return this.decisionKnowledgeElements;
	}
	
	public void setCommitDecisions(List<DecisionKnowledgeElement> decisionKnowledgeElements) {
		this.decisionKnowledgeElements = decisionKnowledgeElements;
	}
	
	public boolean addCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement) {
		return this.decisionKnowledgeElements.add(decisionKnowledgeElement);
	}
	
	public boolean removeCommitDecision(DecisionKnowledgeElement decisionKnowledgeElement) {
		return this.decisionKnowledgeElements.remove(decisionKnowledgeElement);
	}
	// END CommitDecision-Section
	
	// ChangedClasses-Section
	public List<CodeClass> getChangedClasses(){
		return this.changedClasses;
	}
	
	public void setChangedClasses(List<CodeClass> changedClasses) {
		this.changedClasses = changedClasses;
	}
	
	public boolean addChangedClass(CodeClass changedClass) {
		return this.changedClasses.add(changedClass);
	}
	
	public boolean removeChangedClass(CodeClass changedClass) {
		return this.changedClasses.remove(changedClass);
	}
	// END CHangedClasses-Section
	
	// IssueKey-Section
	public List<IssueKey> getIssueKeys(){
		return this.issueKeys;
	}
	
	public Set<IssueKey> getIssueKeysAsSet(){
		Set<IssueKey> output = new HashSet<IssueKey>();
		for(IssueKey ik : this.issueKeys) {
			output.add(ik);
		}
		return output;
	}
	
	public void setIssueKeys(List<IssueKey> issueKeys) {
		this.issueKeys = issueKeys;
	}
	
	public boolean addIssueKey(IssueKey issueKey) {
		return this.issueKeys.add(issueKey);
	}
	
	public boolean removeIssueKey(IssueKey issueKey) {
		return this.issueKeys.remove(issueKey);
	}
	// END IssueKey-Section
	
	public RevCommit getBindedRevCommit() {
		return this.revCommit;
	}
	
	
	@Override
	public String toString() {
		return this.revCommit.getShortMessage();
	}
	
	public void extractChangedClasses(GitClient gm) {
		this.changedClasses = gm.getDiffEntries(this);
		for(INode n : this.changedClasses) {
			this.addLinkedNode(n);
			n.addLinkedNode(this);
		}
	}
	
	
	private void extractIssuesFromCommit(RevCommit commit) {
		String commitMessage = commit.getFullMessage();
		// replace("\r\n") is necessary for supporting both Windows-Lineendings AND Unix-Lineendings
		if(commitMessage.contains("[") && commitMessage.contains("[/")) {
			List<String> list = new ArrayList<String>();
			String[] split = commitMessage.split("\\[");
	        for(String i : split){
	            for(String j : i.split("\\]")){
	                list.add(j);
	            }
	        }
	        boolean recordDescription = false;
	        String description = "";
	        KnowledgeType dt = null;
			for(String s : list) {
				String lower_s = s.replace(" ", "").toLowerCase();
				// Already recording?
				// Yes -> Record until there is a trigger to stop
				if(recordDescription) {
					switch(dt) {
					case ALTERNATIVE:
						if(lower_s.equals("/alternative")) {
							decisionKnowledgeElements.add(new DecisionKnowledgeElement(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case CON:
						if(lower_s.equals("/con")) {
							decisionKnowledgeElements.add(new DecisionKnowledgeElement(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case DECISION:
						if(lower_s.equals("/decision")) {
							decisionKnowledgeElements.add(new DecisionKnowledgeElement(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case GOAL:
						if(lower_s.equals("/goal")) {
							decisionKnowledgeElements.add(new DecisionKnowledgeElement(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case ISSUE:
						if(lower_s.equals("/issue")) {
							decisionKnowledgeElements.add(new DecisionKnowledgeElement(dt, formatDescription(description)));
							recordDescription = false;
							description = "";
							dt = null;
						} else {
							description += s;
						}
						continue;
					case PRO:
						if(lower_s.equals("/pro")) {
							decisionKnowledgeElements.add(new DecisionKnowledgeElement(dt, formatDescription(description)));
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
					if(lower_s.equals("decision")) {
						dt = KnowledgeType.DECISION;
						recordDescription = true;
					} else if(lower_s.equals("issue")) {
						dt = KnowledgeType.ISSUE;
						recordDescription = true;
					} else if(lower_s.equals("alternative")) {
						dt = KnowledgeType.ALTERNATIVE;
						recordDescription = true;
					} else if(lower_s.equals("con")) {
						dt = KnowledgeType.CON;
						recordDescription = true;
					} else if(lower_s.equals("pro")) {
						dt = KnowledgeType.PRO;
						recordDescription = true;
					} else if(lower_s.equals("goal")) {
						dt = KnowledgeType.GOAL;
						recordDescription = true;
					}
				}
				for(DecisionKnowledgeElement cd : decisionKnowledgeElements) {
					this.addLinkedNode(cd);
					cd.addLinkedNode(this);
				}
			}
		}
	}
	
	/**
	 * This function only removes spacebars at the beginning and at the end of the given String 
	 */
	private String formatDescription(String description) {
		while(description.startsWith(" ") && description.length() > 1) {
			description = description.substring(1);
		}
		while(description.endsWith(" ") && description.length() > 1) {
			description = description.substring(0, description.length() - 1);
		}
		return description;
	}



}
