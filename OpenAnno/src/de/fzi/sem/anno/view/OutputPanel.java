package de.fzi.sem.anno.view;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OutputPanel extends Tab {	
	
	// Data
	private JPanel panel = null;
	private String comboBoxLabel = "";
	private String[] comboBoxData = {};
	private String comboBoxDefault = "";
	private String buttonValue = "";
	
	// Listener
	private ActionListener comboListener = null;
	private ActionListener createListener = null;	
	
	public OutputPanel(String title) {
		this.panel = new JPanel();
		this.setTitle(title);
	}
	
	public void build() {	
		// set up components
		JLabel labelMotivation = new JLabel(comboBoxLabel);
		JComboBox<?> select = new JComboBox<Object>(comboBoxData);
		select.setMaximumSize(select.getPreferredSize() );
		select.setSelectedItem(comboBoxDefault);
		select.addActionListener(comboListener);

		
		JButton createButton = new JButton(buttonValue);
		createButton.addActionListener(createListener);	
		
		// set up panel
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(labelMotivation);
		panel.add(select);
		panel.add(createButton);
	}

	// Getter / Setter
	
	@Override
	public JPanel getComponent() {
		return panel;
	}

	public String getComboBoxLabel() {
		return comboBoxLabel;
	}

	public void setComboBoxLabel(String comboBoxLabel) {
		this.comboBoxLabel = comboBoxLabel;
	}

	public String[] getComboBoxData() {
		return comboBoxData;
	}

	public void setComboBoxData(String[] comboBoxData) {
		this.comboBoxData = comboBoxData;
	}

	public String getComboBoxDefault() {
		return comboBoxDefault;
	}

	public void setComboBoxDefault(String comboBoxDefault) {
		this.comboBoxDefault = comboBoxDefault;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public ActionListener getComboListener() {
		return comboListener;
	}

	public void setComboListener(ActionListener comboListener) {
		this.comboListener = comboListener;
	}

	public ActionListener getCreateListener() {
		return createListener;
	}

	public void setCreateListener(ActionListener createListener) {
		this.createListener = createListener;
	}
}
