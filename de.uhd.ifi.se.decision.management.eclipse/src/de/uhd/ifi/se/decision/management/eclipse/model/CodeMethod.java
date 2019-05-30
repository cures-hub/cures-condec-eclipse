package de.uhd.ifi.se.decision.management.eclipse.model;

public interface CodeMethod extends Node {

	String getMethodName();

	int getMethodStartInCodefile();

	int getMethodStopInCodefile();

	void setMethodStartInCodefile(int start);

	void setMethodStopInCodefile(int stop);

}