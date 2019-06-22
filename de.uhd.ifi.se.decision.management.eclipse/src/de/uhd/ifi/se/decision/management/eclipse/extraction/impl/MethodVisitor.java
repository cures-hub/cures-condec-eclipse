package de.uhd.ifi.se.decision.management.eclipse.extraction;

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * the MethodVisitor class creates ParsedMethodItems out of parsed content
 */
public class MethodVisitor extends VoidVisitorAdapter<Void> {

	private Set<MethodDeclaration> methodDeclarations = new LinkedHashSet<MethodDeclaration>();

	public MethodVisitor() {
		this.methodDeclarations = new LinkedHashSet<MethodDeclaration>();
	}

	public Set<MethodDeclaration> getMethodDeclarations() {
		return methodDeclarations;
	}

	public void setMethodDeclarations(Set<MethodDeclaration> methodDeclarations) {
		this.methodDeclarations = methodDeclarations;
	}

	/**
	 * Identifies method names, their beginning and end, and their annotations
	 * 
	 * @param methodDeclaration
	 *            a method declaration
	 * @param arg
	 *            void
	 */
	@Override
	public void visit(MethodDeclaration methodDeclaration, Void arg) {
		methodDeclarations.add(methodDeclaration);
		super.visit(methodDeclaration, arg);
	}
}
