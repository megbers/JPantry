package org.egbers.homeautomation.kitchen.upc.ui;

import javax.swing.JFrame;

public class UPCLookUpGUI extends JFrame {
	private static final long serialVersionUID = -701862696283540276L;
	private final UPCInputTextField upcInput;
	private final ListTextArea listTextArea;

	public UPCLookUpGUI() {
		super("UPC Look Up");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		upcInput = new UPCInputTextField();
		listTextArea = new ListTextArea();
		this.add(upcInput);
		// this.add(listTextArea);
		this.pack();
		this.setVisible(true);
	}

	public void addProductToList(final String item) {
		listTextArea.append(item);
	}

	public String getUPCCode() {
		return upcInput.getUPCCode();
	}
}
