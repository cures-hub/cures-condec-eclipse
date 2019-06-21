package de.uhd.ifi.se.decision.management.eclipse.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

public class TestConfigPersistenceManager {

	@Test
	public void testConstructor() {
		assertNotNull(new ConfigPersistenceManager());
	}

	@Test
	public void testPathToGit() {
		IPath path = ConfigPersistenceManager.getPathToGit();
		assertNotNull(path);
		assertTrue(path.isAbsolute());
		assertEquals(new Path(".git").makeAbsolute(), path);
	}

	@Test
	public void testSetAndGetBranch() {
		ConfigPersistenceManager.setPreference(ConfigPersistenceManager.BRANCH, "HEAD");
		assertEquals("HEAD", ConfigPersistenceManager.getBranch());
	}

	@Test
	public void testJiraProjectKey() {
		assertEquals("", ConfigPersistenceManager.getProjectKey());
	}

	@Test
	public void testJiraUri() {
		assertEquals(URI.create(""), ConfigPersistenceManager.getJiraUri());
	}

	@Test
	public void testJiraPassword() {
		assertEquals("", ConfigPersistenceManager.getJiraPassword());
	}

	@Test
	public void testJiraUser() {
		assertEquals("", ConfigPersistenceManager.getJiraUser());
	}

	@Test
	public void testLinkDistance() {
		assertEquals(4, ConfigPersistenceManager.getLinkDistance());
	}
}
