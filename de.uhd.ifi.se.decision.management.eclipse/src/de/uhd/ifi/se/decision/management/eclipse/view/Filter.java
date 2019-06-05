package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.Insets;

import javax.swing.JCheckBox;

public class Filter {

	private String title;
	private boolean isActivated;
	private JCheckBox checkBox;
	private Class<?> nodeClass;

	public Filter(String title) {
		this.title = title;
		this.checkBox = new JCheckBox(title);
		this.isActivated = true;

		this.checkBox.setSelected(true);
		this.checkBox.setMargin(new Insets(5, 5, 5, 5));
	}

	public Filter(String title, Class<?> nodeClass) {
		this(title);
		this.nodeClass = nodeClass;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public Class<?> getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(Class<?> nodeClass) {
		this.nodeClass = nodeClass;
	}
}
