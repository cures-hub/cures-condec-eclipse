package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Interface for methods as part of the knowledge graph.
 */
public interface CodeMethod extends Node {

	/**
	 * Instances of CodeMethod.
	 */
	public Set<CodeMethod> instances = new HashSet<CodeMethod>();

	/**
	 * Returns all available instances of CodeMethod.
	 * 
	 * @return instances of CodeMethod.
	 */
	public static Set<CodeMethod> getInstances() {
		return instances;
	}

	/**
	 * Returns the method name.
	 * 
	 * @return method name.
	 */
	String getMethodName();

	/**
	 * Returns the Java class that the method belongs to.
	 * 
	 * @return Java class that the method belongs to as a {@link ChangedFile} object.
	 */
	ChangedFile getJavaClass();
}