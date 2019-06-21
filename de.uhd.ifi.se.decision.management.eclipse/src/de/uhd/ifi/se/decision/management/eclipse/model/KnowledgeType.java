package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.Locale;

/**
 * Type of decision knowledge element.
 */
public enum KnowledgeType {
	DECISION("decision"), ISSUE("issue"), ALTERNATIVE("alternative"), CON("con"), PRO("pro"), GOAL("goal"), OTHER("");

	private final String name;

	private KnowledgeType(String name) {
		this.name = name;
	}

	/**
	 * Returns the knowledge type for a String.
	 * 
	 * @param name
	 *            knowledge type name as a String.
	 * @return knowledge type of a decision knowledge element.
	 */
	public static KnowledgeType getKnowledgeType(String name) {
		for (KnowledgeType knowledgeType : values()) {
			if (knowledgeType.name.equalsIgnoreCase(name)) {
				return knowledgeType;
			}
		}
		return KnowledgeType.OTHER;
	}

	/**
	 * Returns the knowledge type as a String starting with a capital letter, e.g.,
	 * Argument, Decision, or Alternative.
	 *
	 * @return knowledge type as a String starting with a capital letter.
	 */
	public String getName() {
		return this.name().substring(0, 1).toUpperCase(Locale.ENGLISH)
				+ this.name().substring(1).toLowerCase(Locale.ENGLISH);
	}

}
