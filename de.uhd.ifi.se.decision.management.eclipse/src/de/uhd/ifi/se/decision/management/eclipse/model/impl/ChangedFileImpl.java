package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.io.FileInputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IPath;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;
import de.uhd.ifi.se.decision.management.eclipse.model.ChangedFile;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;
import de.uhd.ifi.se.decision.management.eclipse.model.GitCommit;

public class ChangedFileImpl extends NodeImpl implements ChangedFile {

	private IPath path;
	private Set<CodeMethod> methodsInClass;
	private Set<GitCommit> commits;

	public ChangedFileImpl(IPath path) {
		this.path = path;
		this.methodsInClass = parseMethods();
		this.commits = new LinkedHashSet<GitCommit>();
	}

	@Override
	public boolean isExistingJavaClass() {
		return exists() && isJavaClass();
	}

	@Override
	public boolean exists() {
		return this.path.toFile().exists();
	}

	@Override
	public boolean isJavaClass() {
		String fileExtension = this.path.getFileExtension();
		return fileExtension != null && fileExtension.equalsIgnoreCase("java");
	}

	private Set<CodeMethod> parseMethods() {
		Set<CodeMethod> methodsInClass = new LinkedHashSet<CodeMethod>();

		if (!isExistingJavaClass()) {
			return methodsInClass;
		}

		MethodVisitor methodVistor = getMethodVisitor();
		for (MethodDeclaration methodDeclaration : methodVistor.getMethodDeclarations()) {
			CodeMethod codeMethod = new CodeMethodImpl(methodDeclaration.getNameAsString(), this);
			methodsInClass.add(codeMethod);
			this.addLinkedNode(codeMethod);
			codeMethod.addLinkedNode(this);
		}

		return methodsInClass;
	}

	private MethodVisitor getMethodVisitor() {
		ParseResult<CompilationUnit> parseResult = getParseResult();
		CompilationUnit compilationUnit = parseResult.getResult().get();
		MethodVisitor methodVistor = new MethodVisitor();
		compilationUnit.accept(methodVistor, null);
		return methodVistor;
	}

	private ParseResult<CompilationUnit> getParseResult() {
		ParseResult<CompilationUnit> parseResult = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(this.path.toString());
			JavaParser javaParser = new JavaParser();
			parseResult = javaParser.parse(fileInputStream);
			fileInputStream.close();
		} catch (Exception e) {
			System.err.println("Methods of class " + this.getFileName() + " could not be parsed. Message: " + e);
		}
		return parseResult;
	}

	@Override
	public IPath getPath() {
		return this.path;
	}

	@Override
	public Set<CodeMethod> getCodeMethods() {
		return this.methodsInClass;
	}

	@Override
	public String toString() {
		return this.getFileName();
	}

	@Override
	public String getFileName() {
		return path.lastSegment();
	}

	@Override
	public Set<GitCommit> getCommits() {
		return commits;
	}

	@Override
	public void addCommit(GitCommit gitCommit) {
		this.commits.add(gitCommit);
	}
}
