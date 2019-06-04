package de.fzi.sem.anno.model;

import java.io.IOException;
import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class RDF {
	private Model model = null;

	public RDF(String rdf_file) throws IOException {
		super();
		this.model = ModelFactory.createOntologyModel();
		InputStream inModelInstance = FileManager.get().open(rdf_file);
		String baseUri = "http://example.com";
		String fileExtension =  rdf_file.substring( rdf_file.lastIndexOf('.') + 1 );
		if(fileExtension.equals("ttl")) {
			this.model.read(inModelInstance, baseUri, "TTL");
		} else if (fileExtension.equals("rdf")) {
			this.model.read(inModelInstance, baseUri, "RDF/XML"); 
		} else if (fileExtension.equals("owl")) {
			this.model.read(inModelInstance, baseUri, "RDF/XML"); 
		} else {
			System.out.println("Unknown file exension \"" + fileExtension + "\" for " + rdf_file + ". Defualt ontology model returned.");
		}
		inModelInstance.close();
	}
	
	public Model getModel() {
		return this.model;
	}
}
