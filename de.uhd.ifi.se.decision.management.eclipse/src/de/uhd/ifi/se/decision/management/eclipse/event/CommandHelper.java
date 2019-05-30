package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("restriction")
public class CommandHelper {

	public static IPath getPathOfSelectedFile(ExecutionEvent event) {
		CompilationUnit compilationUnit = getCompilationUnit(event);
		IResource resource = (IResource) ((IAdaptable) compilationUnit).getAdapter(IResource.class);
		return resource.getLocation();
	}

	public static CompilationUnit getCompilationUnit(ExecutionEvent event) {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof CompilationUnit) {
			return (CompilationUnit) firstElement;
		}
		return null;
	}

	public static boolean isValidSelection(ExecutionEvent event) {
		CompilationUnit compilationUnit = getCompilationUnit(event);
		if (compilationUnit != null) {
			return true;
		}
		return false;
	}
}
