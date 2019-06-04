package de.fzi.sem.target;

import de.fzi.sem.interfaces.IController;
import de.fzi.sem.interfaces.IModel;
import de.fzi.sem.interfaces.IView;
import de.fzi.sem.target.conroller.TargetController;
import de.fzi.sem.target.model.TargetModel;
import de.fzi.sem.target.view.TargetView;

public class TargetRun implements Runnable {
	private String parentAppName;

	public TargetRun(String parentAppName) {
		super();
		this.parentAppName = parentAppName;
	}

	@Override
	public void run() {
		// set up MVC
		IModel model = new TargetModel(parentAppName + ": Create Target");
		IView view = new TargetView(model.getAppName());
		IController controller = new TargetController(model, view);
		
		// initialize view
		System.out.println("Creating view for " + model.getAppName());
		controller.initView();
	}

}
