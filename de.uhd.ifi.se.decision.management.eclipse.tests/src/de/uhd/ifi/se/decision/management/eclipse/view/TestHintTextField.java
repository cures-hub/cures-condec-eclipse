package de.uhd.ifi.se.decision.management.eclipse.view;

import static org.junit.Assert.assertNotNull;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.junit.Test;

public class TestHintTextField {
	
	@Test
	public void testHintTextFieldEmpty() {
		JFrame frame = new JFrame();
		String hint = "Hint";
		JTextField hintTextField = new HintTextField(hint);
		hintTextField.setText(null);
		frame.add(hintTextField);
		frame.setVisible(true);
		assertNotNull(hintTextField);
	}
	
	@Test
	public void testHintTextFieldText() {
		JFrame frame = new JFrame();
		String hint = "Hint";
		JTextField hintTextField = new HintTextField(hint);
		hintTextField.setText("Test");
		frame.add(hintTextField);
		frame.setVisible(true);
		assertNotNull(hintTextField);
	}

}
