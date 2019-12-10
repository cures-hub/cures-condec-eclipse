package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.DecisionKnowledgeElementImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.LinkImpl;

public class TestLink {

	@Test
	public void testConstructor() {
		Link link = new LinkImpl();
		assertNotNull(link);
		assertNull(link.getSource());
		assertNull(link.getTarget());
	}
	
	@Test
	public void testConstructorFull() {
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
        DecisionKnowledgeElement node2 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is also a decision!");
        
        Link link = new LinkImpl(node1, node2);
        
        assertNotNull(link);
        assertTrue(link.getSourceNode() == node1);
        assertTrue(link.getTargetNode() == node2);
	}
	
	@Test
	public void testGetSetNode() {
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
        DecisionKnowledgeElement node2 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is also a decision!");
        
        Link link = new LinkImpl();
        
        link.setSourceNode(node1);
        link.setTargetNode(node2);
        
        assertTrue(link.getSourceNode() == node1);
        assertTrue(link.getTargetNode() == node2);
	}
	
	@Test
	public void testGetNodeID() {
		DecisionKnowledgeElement node1 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is a decision!");
        DecisionKnowledgeElement node2 = new DecisionKnowledgeElementImpl(KnowledgeType.ISSUE,
				"This is also a decision!");
        
        Link link = new LinkImpl();
        
        link.setSourceNode(node1);
        link.setTargetNode(node2);
        
        assertTrue(link.getSourceID().contains("This is a decision!"));
        assertTrue(link.getTargetID().contains("This is also a decision!"));
	}
}
