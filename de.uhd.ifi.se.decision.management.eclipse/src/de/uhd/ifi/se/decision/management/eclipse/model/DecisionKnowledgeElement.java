package de.uhd.ifi.se.decision.management.eclipse.model;

/**
 * Interface for decision knowledge elements.
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
	 * Set the id of the decision knowledge element. This id is the internal
	 * database id. When using JIRA issues to persist decision knowledge, this id is
	 * different to the project internal id that is part of the key.
	 *
	 * @param id
	 *            of the decision knowledge element.
	 */
	@Override
	void setId(long id);

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
}
