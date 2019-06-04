package de.fzi.sem.target.conroller;

import de.fzi.sem.interfaces.IController;
import de.fzi.sem.interfaces.IModel;
import de.fzi.sem.interfaces.IView;
import de.fzi.sem.target.model.TargetModel;
import de.fzi.sem.target.view.TargetView;

public class TargetController implements IController {
	private TargetModel model;
	private TargetView view;
	
	public TargetController(IModel model, IView view) {
		this.model = (TargetModel) model;
		this.view = (TargetView) view;
	}

	@Override
	public void initView() {
		view.build();	
	}

	@Override
	public TargetModel getModel() {
		return model;
	}

	@Override
	public TargetView getView() {
		return view;
	}

}
