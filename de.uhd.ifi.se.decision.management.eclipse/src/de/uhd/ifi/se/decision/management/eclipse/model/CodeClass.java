package de.uhd.ifi.se.decision.management.eclipse.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jgit.diff.DiffEntry;

import de.uhd.ifi.se.decision.management.eclipse.model.impl.CodeClassImpl;

public interface CodeClass extends Node {

	/**
	 * Instances of CodeClass that are identified by the path to the class.
	 */
	public Map<String, CodeClass> instances = new HashMap<String, CodeClass>();

	public static CodeClass getOrCreate(DiffEntry diffEntry, String pathToGit) {
		String path = diffEntry.getNewPath();
		if (instances.containsKey(path)) {
			return instances.get(path);
		}
		CodeClass codeClass = new CodeClassImpl(path, pathToGit);
		instances.put(path, codeClass);
		return codeClass;
	}

	public static Set<CodeClass> getInstances() {
		Set<CodeClass> output = new HashSet<CodeClass>();
		for (Map.Entry<String, CodeClass> entry : instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	String getClassName();

	String getPackage();

	String getProject();

	IPath getPath();

	String getFileLocation();
}
