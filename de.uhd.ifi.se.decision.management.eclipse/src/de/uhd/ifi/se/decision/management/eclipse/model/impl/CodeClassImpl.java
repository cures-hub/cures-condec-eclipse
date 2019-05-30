package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.diff.DiffEntry;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

public class CodeClassImpl extends NodeImpl implements CodeClass {
	private static Map<String, CodeClass> instances = new HashMap<String, CodeClass>();
	private String className;
	private String packageName;
	private String project;
	private String fullClassPath;
	private boolean hasLoadedFeatures;
	private String pathToGit = "";
	private String fileLocation = "";
	private List<CodeMethod> methodsInClass;

	public static Set<CodeClass> getInstances() {
		Set<CodeClass> output = new HashSet<CodeClass>();
		for (Map.Entry<String, CodeClass> entry : instances.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	public static CodeClass getOrCreate(DiffEntry diffEntry, String pathToGit) {
		String fullClassPath = diffEntry.getNewPath();
		if (instances.containsKey(fullClassPath)) {
			return instances.get(fullClassPath);
		} else {
			CodeClass cc = new CodeClassImpl(fullClassPath, pathToGit);
			instances.put(fullClassPath, cc);
			return cc;
		}
	}

	private CodeClassImpl(String fullClassPath, String pathToGit) {
		this.methodsInClass = new ArrayList<CodeMethod>();
		this.fullClassPath = fullClassPath;
		this.pathToGit = pathToGit;
		String repoPath = pathToGit.toString();
		char directorySeperator = '\\';
		if (repoPath.contains("/")) {
			directorySeperator = '/';
		}
		repoPath = repoPath.substring(0, repoPath.lastIndexOf(directorySeperator));
		String modifiedFullClassPath = fullClassPath.replace('\\', directorySeperator).replace('/', directorySeperator);
		if (modifiedFullClassPath.startsWith(String.valueOf(directorySeperator))) {
			modifiedFullClassPath = modifiedFullClassPath.substring(1);
		}
		repoPath += directorySeperator + modifiedFullClassPath;
		this.fileLocation = repoPath;
		String[] splits = fullClassPath.split("/");
		// EXAMPLE:
		// de.uhd.ifi.se.flavored/src/de/uhd/ifi/se/flavored/examples/GitDiffExample.java
		// <----- PROJECT ------>/<---------- PACKAGE-NAME --------->/<-- CLASS-NAME
		// --->
		if (splits.length >= 3) {
			this.project = splits[0];
			this.className = splits[splits.length - 1];
			this.packageName = "";
			for (int i = 1; i < splits.length - 1; i++) {
				this.packageName += splits[i] + "/";
			}
		}
		this.hasLoadedFeatures = false;
		if (this.fullClassPath.endsWith(".java")) {
			if (repoPath != null && !repoPath.isEmpty() && pathToGit.endsWith(directorySeperator + ".git")) {
				try {
					FileInputStream fileInputStream = new FileInputStream(this.fileLocation);
					JavaParser javaParser = new JavaParser();
					ParseResult<CompilationUnit> parseResult;
					try {
						parseResult = javaParser.parse(fileInputStream);
					} finally {
						fileInputStream.close();
					}
					CompilationUnit compilationUnit = parseResult.getResult().get();
					MethodVisitor methodVistor = new MethodVisitor();
					compilationUnit.accept(methodVistor, null);
					for (MethodDeclaration md : methodVistor.getMethodDeclarations()) {
						CodeMethodImpl cm = new CodeMethodImpl(md.getNameAsString());
						cm.setMethodStartInCodefile(md.getBegin().get().line);
						cm.setMethodStopInCodefile(md.getEnd().get().line);
						this.methodsInClass.add(cm);
						this.addLinkedNode(cm);
						cm.addLinkedNode(this);
						for (AnnotationExpr ae : md.getAnnotations()) {
							String annotation = ae.getTokenRange().get().toString();
							if (annotation.startsWith("@Feature")) {
								String feature = annotation.replaceFirst("@Feature", "").replaceAll("\"", "");
								feature = feature.substring(feature.indexOf("(") + 1);
								feature = feature.substring(0, feature.lastIndexOf(")"));
							}
						}
					}
				} catch (Exception ex) {
				}
			}
			this.hasLoadedFeatures = true;
		}
	}

	public List<CodeMethod> getCodeMethods() {
		return this.methodsInClass;
	}

	@Override
	public String getFileLocation() {
		return this.fileLocation;
	}

	public void setPathToGit(String pathToGit) {
		this.pathToGit = pathToGit;
	}

	public String getPathToGit() {
		return this.pathToGit;
	}

	public boolean hasLoadedFeatures() {
		return this.hasLoadedFeatures;
	}

	@Override
	public String toString() {
		return this.fullClassPath;
	}

	@Override
	public String getClassName() {
		return this.className;
	}

	@Override
	public String getPackage() {
		return this.packageName;
	}

	@Override
	public String getProject() {
		return this.project;
	}

	@Override
	public String getFullClassPath() {
		return this.fullClassPath;
	}
}
