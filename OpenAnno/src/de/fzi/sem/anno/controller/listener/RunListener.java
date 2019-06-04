package de.fzi.sem.anno.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.fzi.sem.target.TargetRun;

public class RunListener implements ActionListener {
	private String threadName;
	private String parentAppName;	

	public RunListener(String threadName, String parentAppName) {
		super();
		this.threadName = threadName;
		this.parentAppName = parentAppName;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (threadName) {
		case "target":
			new Thread(new TargetRun(parentAppName)).start();
			break;
			
		default:
			System.out.println("Could not start thread. Reason: Thread not defined");
			break;
		}	
	}
}
