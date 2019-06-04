package de.fzi.sem.interfaces;


public interface IController {

	public abstract void initView();

	public abstract IModel getModel();

	public abstract IView getView();

}