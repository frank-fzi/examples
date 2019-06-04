package de.fzi.sem.anno.controller.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class InfoListener implements ActionListener {
	private Object message = null;
	private Component parentComponent;

	public InfoListener(Component parentComponent, Object message) {
		this.message = message;
	    this.parentComponent = parentComponent;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(parentComponent, message.toString(), "Info", JOptionPane.INFORMATION_MESSAGE);
	}

}
