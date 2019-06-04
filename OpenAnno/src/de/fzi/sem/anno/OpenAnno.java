package de.fzi.sem.anno;

import org.apache.log4j.PropertyConfigurator;

public class OpenAnno {
	public static void main(String[] args) {
		PropertyConfigurator.configure("jena-log4j.properties");
		new Thread(new OpenAnnoRun()).start();		
//		new Thread(new SandboxRun()).start();
	}
}
