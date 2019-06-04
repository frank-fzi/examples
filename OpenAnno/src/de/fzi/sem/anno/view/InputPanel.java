package de.fzi.sem.anno.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.fzi.sem.anno.model.Button;
import de.fzi.sem.anno.model.Select;

public class InputPanel extends JPanel {	
	private static final long serialVersionUID = -734199491446077732L;
	
	// Data
	private List<Select> comboBoxes = null;
	private Button button;
	private String comboBoxLabel = "";
	private String[] comboBoxData = {};
	private String comboBoxDefault = "";
	private String buttonValue = "";	
	
	public InputPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.comboBoxes = new ArrayList<Select>();
	}
	
	public void build() {	
		// set up components
		Dimension maxSize = new Dimension();
		maxSize.setSize(0, 0);
		
		for(Select select: comboBoxes) {
			JComboBox<?> comboBox = new JComboBox<Object>(select.getComboBoxData());
			if (comboBox.getPreferredSize().getWidth() > maxSize.getWidth()) maxSize = comboBox.getPreferredSize();
		}
		
		for(Select select: comboBoxes) {
			JLabel label = new JLabel(select.getComboBoxLabel());
			JComboBox<?> comboBox = new JComboBox<Object>(select.getComboBoxData());
			comboBox.setMaximumSize(maxSize);
			comboBox.setSelectedItem(select.getComboBoxDefault());
			comboBox.addActionListener(select.getComboListener());
			this.add(label);
			this.add(comboBox);
		}	
		
		JButton searchButton = new JButton(button.getValue());
		searchButton.addActionListener(button.getListener());	
		this.add(searchButton);
	}

	// Getter / Setter
	
	public JPanel getComponent() {
		return this;
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
	
	public void addSelect(Select select) {
		this.comboBoxes.add(select);
	}

	public void setButton(Button button) {
		this.button = button;
	}
}
