package de.fzi.sem.anno.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import de.fzi.sem.anno.model.singleton.NameSpaces;

public class OADM {
	
	
	/*
	 * This class is an implementation of the Open Annotation Core (http://www.openannotation.org/spec/core/) in Java
	 * 
	 * PROPERTIES:
	 * target: IRI of CRM-Object to be annotated
	 * body: IRI(s) of resource(s) to be annotated, e.g. from DBPedia, Europeana etc.
	 * agent1 (annotadedBy)
	 * agent2 (serializedBy, may be static or application specific)
	 * motivation
	 * time (annotatedAt)
	 * 
	 * 
	 * METHODS:
	 * constructor: needs target, agents, motivation  (WHO wants to annotate WHAT and WHY)
	 * 	with
	 * 		agent1 = annotadedBy foaf:person, foaf:organization
	 * 		agent2 = serializedBy prov:SoftwareAgent (prov:SoftwareAgent, e.g. 'OpenAnno')
	 * 		motivation = bookmarking, classifying, commenting, describing, editing, highlighting, identifying, linking, moderating, questioning, replying, tagging
	 * addBody(): adds the IRI(s) of a body ressource(s)
	 * create(): creates an RDF-File with the serialized OADM-Object and the time for serializedAt
	 * 
	 */
	
	/*
	 * Properties
	 */
	private NameSpaces ns = null;	
	private Iri target = null;
	private List<Iri> body = new ArrayList<Iri>();
	private Agent annotatedBy = null;
	private Agent serializedBy = null;
	private Calendar annotatedAt = null;
	private Calendar serializedAt = null;
	private Motivation motivation = null;
	private List<String> keys = new ArrayList<String>();
	private String localName = null;
	private SimpleDateFormat dateFormat = null;

	/*
	 * Constructor
	 */
	public OADM(String nameSpaceKey) throws Exception {
		this(NameSpaces.getInstance(), nameSpaceKey);
	}	

	public OADM(NameSpaces ns, String nameSpaceKey) throws Exception {
		this.ns = ns;
		keys.add(nameSpaceKey);
		this.localName = UUID.randomUUID().toString();
		dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss '(UTC 'XXX')'");
		
		// public name spaces
		keys.add("oa");
		keys.add("cnt");
		keys.add("dc");
		keys.add("dcterms");
		keys.add("dctypes");
		keys.add("foaf");
		keys.add("prov");
		keys.add("rdf");
		keys.add("rdfs");
		keys.add("skos");
		keys.add("trig");
		keys.add("xsd");
	}
	
	/*
	 *  Setter
	 */	
	public void setTarget(Iri iri) {
		this.target = iri;
		this.annotatedAt = Calendar.getInstance();		
	}

	public void setAnnotatedBy(Agent annotatedBy) {
		this.annotatedBy = annotatedBy;
		this.annotatedAt = Calendar.getInstance();
	}

	public void setSerializedBy(Agent serializedBy) {
		this.serializedBy = serializedBy;
		this.annotatedAt = Calendar.getInstance();
	}

	public void setMotivation(String m) {
		this.motivation = new Motivation(m);	
		this.annotatedAt = Calendar.getInstance();
		System.out.println("Motivation changed to \"" + this.motivation.getType() + "\".");
	}
	
	public void addKey(String key) {
		keys.add(key);
	}
	
	public void setBody() {
		this.body = new ArrayList<Iri>();
	}

	public void addBody(Iri iri) {
		body.add(iri);		
	}	
	
	/*
	 * Getter
	 */
	public List<String> getKeys() {
		return keys;
	}
	
	public String getLocalName() {
		return localName;
	}

	public Agent getAnnotatedBy() {
		return annotatedBy;
	}

	public Agent getSerializedBy() {
		return serializedBy;
	}

	public Motivation getMotivation() {
		return motivation;
	}

	public Iri getTarget() {
		return target;
	}

	public List<Iri> getBody() {
		return body;
	}
	
