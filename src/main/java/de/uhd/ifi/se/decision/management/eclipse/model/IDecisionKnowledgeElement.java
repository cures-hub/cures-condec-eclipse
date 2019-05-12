package de.uhd.ifi.se.decision.management.eclipse.model;

public interface IDecisionKnowledgeElement extends Node {
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
	 * Get the description of the decision knowledge element. The description
	 * provides details about the element. When using JIRA issues to persist
	 * decision knowledge, it can include images and other fancy stuff.
	 *
	 * @return description of the decision knowledge element.
	 */
	String getDescription();

	/**
	 * Set the description of the decision knowledge element. The description
	 * provides details about the element. When using JIRA issues to persist
	 * decision knowledge, it can include images and other fancy stuff.
	 *
	 * @param description
	 *            of the decision knowledge element.
	 */
	void setDescription(String description);
}
