package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;

public class ShowGraphInJira extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPath pathOfSelectedFile = CommandHelper.getPathOfSelectedFile(event);
		if (pathOfSelectedFile == null || !pathOfSelectedFile.toFile().exists()) {
			return null;
		}
		if (pathOfSelectedFile != null) {
			OpenWebbrowser.openWebpage(pathOfSelectedFile);
		}

		return null;
	}

}
