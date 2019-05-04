package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.util.HashSet;
import java.util.Set;

import de.uhd.ifi.se.decision.management.eclipse.model.Node;

public class CodeMethod extends Node {
	private String methodName = "";
	private int methodStart = 0;
	private int methodStop = 0;
	private static Set<CodeMethod> instances = new HashSet<CodeMethod>();
	
	public CodeMethod(String methodName) {
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
	
	public String getMethodName() {
		return this.methodName;
	}
	
	public int getMethodStartInCodefile() {
		return this.methodStart;
	}
	
	public int getMethodStopInCodefile() {
		return this.methodStop;
	}
	
	public void setMethodStartInCodefile(int start) {
		this.methodStart = start;
	}
	
	public void setMethodStopInCodefile(int stop) {
		this.methodStop = stop;
	}
	
}
