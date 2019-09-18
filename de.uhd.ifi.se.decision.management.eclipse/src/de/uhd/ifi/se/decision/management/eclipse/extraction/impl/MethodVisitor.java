package de.uhd.ifi.se.decision.management.eclipse.extraction.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Class responsible for retrieving method declarations from a Java class. Used to
 * parse changed files in commits.
 * 
 * @see ChangedFile
 */
public class MethodVisitor extends VoidVisitorAdapter<Void> {

	private Set<MethodDeclaration> methodDeclarations = new LinkedHashSet<MethodDeclaration>();

	public MethodVisitor() {
		this.methodDeclarations = new LinkedHashSet<MethodDeclaration>();
	}

	public Set<MethodDeclaration> getMethodDeclarations() {
		return methodDeclarations;
	}

	/**
	 * Identifies the method names. Could also be used to identify their beginning
	 * and end, and their annotations.
	 * 
	 * @param methodDeclaration
	 *            a method declaration.
	 * @param arg
	 *            passed to visit method in super class.
	 */
	@Override
	public void visit(MethodDeclaration methodDeclaration, Void arg) {
		methodDeclarations.add(methodDeclaration);
		super.visit(methodDeclaration, arg);
	}
}
