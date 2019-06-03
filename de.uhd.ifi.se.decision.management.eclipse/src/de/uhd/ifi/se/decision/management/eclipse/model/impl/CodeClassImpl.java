package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

public class CodeClassImpl extends NodeImpl implements CodeClass {

	private String className;
	private String packageName;
	private String project;
	private IPath path;
	private String pathToGit = "";
	private String fileLocation = "";
	private List<CodeMethod> methodsInClass;

	public CodeClassImpl(String fullClassPath, String pathToGit) {
		this.methodsInClass = new ArrayList<CodeMethod>();
		this.path = new Path(fullClassPath);
		this.pathToGit = pathToGit;
		char directorySeperator = '\\';
		if (pathToGit.contains("/")) {
			directorySeperator = '/';
		}
		String repoPath = pathToGit.substring(0, pathToGit.lastIndexOf(directorySeperator));
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
		if (this.path.getFileExtension().equalsIgnoreCase("java")) {
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
					}
				} catch (Exception ex) {
				}
			}
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

	@Override
	public String toString() {
		return this.path.toString();
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
	public IPath getPath() {
		return this.path;
	}
}
