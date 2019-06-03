package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

public class CodeMethodImpl extends NodeImpl implements CodeMethod {
	private String methodName = "";
	private int methodStart = 0;
	private int methodStop = 0;

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

	@Override
	public int getMethodStartInCodefile() {
		return this.methodStart;
	}

	@Override
	public int getMethodStopInCodefile() {
		return this.methodStop;
	}

	@Override
	public void setMethodStartInCodefile(int start) {
		this.methodStart = start;
	}

	@Override
	public void setMethodStopInCodefile(int stop) {
		this.methodStop = stop;
	}
}