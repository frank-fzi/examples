package de.fzi.sem.anno.model.singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NameSpaces {	
	
	private static NameSpaces instance;
	private List<String> keys;
	private Map<String, String> nameSpace;
	private Map<String, String> autoGen;
	
	private NameSpaces() {	
		nameSpace = new HashMap<String, String>();
		autoGen = new HashMap<String, String>();
		
		// Namespaces from http://www.openannotation.org/spec/core/#Namespaces
		nameSpace.put("oa", "http://www.w3.org/ns/oa#"); // The Open Annotation ontology
		nameSpace.put("cnt", "http://www.w3.org/2011/content#"); // Representing Content in RDF
		nameSpace.put("dc", "http://purl.org/dc/elements/1.1/"); // Dublin Core Elements
		nameSpace.put("dcterms", "http://purl.org/dc/terms/"); // Dublin Core Terms
		nameSpace.put("dctypes", "http://purl.org/dc/dcmitype/"); // Dublin Core Type Vocabulary
		nameSpace.put("foaf", "http://xmlns.com/foaf/0.1/"); // Friend-of-a-Friend Vocabulary
		nameSpace.put("prov", "http://www.w3.org/ns/prov#"); // Provenance Ontology
		nameSpace.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"); // RDF
		nameSpace.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#"); // RDF Schema
		nameSpace.put("skos", "http://www.w3.org/2004/02/skos/core#"); // Simple Knowledge Organization System
		nameSpace.put("trig", "http://www.w3.org/2004/03/trix/rdfg-1/"); // TriG Named Graphs
		
		// Name spaces for CIDOC-CRM		
		nameSpace.put("crm", "http://www.cidoc-crm.org/cidoc-crm/");
		nameSpace.put("ecrm", "http://erlangen-crm.org/140617/");			
		
		// Name spaces from DBPedia
		nameSpace.put("dbpr", "http://dbpedia.org/resource/");
		nameSpace.put("dbpo", "http://dbpedia.org/ontology/");
		nameSpace.put("dbpp", "http://dbpedia.org/property/");
			
		// Name spaces from Europeana
		nameSpace.put("data", "http://data.europeana.eu/");
		nameSpace.put("edm", "http://www.europeana.eu/schemas/edm/");
		nameSpace.put("eu", "http://www.europeana.eu/");
		
		// Additional name spaces
		nameSpace.put("owl", "http://www.w3.org/2002/07/owl#");
		nameSpace.put("cc", "http://creativecommons.org/ns#");
		nameSpace.put("a", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		nameSpace.put("html", "http://www.w3.org/1999/xhtml/vocab#");
		nameSpace.put("ore", "http://www.openarchives.org/ore/terms/");
		nameSpace.put("wpc", "http://upload.wikimedia.org/wikipedia/commons/a/aa/");
		nameSpace.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		
		// Own name spaces
		nameSpace.put("anno", "http://www.example.org/annotations/");
		nameSpace.put("ind", "http://www.example.org/individuals/");
		nameSpace.put("agent", "http://www.example.org/agents/");
		nameSpace.put("cyc", "http://data.cycladic.gr/artifact/");
		
		keys = new ArrayList<String>();
	}
	
	public static synchronized NameSpaces getInstance() {
		if (NameSpaces.instance == null) {
			NameSpaces.instance = new NameSpaces ();
		}
		return NameSpaces.instance;
	}
	
	public String get(String key) throws Exception {
		if(nameSpace.containsKey(key)) {
			return this.nameSpace.get(key);
		} else if (autoGen.containsKey(key)) {
			return this.autoGen.get(key);
		} else {
			throw new Exception("Namespace " + key + " is not defined!");
		}
	}
	
	public String getKey(String nameSpace) {
		if(this.nameSpace.containsValue(nameSpace)) {
			for (Entry<String, String> entry : this.nameSpace.entrySet()) {
		        if (nameSpace.equals(entry.getValue())) {
		        	keys.add(entry.getKey());
		            return entry.getKey();
		        }
		    }
		} else if (this.autoGen.containsValue(nameSpace)) {
			for (Entry<String, String> entry : this.autoGen.entrySet()) {
		        if (nameSpace.equals(entry.getValue())) {
		        	keys.add(entry.getKey());
		            return entry.getKey();
		        }
		    }			
		} else {
			String key = "ns" + autoGen.size();
			this.autoGen.put(key, nameSpace);
			keys.add(key);
			return this.getKey(nameSpace);
		}
		return null;
	}
	
	public List<String> getKeys() {
		return keys;
	}
	
	public void resetKeys() {
		keys = new ArrayList<String>();
	}

	@Override
	public String toString() {
		List<String> keys = new ArrayList<String>(nameSpace.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");		
		sb.append("Namespaces:");
		sb.append("<table border=0>");
		for(String key: keys) {
			sb.append("<tr><td>");
			sb.append(key);
			sb.append(":</td><td>");
			sb.append(nameSpace.get(key));
			sb.append("</td></tr>");
		}
		sb.append("</table>");
		sb.append("\nNumber of auto generated keys: ");
		sb.append(autoGen.size());
		return sb.toString();
	}	
}
