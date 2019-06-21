package de.uhd.ifi.se.decision.management.eclipse.view;

import de.uhd.ifi.se.decision.management.eclipse.model.KnowledgeGraph;

/**
 * Interface responsible to convert a textual representation of the knowledge
 * graph.
 * 
 * @see KnowledgeGraph
 */
public interface GraphToTextConverter {

	/**
	 * Produces the content for the DecisionExplorationView
	 * 
	 * @return decision exploration String
	 */
	String produceDecisionExploration(int line);

}