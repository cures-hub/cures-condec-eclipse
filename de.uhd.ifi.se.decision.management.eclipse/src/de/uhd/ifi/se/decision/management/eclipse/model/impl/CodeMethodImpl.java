package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.Node;

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

	@Override
	public Set<Node> getLinkedNodes() {
		Set<Node> linkedNodes = new HashSet<Node>();
		linkedNodes.add(this.getJavaClass());
		return linkedNodes;
	}
}