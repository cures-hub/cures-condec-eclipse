package de.uhd.ifi.se.decision.management.eclipse.model;

public interface ICodeClass extends INode {
	
	String getClassName();

	String getPackage();
	
	String getProject();
	
	String getFullClassPath();
}
