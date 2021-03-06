package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;
import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeMethodImpl;

public class TestCodeMethod {

	@Test
	public void testConstructor() {
		IPath path = new Path("GodClass.java");
		ChangedFile file = new ChangedFileImpl(path);
		CodeMethod codeMethod = new CodeMethodImpl("createGraph()", file);
		
		assertEquals("createGraph()", codeMethod.getMethodName());
		assertEquals("Method: createGraph()", codeMethod.toString());
		assertEquals(file, codeMethod.getJavaClass());
	}
	
	@Test
	public void testGetNodeId() {
		IPath path = new Path("src/de/uhd/ifi/se/decision/management/eclipse/model/TestChangedFile.java");
		ChangedFile file = new ChangedFileImpl(path);
		CodeMethod codeMethod = new CodeMethodImpl("createGraph()", file);
		
		assertTrue(codeMethod.getNodeId().equals("src/de/uhd/ifi/se/decision/management/eclipse/model/TestChangedFile.java createGraph()"));
	}

	@AfterClass
	public static void tearDown() {
		CodeMethod.instances.clear();
		ChangedFile.instances.clear();
	}

}
