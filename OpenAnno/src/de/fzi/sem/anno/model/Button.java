package de.fzi.sem.anno.model;

import java.awt.event.ActionListener;

public class Button {
	private String value;
	private ActionListener listener;
	
	public Button(String value, ActionListener listener) {
		super();
		this.value = value;
		this.listener = listener;
	}

	public String getValue() {
		return value;
	}

	public ActionListener getListener() {
		return listener;
	}
}
