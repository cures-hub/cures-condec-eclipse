package de.uhd.ifi.se.decision.management.eclipse.model;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestGitCommit {

	@Test
	public void testRevCommitNull() {
		GitCommit gitCommit = GitCommit.getOrCreate(null, "");
		assertNull(gitCommit);
	}
}
