package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

public class CodeMethodImpl extends NodeImpl implements CodeMethod {
	private String methodName = "";
	private int methodStart = 0;
	private int methodStop = 0;
	private static Set<CodeMethod> instances = new HashSet<CodeMethod>();
	
	public CodeMethodImpl(String methodName) {
		this.methodName = methodName;
		instances.add(this);
	}
	
	public static Set<CodeMethod> getInstances(){
		return instances;
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
