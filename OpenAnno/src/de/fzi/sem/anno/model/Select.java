package de.fzi.sem.anno.model;

import java.awt.event.ActionListener;

public class Select {
	private String comboBoxLabel = "";
	private Object[] comboBoxData = {};
	private Object comboBoxDefault = "";
	private ActionListener comboListener = null;

	public Select(String comboBoxLabel, Object[] comboBoxData,
			Object comboBoxDefault, ActionListener comboListener) {
		super();
		this.comboBoxLabel = comboBoxLabel;
		this.comboBoxData = comboBoxData;
		this.comboBoxDefault = comboBoxDefault;
		this.comboListener = comboListener;
	}

	public String getComboBoxLabel() {
		return comboBoxLabel;
	}

	public Object[] getComboBoxData() {
		return comboBoxData;
	}

	public Object getComboBoxDefault() {
		return comboBoxDefault;
	}

	public ActionListener getComboListener() {
		return comboListener;
	}
	
	

}
