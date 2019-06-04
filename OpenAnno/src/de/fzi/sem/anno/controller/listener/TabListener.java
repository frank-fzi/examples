package de.fzi.sem.anno.controller.listener;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.fzi.sem.anno.controller.OpenAnnoController;

public class TabListener implements ChangeListener {
	private OpenAnnoController controller;	

	public TabListener(OpenAnnoController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		int index = sourceTabbedPane.getSelectedIndex();
        System.out.println("Tab of " + controller.getModel().getAppName() + " changed to: " + sourceTabbedPane.getTitleAt(index));	
	}
}
