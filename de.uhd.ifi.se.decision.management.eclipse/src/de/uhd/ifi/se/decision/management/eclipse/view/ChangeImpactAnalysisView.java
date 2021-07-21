package de.uhd.ifi.se.decision.management.eclipse.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

/**
 * @issue How to present related knowledge and change impact to developers?
 * @decision Present related knowledge and change impact in dedicated views.
 * @alternative Present related knowledge and change impact as a list of
 *              proposals.
 * @con Would mislead developers. Developers associate content assist with
 *      auto-completion and proposals for bug-fixes.
 * 
 * @issue How to trigger decision exploration and change impact analysis?
 * @decision Use menu items in context menu to trigger decision exploration and
 *           change impact analysis!
 * @alternative Content assist invocation triggers decision exploration view and
 *              change impact analysis view
 * @con Would mislead developers. Developers associate content assist with
 *      auto-completion and proposals for bug-fixes.
 */
public class ChangeImpactAnalysisView extends ViewPart {

	private Label label;
	private ScrolledComposite scrolledComposite;

	@Override
	public void createPartControl(Composite parent) {
		scrolledComposite = DecisionExplorationView.initScrolledComposite(parent);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new FillLayout());

		label = new Label(composite, SWT.NONE);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label.setText("This view shows the results of the change impact analysis:\n");

		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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