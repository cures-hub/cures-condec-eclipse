package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class ShowGraphInJira extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = 
		        workbench == null ? null : workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = 
		        window == null ? null : window.getActivePage();

		IEditorPart editor = 
		        activePage == null ? null : activePage.getActiveEditor();
		IEditorInput input = 
		        editor == null ? null : editor.getEditorInput();
		IPath path = input instanceof FileEditorInput 
		        ? ((FileEditorInput)input).getPath()
		        : null;
		if (path != null) {
			OpenWebbrowser.openWebpage(path);
		}

		return null;
	}

}
