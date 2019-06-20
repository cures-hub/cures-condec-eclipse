package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;

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
		assertEquals("createGraph()", codeMethod.toString());
		assertEquals(file, codeMethod.getJavaClass());
	}

	@AfterClass
	public static void tearDown() {
		CodeMethod.instances.clear();
		ChangedFile.instances.clear();
	}

}
