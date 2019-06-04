package de.fzi.sem.anno;

import org.apache.log4j.PropertyConfigurator;

import de.fzi.sem.anno.model.Iri;
import de.fzi.sem.anno.model.RDF;
import de.fzi.sem.anno.model.singleton.FilePath;
import de.fzi.sem.anno.model.singleton.NameSpaces;
import de.fzi.sem.anno.model.singleton.Services;
import de.fzi.sem.anno.util.LiteralQuery;

public class SandboxRun implements Runnable {
	
	private NameSpaces ns = null;
	private FilePath fp = null;
	private Services services = null;

	public void run() {
		PropertyConfigurator.configure("jena-log4j.properties");
		
		// get singletons
		ns = NameSpaces.getInstance();
		fp = FilePath.getInstance();
		services = Services.getInstance();
				
		// set up sources
		LiteralQuery dbpQuery = new LiteralQuery(services.get("dbp"));
		LiteralQuery lodQuery = new LiteralQuery(services.get("lod"));
		LiteralQuery euQuery = new LiteralQuery(services.get("eu"));
		LiteralQuery euQueryLod = new LiteralQuery(services.get("lod"));
		LiteralQuery crmLabelQuery = null;
		LiteralQuery lupaLabelQuery = null;
		LiteralQuery oadmLabelQuery = null;
		LiteralQuery crmCommentQuery = null;
		try {
			crmLabelQuery = new LiteralQuery(new RDF(fp.get("oa")).getModel());
			lupaLabelQuery = new LiteralQuery(new RDF(fp.get("lupa")).getModel());
			oadmLabelQuery = new LiteralQuery(new RDF(fp.get("oa")).getModel());
			crmCommentQuery = new LiteralQuery(new RDF(fp.get("crm")).getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// build queries
		Iri property = new Iri(ns, "rdfs", "label");
		Iri cWolf = new Iri(ns, "dbpr", "Capitoline_Wolf");
		Iri museum = new Iri(ns, "dbpo", "museum");
		String criteria = ". <" + cWolf.toString() + "> <" + museum.toString() + "> ?class";
		String language = "DE";
		
		dbpQuery.buildSelectQuery(property, criteria, language);
		
		lodQuery.buildSelectQuery(property, criteria, language);
		
		criteria = "";
		crmLabelQuery.buildSelectQuery(property, criteria, language);
		lupaLabelQuery.buildSelectQuery(property, criteria, language);
		oadmLabelQuery.buildSelectQuery(property, criteria);
		
		property = new Iri(ns, "rdfs", "comment");
		crmCommentQuery.buildSelectQuery(property, criteria, language);
		
		property = new Iri(ns, "dc", "title");
		criteria = ". ?class <" + property.toString() + "> 'LUPA CAPITOLINA'";
		euQuery.buildSelectQuery(property, criteria);
		euQueryLod.buildSelectQuery(property, criteria);
		
		// Load data for CRM schema
		System.out.println("LOAD DATA FOR CRM SCHEMA");			
		try {
			crmLabelQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(crmLabelQuery.literalResponse());
		crmLabelQuery = null;
		
		// comments
		System.out.println("\n\nShow comments for " + language);
		try {
			crmCommentQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(crmCommentQuery.literalResponse());
		crmCommentQuery = null;
				
		// Load data for OADM
		System.out.println("\n\nLOAD DATA FOR OADM");	
		try {
			oadmLabelQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(oadmLabelQuery.literalResponse());
		oadmLabelQuery = null;
		
		// Load data for Lupa Capitolina
		System.out.println("\n\nLOAD DATA FOR LUPA CAPITOLINA");
		try {
			lupaLabelQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(lupaLabelQuery.literalResponse());
		lupaLabelQuery = null;
		
		// Show labels of museum holding Lupa Capitolina
		System.out.println("\n\nDATA FROM DBPEDIA");
		try {
			dbpQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(dbpQuery.literalResponse());
		dbpQuery = null;
		
		// same query with LOD (LOD = Linking Open Data)
		System.out.println("\n\nSAME QUERY WITH LOD");
		try {
			lodQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(lodQuery.literalResponse());
		lodQuery = null;
		
		// Europeana request
		System.out.println("\n\nDATA FROM EUROPEANA");
		try {
			euQuery.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(euQuery.literalResponse());	
		euQuery = null;
		
		// same query with LOD (LOD = Linking Open Data)
		System.out.println("\n\nSAME QUERY WITH LOD");
		try {
			euQueryLod.runQuery();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(euQueryLod.literalResponse());
		euQueryLod = null;
		
		// tidy up memory
		System.gc();
	}
}
