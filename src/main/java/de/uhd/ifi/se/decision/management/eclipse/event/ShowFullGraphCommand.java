package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.uhd.ifi.se.decision.management.eclipse.extraction.GitClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.JiraClient;
import de.uhd.ifi.se.decision.management.eclipse.extraction.Linker;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.GitClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.extraction.impl.JiraClientImpl;
import de.uhd.ifi.se.decision.management.eclipse.view.MapDesigner;

public class ShowFullGraphCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {
				IResource resource = (IResource) ((IAdaptable) firstElement).getAdapter(IResource.class);
				resource = resource.getProject().getAdapter(IResource.class);

				JiraClient jm = JiraClientImpl.getOrCreate();
				if (jm.authenticate() != 0) {
					System.out.println("There was an error when authenticate JiraManager");
				}
				GitClient gm = GitClientImpl.getOrCreate();
				Linker l = new Linker(gm, jm);
				MapDesigner mapDesigner = MapDesigner.getOrCreate();
				mapDesigner.createFullMap(l);
			}
		}
		return null;
	}

}