	/*
	 * build RDF-Model
	 */	
	public Model toModel() {
		Model model = ModelFactory.createDefaultModel(); 
		
		try {
			// set prefixes
			for(String k: keys) {
				model.setNsPrefix(k, ns.get(k));
			}
			
			// prepare statements
			Resource annotation = model.createResource(ns.get("anno") + this.localName);			
			Property predicate = null;
			Resource object = null;
			
			// set RDF-type			
			predicate = model.createProperty(ns.get("rdf") + "type");
			object = model.createResource(ns.get("oa") + "Annotation");
			annotation.addProperty(predicate, object);	
			
			// serialized by
			predicate = model.createProperty(ns.get("oa") + "serializedBy");
			object = model.createResource(serializedBy.getUri());
			annotation.addProperty(predicate, object);
			
			// serialized at
			if(serializedAt == null)
				serializedAt = Calendar.getInstance();
			predicate = model.createProperty(ns.get("oa") + "serializedAt");
			annotation.addProperty(predicate, model.createTypedLiteral(this.serializedAt)); 
			
			// annotated by
			predicate = model.createProperty(ns.get("oa") + "annotatedBy");
			object = model.createResource(annotatedBy.getUri());
			annotation.addProperty(predicate, object); 
			
			// annotated at
			if(annotatedAt == null)
				annotatedAt = Calendar.getInstance();
			predicate = model.createProperty(ns.get("oa") + "annotatedAt");
			annotation.addProperty(predicate, model.createTypedLiteral(this.annotatedAt));

			// motivation
			predicate = model.createProperty(ns.get("oa") + "motivatedBy");
			object = model.createResource(ns.get("oa") + this.motivation.getType());
			annotation.addProperty(predicate, object);
			
			// add target
			predicate = model.createProperty(ns.get("oa") + "hasTarget");
			object = model.createResource(this.target.toString());
			annotation.addProperty(predicate, object);
			
			// add body resources
			for(Iri iri: body) {
				predicate = model.createProperty(ns.get("oa") + "hasBody");
				object = model.createResource(iri.toString());
				annotation.addProperty(predicate, object);
			}
			
			System.out.println("Model created: " + this.toString());
		} catch (Exception e) {
			System.out.println("Could not create RDF-Model for " + this.toString() + ": " + e.getMessage());
		}		
		return model;
	}

	/*
	 * build String
	 */
	@Override
	public String toString() {
		StringBuilder oadm = new StringBuilder();
		oadm.append("Local name: " + localName);
		oadm.append("\nSerialized by: " + serializedBy.getName());
		oadm.append(" (" + serializedBy.getType() + ")");
		oadm.append("\nSerialized at: " + dateFormat.format(serializedAt.getTime()));
		oadm.append("\nAnnotaded by: " + annotatedBy.getName());
		oadm.append(" (" + annotatedBy.getType() + ")");
		oadm.append("\nAnnotated at: " + dateFormat.format(annotatedAt.getTime()));
		oadm.append("\nMotivation: " + motivation.getType());
		oadm.append("\nTarget: " + target);
		oadm.append("\n\nAnnotaded ressources: ");
		for(Iri iri: body) {
			oadm.append("\n" + iri.toString());
		}		
		return oadm.toString();
	}	
	
	public String getHtml()	{
		StringBuilder html = new StringBuilder();
		html.append("<table><tr><td>Local name:</td><td>");
		html.append(localName);
		html.append("</td></tr><tr><td>Serialized by:</td><td>");
		html.append(serializedBy.getName());
		html.append(" (" + serializedBy.getType() + ")");
		html.append("</td></tr><tr><td>Serialized at:</td><td>");
		html.append(dateFormat.format(serializedAt.getTime()));
		html.append("</td></tr><tr><td>Annotaded by:</td><td>");
		html.append(annotatedBy.getName());
		html.append(" (" + annotatedBy.getType() + ")");
		html.append("</td></tr><tr><td>Annotated at:</td><td>");
		html.append(dateFormat.format(annotatedAt.getTime()));
		html.append("</td></tr><tr><td>Motivation:</td><td>");
		html.append(motivation.getType());
		html.append("</td></tr><tr><td>Target:</td><td>");
		html.append(target);
		html.append("</td></tr></table>");
		html.append("<br>Annotaded ressources: ");
		int limit = 10;
		int count = 0;
		for(Iri iri: body) {
			count++;
			html.append("<br>" + iri.toString());
			if(count > limit) {
				html.append("<br> ...");
				html.append(body.size() - limit);
				html.append(" more");
				break;
			}
		}
		return html.toString();
	}
}
