package de.uhd.ifi.se.decision.management.eclipse.model;

public interface ICodeClass extends Node {
	
	String getClassName();

	String getPackage();
	
	String getProject();
	
	String getFullClassPath();
}
