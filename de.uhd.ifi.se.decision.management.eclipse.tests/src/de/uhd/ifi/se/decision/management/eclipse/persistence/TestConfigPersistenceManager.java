package de.uhd.ifi.se.decision.management.eclipse.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IPath;
import org.junit.Test;

public class TestConfigPersistenceManager {

	@Test
	public void testPathToGit() {
		IPath path = ConfigPersistenceManager.getPathToGit();
		assertNotNull(path);
		assertTrue(path.isAbsolute());
	}
}
