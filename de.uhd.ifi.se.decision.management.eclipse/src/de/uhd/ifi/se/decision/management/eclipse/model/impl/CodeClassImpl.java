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

		try {
			FileInputStream fileInputStream = new FileInputStream(this.path.toString());
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
			for (MethodDeclaration methodDeclaration : methodVistor.getMethodDeclarations()) {
				CodeMethod codeMethod = new CodeMethodImpl(methodDeclaration.getNameAsString());
				codeMethod.setMethodStartInCodefile(methodDeclaration.getBegin().get().line);
				codeMethod.setMethodStopInCodefile(methodDeclaration.getEnd().get().line);
				methodsInClass.add(codeMethod);
				this.addLinkedNode(codeMethod);
				codeMethod.addLinkedNode(this);
			}
		} catch (Exception e) {
			System.err.println("Methods of class " + this.getClassName() + " could not be parsed. Message: " + e);
		}
		return methodsInClass;
	}

	private boolean isExistingJavaClass() {
		// Is Java file?
		if (!this.path.getFileExtension().equalsIgnoreCase("java")) {
			return false;
		}
		// Is existing in currently checked out version?
		if (!this.path.toFile().exists()) {
			return false;
		}
		return true;
	}

	@Override
	public IPath getPath() {
		return this.path;
	}

	public List<CodeMethod> getCodeMethods() {
		return this.methodsInClass;
	}

	@Override
	public String toString() {
		return this.path.toString();
	}

	@Override
	public String getClassName() {
		return path.lastSegment();
	}

}
