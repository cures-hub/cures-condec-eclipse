package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.Path;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
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
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
        
        Link link = new LinkImpl(node1, node2);
        
        assertNotNull(link);
        assertTrue(link.getSourceNode() == node1);
        assertTrue(link.getTargetNode() == node2);
	}
	
	@Test
	public void testConstructorNull() {
        Link link = new LinkImpl(null, null);
        
        assertNotNull(link);
        assertNull(link.getSourceNode());
        assertNull(link.getTargetNode());
	}
	
	@Test
	public void testGetSetNode() {
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
        
        Link link = new LinkImpl();
        
        link.setSourceNode(node1);
        link.setTargetNode(node2);
        
        assertTrue(link.getSourceNode() == node1);
        assertTrue(link.getTargetNode() == node2);
	}
	
	@Test
	public void testGetSetNodeNull() {
        Link link = new LinkImpl();
        
        link.setSourceNode(null);
        link.setTargetNode(null);
        
        assertNull(link.getSourceNode());
        assertNull(link.getTargetNode());
	}
	
	@Test
	public void testGetNodeID() {
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
        
        Link link = new LinkImpl();
        
        link.setSourceNode(node1);
        link.setTargetNode(node2);
        
        assertTrue(link.getSourceId().contains("file1"));
        assertTrue(link.getTargetId().contains("file2"));
	}
	
	@Test
	public void testEqualsTrue() {
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
		ChangedFile node3 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node4 = new ChangedFileImpl(new Path("./file2"));
        
        Link link1 = new LinkImpl();
        Link link2 = new LinkImpl();
        
        link1.setSourceNode(node1);
        link1.setTargetNode(node2);
        link2.setSourceNode(node3);
        link2.setTargetNode(node4);
        
        assertTrue(link1.equals(link2));
	}
	
	@Test
	public void testEqualsFalse() {
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
		ChangedFile node3 = new ChangedFileImpl(new Path("./file3"));
		ChangedFile node4 = new ChangedFileImpl(new Path("./file4"));
        
        Link link1 = new LinkImpl();
        Link link2 = new LinkImpl();
        
        link1.setSourceNode(node1);
        link1.setTargetNode(node2);
        link2.setSourceNode(node3);
        link2.setTargetNode(node4);
        
        assertFalse(link1.equals(link2));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsNolink() {
		ChangedFile node1 = new ChangedFileImpl(new Path("./file1"));
		ChangedFile node2 = new ChangedFileImpl(new Path("./file2"));
		ChangedFile node3 = new ChangedFileImpl(new Path("./file3"));
        
        Link link1 = new LinkImpl();
        
        link1.setSourceNode(node1);
        link1.setTargetNode(node2);
        
        assertFalse(link1.equals(node3));
	}
}
