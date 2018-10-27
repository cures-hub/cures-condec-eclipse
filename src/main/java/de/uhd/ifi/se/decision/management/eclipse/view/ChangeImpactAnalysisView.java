package de.uhd.ifi.se.decision.management.eclipse.view;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uhd.ifi.se.decision.management.eclipse.changesupport.ViewSupport;

public class ChangeImpactAnalysisView extends ViewPart {

	private Label label;
	private ScrolledComposite scrolledComposite;

	@Override
	@PostConstruct
	public void createPartControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new FillLayout());
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		label = new Label(composite, SWT.NONE);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label.setText("This view shows the results of the change impact analysis:\n");

		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public void analyseChangeImpact() throws MissingObjectException, IncorrectObjectTypeException, IOException {
		label.setText(ViewSupport.analyzeChange());
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		label.setFocus();
	}
}