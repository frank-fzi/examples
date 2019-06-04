package de.fzi.sem.anno.view;

import java.awt.Component;

public class Tab implements Itab {
	
	private String title = null;
	private Component component = null;
	
	public Tab() {
		this("", null);
	}
	
	public Tab(String title, Component component) {
		this.title = title;
		this.component = component;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
}
