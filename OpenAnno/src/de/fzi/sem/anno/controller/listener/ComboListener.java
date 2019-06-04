package de.fzi.sem.anno.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import de.fzi.sem.anno.controller.OpenAnnoController;
import de.fzi.sem.anno.model.Agent;
import de.fzi.sem.anno.model.Iri;
import de.fzi.sem.anno.model.RdfSource;

public class ComboListener implements ActionListener {
	
	private OpenAnnoController controller = null;
	private String property = "";

	public ComboListener(OpenAnnoController controller, String property) {
		this.controller = controller;
		this.property = property;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		JComboBox<?> select = (JComboBox<?>)ae.getSource();
		
		switch(property) {
			case "motivation":
				try {
					controller.getModel().getAnno().setMotivation(select.getSelectedItem().toString());
					System.out.println("Motivation \"" + controller.getModel().getAnno().getMotivation().getType() + "\" successfully added to " + controller.getModel().getAnno().getLocalName());
				} catch (Exception e) {
					System.out.println("Could not add motivation to data model. Reason: " + e.getMessage());
				}	
				break;
			
			case "agent":
				try {
					controller.getModel().getAnno().setAnnotatedBy((Agent) select.getSelectedItem());
					System.out.println("Annotated by \"" + controller.getModel().getAnno().getAnnotatedBy().getName() + "\" successfully added to " + controller.getModel().getAnno().getLocalName());
				} catch (Exception e) {
					System.out.println("Could not add agent to data model. Reason: " + e.getMessage());
				}		
				break;
				
			case "target":
				try {
					controller.getModel().getAnno().setTarget((Iri) select.getSelectedItem());
					controller.setSearched(false);
					System.out.println("Target \"" + controller.getModel().getAnno().getTarget().toString() + "\" successfully added to " + controller.getModel().getAnno().getLocalName());
				} catch (Exception e) {
					System.out.println("Could not add target to data model. Reason: " + e.getMessage());
				}		
				break;
				
			case "service":
				try {
					controller.getModel().setService((RdfSource) select.getSelectedItem());
					System.out.println("Service \"" + controller.getModel().getService() + "\" successfully added to " + controller.getModel().getAnno().getLocalName());
				} catch (Exception e) {
					System.out.println("Could not add service to data model. Reason: " + e.getMessage());
				}		
				break;
				
			case "format":
				System.out.println("Format set to " + select.getSelectedItem());
				break;
				
			default:
				System.out.println("Could not add property to data model. Reason: Property not defined");
				break;
		}	
	}
}
