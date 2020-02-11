package de.uhd.ifi.se.decision.management.eclipse.model;

/**
 * Type of node.
 */
public enum NodeType {
	CHANGED_FILE("File"),
	CODE_METHOD("Method"),
	GIT_COMMIT("Git Commit"),
	JIRA_ISSUE("Jira Issue"),
	DECISION_KNOWLEDGE_ELEMENT("Decision Knowledge Element"), 
	OTHER("");
	
	private final String name;

	private NodeType(String name) {
		this.name = name;
	}

	/**
	 * Returns the node type for a String.
	 * 
	 * @param name
	 *            node type name as a String.
	 * @return node type of a node.
	 */
	public static NodeType getKnowledgeType(String name) {
		for (NodeType knowledgeType : values()) {
			if (knowledgeType.name.equalsIgnoreCase(name)) {
				return knowledgeType;
			}
		}
		return NodeType.OTHER;
	}

	/**
	 * Returns the node type as a String starting with a capital letter, e.g.,
	 * File or Method.
	 *
	 * @return node type as a String starting with a capital letter.
	 */
	public String getName() {
		return this.name;
	}
}
