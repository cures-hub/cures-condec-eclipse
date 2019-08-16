package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PartInitException;
import org.junit.AfterClass;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;

public class TestChangedFile {

	@Test
	public void testExistingNonJavaFile() {
		IPath path = new Path("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		assertTrue(file.exists());
		assertFalse(file.isExistingJavaClass());

		assertEquals(file, ChangedFile.getOrCreate(path));
		assertEquals("File: pom.xml", file.toString());
		assertEquals(path, file.getPath());
		assertTrue(file.getCodeMethods().size() == 0);
	}

	@Test
	public void testNonExistingJavaFile() {
		IPath path = new Path("GodClass.java");
		ChangedFile file = new ChangedFileImpl(path);
		assertFalse(file.isExistingJavaClass());

		assertTrue(file.getCodeMethods().size() == 0);
		assertTrue(file.getCommits().size() == 0);
	}

	@Test
	public void testExistingJavaFile() {
		IPath path = new Path("src/de/uhd/ifi/se/decision/management/eclipse/model/TestChangedFile.java");
		ChangedFile file = new ChangedFileImpl(path);
		assertTrue(file.isExistingJavaClass());

		assertTrue(file.getCodeMethods().size() == 4);
	}
	
	//@Test(expected = PartInitException.class)
	//public void testGoToChangedFileNonExistingJavaFile() {
	//	IPath path = new Path("GodClass.java");
	//	ChangedFile file = new ChangedFileImpl(path);
	//	file.goToChangedFile();
	//}
	
	@Test
	public void testGoToChangedFileExistingJavaFile() {
		IPath path = new Path("src/de/uhd/ifi/se/decision/management/eclipse/model/TestChangedFile.java");
		ChangedFile file = new ChangedFileImpl(path);
		file.goToChangedFile();
		
		//TODO: write assertion
	}
	
	@Test
	public void testGoToChangedFileExistingNonJavaFile() {
		IPath path = new Path("pom.xml");
		ChangedFile file = ChangedFile.getOrCreate(path);
		file.goToChangedFile();
		
		//TODO: write assertion
	}

	@AfterClass
	public static void tearDown() {
		ChangedFile.instances.clear();
		CodeMethod.instances.clear();
	}
}
