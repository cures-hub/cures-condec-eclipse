package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;

public class TestCodeMethod {

	@Test
	public void testConstructor() {
		CodeMethod codeMethod = new CodeMethodImpl("createGraph()");
		assertEquals("createGraph()", codeMethod.getMethodName());
		assertEquals("createGraph()", codeMethod.toString());
		
		CodeMethod.instances.clear();
	}

}
