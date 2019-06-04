package de.fzi.sem.anno.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.fzi.sem.anno.controller.OpenAnnoController;

public class ShowResultsListener implements ActionListener {
	private OpenAnnoController controller = null;
	
		public ShowResultsListener(OpenAnnoController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.getView().showResults();
	}
}
