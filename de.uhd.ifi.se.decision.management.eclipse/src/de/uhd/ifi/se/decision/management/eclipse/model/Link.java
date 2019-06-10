package de.uhd.ifi.se.decision.management.eclipse.model;

public interface Link {
	
	Node getSource();

	Node getTarget();

	double getWeight();

}
