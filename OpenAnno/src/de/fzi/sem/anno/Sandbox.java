package de.fzi.sem.anno;

import org.apache.log4j.PropertyConfigurator;

public class Sandbox {
	public static void main(String[] args) {
		PropertyConfigurator.configure("jena-log4j.properties");	
		new Thread(new SandboxRun()).start();
	}
}
