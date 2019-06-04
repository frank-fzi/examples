package de.fzi.sem.anno;

import de.fzi.sem.anno.controller.OpenAnnoController;
import de.fzi.sem.anno.model.OpenAnnoModel;
import de.fzi.sem.anno.view.OpenAnnoView;
import de.fzi.sem.interfaces.IController;

public class OpenAnnoRun implements Runnable {
	
	public void run() {		
		
		// set up MVC
		OpenAnnoModel model = new OpenAnnoModel();
		OpenAnnoView view = new OpenAnnoView(model.getAppName());
		IController controller = new OpenAnnoController(model, view);
		
		// initialize view
		controller.initView();
	}
}
