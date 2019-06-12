package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

/**
 * Class for methods as part of the knowledge graph.
 */
public class CodeMethodImpl extends NodeImpl implements CodeMethod {
	private String methodName;

	public CodeMethodImpl(String methodName) {
		this.methodName = methodName;
		instances.add(this);
	}

	@Override
	public String toString() {
		return this.methodName;
	}

	@Override
	public String getMethodName() {
		return this.methodName;
	}
}