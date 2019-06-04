package de.fzi.sem.anno.controller.listener;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenListener implements ActionListener {
	private String filePath = null;

	public OpenListener(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		try {
			File file = new File(filePath);
			Desktop desk = Desktop.getDesktop();
			desk.open(file);
//			Runtime.getRuntime().exec("C:\\Program Files (x86)\\Notepad++\\notepad++.exe " + filePath);
		} catch( Exception e) {
			System.out.println("Can't open file " + filePath + ": " + e.getMessage());
		} 
	}
}
