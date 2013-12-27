package org.egbers.homeautomation.kitchen.upc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class UPCInputTextField extends JTextField implements ActionListener {
	private static final long serialVersionUID = -4325629353851827939L;

	private String upcCode = "";

	public UPCInputTextField() {
		super();
		addActionListener(this);
	}

	public void actionPerformed(final ActionEvent event) {
		upcCode = this.getText();
		this.setText("");
	}

	public String getUPCCode() {
		return upcCode;
	}
}
