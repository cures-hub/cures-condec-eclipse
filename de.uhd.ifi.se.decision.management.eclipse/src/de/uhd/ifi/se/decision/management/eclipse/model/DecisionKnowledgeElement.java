package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.Set;

/**
 * Interface for decision knowledge elements as part of the knowledge graph.
 */
public interface DecisionKnowledgeElement extends Node {

	/**
	 * Get the id of the decision knowledge element. This id is the internal
	 * database id. When using JIRA issues to persist decision knowledge, this id is
	 * different to the project internal id that is part of the key.
	 *
	 * @return id of the decision knowledge element.
	 */
	@Override
	long getId();

	/**
	 * Get the summary of the decision knowledge element. The summary is a short
	 * description of the element.
	 *
	 * @return summary of the decision knowledge element.
	 */
	String getSummary();

	/**
	 * Set the summary of the decision knowledge element. The summary is a short
	 * description of the element.
	 *
	 * @param summary
	 *            of the decision knowledge element.
	 */
	void setSummary(String summary);

	/**
	 * Get the type of the decision knowledge element. For example, prominent types
	 * are decision, alternative, issue, and argument.
	 *
	 * @see KnowledgeType
	 * @return type of the decision knowledge element.
	 */
	KnowledgeType getType();

	/**
	 * Set the type of the decision knowledge element. For example, prominent types
	 * are decision, alternative, issue, and argument.
	 *
	 * @see KnowledgeType
	 * @param type
	 *            of the decision knowledge element.
	 */
	void setType(KnowledgeType type);

	/**
	 * Returns decision knowledge elements linked to this element.
	 * 
	 * @return set of all decision knowledge elements linked to this element.
	 */
	Set<DecisionKnowledgeElement> getLinkedDecisionKnowledgeElements();

	/**
	 * Links a decision knowledge element to this element.
	 * 
	 * @param element
	 *            {@link DecisionKnowledgeElement} object.
	 * 
	 * @return true if element was linked.
	 */
	boolean addLinkedDecisionKnowledgeElement(DecisionKnowledgeElement element);
	
	/**
	 * Get the git commit this decision knowledge element is from.
	 *
	 * @see GitCommit
	 * @return git commit this decision knowledge element is from.
	 */
	GitCommit getCommit();
	
	/**
	 * Set the git commit this decision knowledge element is from
	 *
	 * @see GitCommit
	 * @param commit
	 *            this decision knowledge element is from.
	 */
	void setCommit(GitCommit commit);
}
