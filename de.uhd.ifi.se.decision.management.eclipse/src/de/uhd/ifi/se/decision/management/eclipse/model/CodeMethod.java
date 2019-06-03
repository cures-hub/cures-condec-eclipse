package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashSet;
import java.util.Set;

public interface CodeMethod extends Node {
	
	public Set<CodeMethod> instances = new HashSet<CodeMethod>();

	public static Set<CodeMethod> getInstances(){
		return instances;
	}

	String getMethodName();

	int getMethodStartInCodefile();

	int getMethodStopInCodefile();

	void setMethodStartInCodefile(int start);

	void setMethodStopInCodefile(int stop);

}