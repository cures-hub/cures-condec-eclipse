package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("restriction")
public class CommandHelper {

	public static IPath getPathOfSelectedFile(ExecutionEvent event) {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof CompilationUnit) {
			CompilationUnit compilationUnit = (CompilationUnit)firstElement;
			IResource resource = (IResource) ((IAdaptable) compilationUnit).getAdapter(IResource.class);
			return resource.getLocation();
		}
		if (firstElement instanceof File) {
			File file = (File) firstElement;
			return file.getLocation();
		}
		return null;
	}

	public static boolean isValidSelection(ExecutionEvent event) {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object firstElement = selection.getFirstElement();
		return firstElement instanceof CompilationUnit || firstElement instanceof File;
	}
}
