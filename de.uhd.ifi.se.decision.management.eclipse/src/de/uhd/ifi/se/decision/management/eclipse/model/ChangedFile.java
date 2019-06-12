package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	 * Returns a list of methods if the file is a Java class.
	 * 
	 * @return list of methods. Returns an empty list if the file is not existing
	 *         (anymore) or if it is not a Java class.
	 */
	List<CodeMethod> getCodeMethods();
}
