package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

/**
 * Class for methods as part of the knowledge graph.
 */
public class CodeMethodImpl extends NodeImpl implements CodeMethod {
	private ChangedFile javaClass;
	private String methodName;

	public CodeMethodImpl(String methodName, ChangedFile javaClass) {
		this.methodName = methodName;
		this.javaClass = javaClass;
		instances.add(this);
	}

	@Override
	public String toString() {
		return methodName;
	}

	@Override
	public String getMethodName() {
		return methodName;
	}

	@Override
	public ChangedFile getJavaClass() {
		return javaClass;
	}
}