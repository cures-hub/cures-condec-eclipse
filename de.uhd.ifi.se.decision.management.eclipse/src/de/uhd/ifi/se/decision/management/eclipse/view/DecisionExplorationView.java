
package de.uhd.ifi.se.decision.management.eclipse.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class DecisionExplorationView extends ViewPart {

	private Label label;
	private ScrolledComposite scrolledComposite;

	@Override
	public void createPartControl(Composite parent) {
		scrolledComposite = initScrolledComposite(parent);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new FillLayout());

		label = new Label(composite, SWT.NONE);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label.setText(
				"This view shows the explored decision knowledge related to the line where content assist is triggered:\n");

		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public static ScrolledComposite initScrolledComposite(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		return scrolledComposite;
	}

	public void setContent(String text) {
		label.setText(text);
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		label.setFocus();
	}
}
