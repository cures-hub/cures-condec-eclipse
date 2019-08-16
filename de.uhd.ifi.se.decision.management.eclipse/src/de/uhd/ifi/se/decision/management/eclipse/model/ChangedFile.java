package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.diff.DiffEntry;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.ChangedFileImpl;

/**
 * Interface for files as part of the knowledge graph.
 */
public interface ChangedFile extends Node {

	/**
	 * Instances of ChangedFile that are identified by the path to the file.
	 */
	public Map<IPath, ChangedFile> instances = new HashMap<IPath, ChangedFile>();

	public static ChangedFile getOrCreate(DiffEntry diffEntry, IPath pathToGit) {
		IPath newPath = new Path(diffEntry.getNewPath());
		IPath path = pathToGit.removeLastSegments(1).append(newPath);
		return getOrCreate(path);
	}

	public static ChangedFile getOrCreate(IPath path) {
		if (instances.containsKey(path)) {
			return instances.get(path);
		}
		ChangedFile codeFile = new ChangedFileImpl(path);
		instances.put(path, codeFile);
		return codeFile;
	}

	public static Set<ChangedFile> getInstances() {
		return new HashSet<ChangedFile>(instances.values());
	}

	/**
	 * Returns the path of the file.
	 * 
	 * @see IPath
	 * @return path of the file.
	 */
	IPath getPath();

	/**
	 * Returns the last segment of the path of the file, which should be its name.
	 * 
	 * @return name of the file.
	 */
	String getFileName();

	/**
	 * Returns a set of methods as {@link CodeMethod} objects if the file is a Java
	 * class.
	 * 
	 * @return set of methods. Returns an empty list if the file is not existing
	 *         (anymore) or if it is not a Java class.
	 */
	Set<CodeMethod> getCodeMethods();

	/**
	 * Returns true if the file exists in currently checked out version of the git
	 * repository. False means that the file could have been deleted or that its
	 * name has been changed.
	 * 
	 * @return true if the file exists in currently checked out version of the git
	 *         repository.
	 */
	boolean exists();

	/**
	 * Returns true if the file is a Java class.
	 * 
	 * @return true if the file is a Java class.
	 */
	boolean isJavaClass();

	/**
	 * Returns true if the file is a Java class and exists in currently checked out
	 * version of the git repository. False means that the file could have been
	 * deleted or that its name has been changed.
	 * 
	 * @return true if the file is a Java class and exists in currently checked out
	 *         version of the git repository.
	 */
	boolean isExistingJavaClass();

	/**
	 * Returns the commits that the file was changed in as a set of
	 * {@link GitCommit} objects.
	 * 
	 * @return commits that the file was changed in as a set of {@link GitCommit}
	 *         objects.
	 */
	Set<GitCommit> getCommits();

	/**
	 * Adds a {@link GitCommit} objects to the set of commits that the file was
	 * changed in.
	 * 
	 * @param gitCommits
	 *            that the file was changed in as {@link GitCommit} objects.
	 */
	void addCommit(GitCommit gitCommit);
	
	/**
	 * Opens the changed file in an editor
	 */
	void goToChangedFile();
}
