package de.fzi.sem.anno.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import de.fzi.sem.anno.model.singleton.NameSpaces;


public class Agent {	
	/*
	 *  this class represents the oadm-agents
	 *  foaf:Person or foaf:Organization for annotatedBy, see http://xmlns.com/foaf/spec/ for details
	 *  prov:SoftwareAgent for serializedBy
	 */
	private enum possibleTypes {Person, Organization, SoftwareAgent} 
	private String name = null; // foaf:name
	private String localName = null;
	private String type = null;
	private String agent_ns = null;
	private NameSpaces ns = null;
	private List<String> keys = new ArrayList<String>();
	
	public Agent(String name, String type) throws Exception {
		this(name, type, NameSpaces.getInstance());
	}
	
	public Agent(String name, String type, NameSpaces ns) throws Exception {
		this.localName = UUID.randomUUID().toString();
		this.ns = ns;
		this.keys.add("agent");
				
		// Add name
		this.name = name;
		
		// Add Type			
		try {
			possibleTypes.valueOf(type);
			this.type = type;
			if(type == "SoftwareAgent")
				agent_ns = ns.get("prov");
			else
				agent_ns = ns.get("foaf");
		} catch (Exception e) {
			System.out.println("Not a valid type for agent. Possible types are " + Arrays.asList(possibleTypes.values()));
		}		
//		predicate = model.createProperty(ns.get("rdf") + "Type");
//		object = model.createResource(agent_ns + this.type);
//		subject.addProperty(predicate, object);
	} // end constructor
		
	public List<String> getKeys() {
		return keys;
	}

	public String getName() {
		return this.name;
	}	
		
	public String getType() {
		return type;
	}

	public String getLocalName() {
		return this.localName;
	}
	
	public String getUri() {
		try {
			return ns.get("agent") + this.localName;
		} catch (Exception e) {
			return "Undefined namespace";
		}
	}
	
	public Model toModel() {
		Model model = ModelFactory.createDefaultModel(); 
		try {
			// set prefixes
			for(String k: keys) {
				model.setNsPrefix(k, ns.get(k));
			}
			// Prepare statements
			Resource agent = model.createResource(ns.get("agent") + this.localName);;
			Property predicate = null;
			Resource object = null;
			
			// type
			predicate = model.createProperty(ns.get("rdf") + "Type");
			object = model.createResource(agent_ns + this.type);
			agent.addProperty(predicate, object);	
			
			// name
			predicate = model.createProperty(ns.get("foaf") + "name");
			agent.addProperty(predicate, this.name);
			
			System.out.println("Model created: " + this.toString());
		} catch (Exception e) {
			System.out.println("Could not create RDF-Model for " + this.toString() + ": " + e.getMessage());
		}		
		return model;
	}

	@Override
	public String toString() {
		return name + " (" + type + ")";
	}
}
