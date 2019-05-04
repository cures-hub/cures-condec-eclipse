package de.uhd.ifi.se.decision.management.eclipse.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import de.uhd.ifi.se.decision.management.eclipse.extraction.MethodVisitor;

/**
 * An example class for testing
 */
public class GetMethodsInFileExample {

	public static void main(String[] args) throws IOException {

		FileInputStream fileInputStream = new FileInputStream(
				"C:\\Users\\anja\\Documents\\gits\\theses\\martinseiler\\implementation\\de.uhd.ifi.se.decdoc.eclipse\\src\\de\\uhd\\ifi\\se\\decision\\management\\eclipse\\changesupport\\DecisionCompletionProposalComputer.java");
		CompilationUnit compilationUnit;
		try {
			compilationUnit = JavaParser.parse(fileInputStream);
		} finally {
			fileInputStream.close();
		}

		MethodVisitor methodVistor = new MethodVisitor();

		compilationUnit.accept(methodVistor, null);
		Set<MethodDeclaration> set = methodVistor.getMethodDeclarations();
		Iterator<MethodDeclaration> iter = set.iterator();

		while (iter.hasNext()) {
			MethodDeclaration methodDeclaration = iter.next();
			System.out.println(methodDeclaration.getNameAsString());
			System.out.println(methodDeclaration.getBegin().get().line);
			System.out.println(methodDeclaration.getEnd().get().line);
			System.out.println(methodDeclaration.getAnnotations());
		}
	}

}
