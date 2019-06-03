package de.uhd.ifi.se.decision.management.eclipse.model;

/**
 * Type of decision knowledge element.
 */
public enum KnowledgeType {
	DECISION("decision"), ISSUE("issue"), ALTERNATIVE("alternative"), CON("con"), PRO("pro"), GOAL("goal"), OTHER("");

	public final String name;

	private KnowledgeType(String name) {
		this.name = name;
	}

	public static KnowledgeType getKnowledgeType(String name) {
		for (KnowledgeType knowledgeType : values()) {
			if (knowledgeType.name.equalsIgnoreCase(name)) {
				return knowledgeType;
			}
		}
		return KnowledgeType.OTHER;
	}

}
