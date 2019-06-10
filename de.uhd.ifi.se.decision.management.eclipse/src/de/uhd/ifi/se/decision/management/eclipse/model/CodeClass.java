package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.diff.DiffEntry;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;

/**
 * Interface for files as part of the knowledge graph.
 */
public interface CodeClass extends Node {

	/**
	 * Instances of CodeClass that are identified by the path to the file.
	 */
	public Map<IPath, CodeClass> instances = new HashMap<IPath, CodeClass>();

	public static CodeClass getOrCreate(DiffEntry diffEntry, IPath pathToGit) {
		IPath newPath = new Path(diffEntry.getNewPath());
		IPath path = pathToGit.removeLastSegments(1).append(newPath);
		return getOrCreate(path);
	}

	public static CodeClass getOrCreate(IPath path) {
		if (instances.containsKey(path)) {
			return instances.get(path);
		}
		CodeClass codeClass = new CodeClassImpl(path);
		instances.put(path, codeClass);
		return codeClass;
	}

	public static Set<CodeClass> getInstances() {
		return new HashSet<CodeClass>(instances.values());
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
