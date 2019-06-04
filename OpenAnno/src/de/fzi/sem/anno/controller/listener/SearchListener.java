package de.fzi.sem.anno.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.fzi.sem.anno.controller.OpenAnnoController;
import de.fzi.sem.anno.controller.Search;

public class SearchListener implements ActionListener {
	
	private OpenAnnoController controller;

	public SearchListener(OpenAnnoController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		System.out.println("searching...");
    	Thread search = new Thread( new Search(controller));
    	search.start();
	}		
} // class SearchListener implements ActionListener
