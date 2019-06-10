package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.diff.DiffEntry;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;

public interface CodeClass extends Node {

	/**
	 * Instances of CodeClass that are identified by the path to the class.
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
		Set<CodeClass> output = new HashSet<CodeClass>();
		for (Map.Entry<IPath, CodeClass> entry : instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	IPath getPath();

	String getClassName();
}
