package de.uhd.ifi.se.decision.management.eclipse.model.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeClass;
import de.uhd.ifi.se.decision.management.eclipse.model.CodeMethod;

public class CodeClassImpl extends NodeImpl implements CodeClass {

	private IPath path;
	private List<CodeMethod> methodsInClass;

	public CodeClassImpl(IPath path) {
		this.path = path;
		this.methodsInClass = parseMethods();
	}

	private List<CodeMethod> parseMethods() {
		List<CodeMethod> methodsInClass = new ArrayList<CodeMethod>();

		if (!isExistingJavaClass()) {
			return methodsInClass;
		}

		MethodVisitor methodVistor = getMethodVisitor();
		for (MethodDeclaration methodDeclaration : methodVistor.getMethodDeclarations()) {
			CodeMethod codeMethod = new CodeMethodImpl(methodDeclaration.getNameAsString());
			codeMethod.setMethodStartInCodefile(methodDeclaration.getBegin().get().line);
			codeMethod.setMethodStopInCodefile(methodDeclaration.getEnd().get().line);
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

	private boolean isExistingJavaClass() {
		// Is Java file and existing in currently checked out version?
		return isJavaClass() && this.path.toFile().exists();
	}

	private boolean isJavaClass() {
		return this.path.getFileExtension().equalsIgnoreCase("java");
	}

	@Override
	public IPath getPath() {
		return this.path;
	}

	@Override
	public List<CodeMethod> getCodeMethods() {
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
}
