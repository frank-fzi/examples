package de.fzi.sem.target.model;

import de.fzi.sem.interfaces.IModel;

public class TargetModel implements IModel {
	private String appName;

	public TargetModel(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

}
