package de.fzi.sem.anno.controller;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fzi.sem.anno.model.Iri;
import de.fzi.sem.anno.model.OpenAnnoModel;
import de.fzi.sem.anno.util.StringVisitor;
import de.fzi.sem.anno.view.OpenAnnoView;

public class Search implements Runnable {	
	private OpenAnnoModel model;
	private OpenAnnoView view;
	private OpenAnnoController controller;	
	private OpenAnnoSettings settings;

	public Search(OpenAnnoController controller) {
		super();
		this.model = controller.getModel();
		this.view = controller.getView();
		this.controller = controller;
		this.settings = controller.getSettings();
	}

	@Override
	public void run() {
		// reset results of last search
		controller.resetResults();
		
		// get all properties of target resource
		Iri targetIri = model.getAnno().getTarget();
		Model input = model.getInput(settings.getInputFileId());
		System.out.println("Input for " + targetIri.toString() + ":");
		input.write(System.out);		
		Resource targetResource = model.getInput(settings.getInputFileId()).getResource(targetIri.toString());
		StmtIterator properties = targetResource.listProperties();
		
		// check for accordances	
		Integer count = 0;
		List<Statement> propertyList = properties.toList();
		
		// remove ignored properties
		List<Statement> delete = new ArrayList<Statement>();
		for(Statement statement: propertyList) {
			if(statement.getObject().isURIResource()) {
				Iri iri = new Iri(statement.getObject().asResource().getURI());
				List<String> ignoredLocalNames = controller.getIgnoredLocalNames();
				if(ignoredLocalNames.contains(iri.getLocalName().toLowerCase()) ) {
					delete.add(statement);
					System.out.println(iri.getCurie() + " ignored in Search.run()");
				} 
			}
		}		
		if (propertyList.removeAll(delete)) {
			System.out.println("Ignored properties have been removed from target resource.");
		}
		
		Integer size = propertyList.size();
		System.out.println("Number of properties: " + size);
		controller.setTotalSize(size);
		for(Statement statement: propertyList) {
			count++;
			
			Property predicate = statement.getPredicate();
			Iri predicateIri = new Iri(predicate.getURI());	
			
			// list info for this property
			Object[] propInfo = new Object[4];
			propInfo[0] = count.toString() + " of " + size.toString();
			propInfo[1] = predicate.getLocalName();				
			propInfo[2] = statement.getObject().visitWith(new StringVisitor()).toString();				
			propInfo[3] = "...search accordances";	
			view.addSearchRow(propInfo);
			
			// search for this property
			RDFNode object = statement.getObject();
			Thread search = new Thread( new PropertySearch(predicateIri, object, (count - 1), controller));
			search.start();	
		}		
		controller.setSearched(true);
	} // run()
} // class
