package de.fzi.sem.anno.model.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

import de.fzi.sem.anno.controller.OpenAnnoController;
import de.fzi.sem.anno.model.Iri;

public class Mapping {
	// mapping from CIDOC-CRM to some other
	private static Mapping instance;
	private OpenAnnoController controller;
	NameSpaces ns = null;

	private Mapping() {
		ns = NameSpaces.getInstance();
	}
	
	public static synchronized Mapping getInstance() {
		if(Mapping.instance == null) {
			Mapping.instance = new Mapping();
		}
		return Mapping.instance;
	}
	
	private Iri getRootPredicate(Iri predicateIn) {
		Iri predicateOut = predicateIn;
		switch (predicateIn.getLocalName()) {
		case "P48_has_preferred_identifier":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by");
			break;
		
		case "P50_has_current_keeper":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P49_has_former_or_current_keeper");
			break;
			
		case "P52_has_current_owner":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P51_has_former_or_current_owner");
			break;
			
		case "P55_has_current_location":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P53_has_former_or_current_location");
			break;
			
		case "P56_bears_feature":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P46_is_composed_of");
			break;
			
		case "P65_shows_visual_item":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P130_shows_features_of");
			break;
			
		case "P102_has_title":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by");
			break;
			
		case "P128_carries":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P130_shows_features_of");
			break;
			
		case "P137_exemplifies":
			predicateOut = new Iri ("http://www.cidoc-crm.org/cidoc-crm/P2_has_type");
			break;		
		}
		return predicateOut;
	}
	
	public List<Iri> listEquivalentPredicates (Iri predicateIn) {
		List<Iri> predicates = new ArrayList<Iri>();
		predicates.add(predicateIn);
		Iri rootPredicate = getRootPredicate(predicateIn);
		if (!rootPredicate.equals(predicateIn)) {
			predicates.add(rootPredicate);
		}
		
		switch (rootPredicate.getLocalName()) {		
			case "P1_is_identified_by":
				predicates.add(new Iri("http://purl.org/dc/elements/1.1/title"));
				predicates.add(new Iri("http://xmlns.com/foaf/0.1/name"));
				predicates.add(new Iri("http://dbpedia.org/property/title"));
				predicates.add(new Iri("http://www.w3.org/2000/01/rdf-schema#label"));
				break;
				
			case "P2_has_type":
				predicates.add(new Iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
				predicates.add(new Iri("http://dbpedia.org/property/type"));
				break;
				
			case "P3_has_note":
				predicates.add(new Iri("http://www.w3.org/2000/01/rdf-schema#label"));
				predicates.add(new Iri("http://www.w3.org/2000/01/rdf-schema#comment"));
				break;
				
			case "P43_has_dimension":
				predicates.add(new Iri("http://dbpedia.org/property/heightMetric"));
				predicates.add(new Iri("http://dbpedia.org/property/lengthMetric"));
				break;
				
			case "P44_has_dimension":
				// currently no adequate mapping available
				break;
				
			case "P45_consists_of":
				predicates.add(new Iri("http://dbpedia.org/property/type"));
				predicates.add(new Iri("http://dbpedia.org/property/material"));				
				predicates.add(new Iri("http://purl.org/dc/terms/medium"));
				break;
				
			case "P46_is_composed_of":
				predicates.add(new Iri("http://purl.org/dc/terms/hasPart"));
				break;
			
			case "P49_has_former_or_current_keeper":
				predicates.add(new Iri("http://dbpedia.org/ontology/museum"));
				break;
				
			case "P51_has_former_or_current_owner":
				predicates.add(new Iri("http://dbpedia.org/ontology/museum"));
				break;
				
			case "P53_has_former_or_current_location":
				predicates.add(new Iri("http://dbpedia.org/ontology/museum"));
				break;
			
			case "P54_has_current_or_permanten_location":
				predicates.add(new Iri("http://dbpedia.org/ontology/museum"));
				break;
			
			case "P57_has_number_of_parts":
				// currently no adequate mapping available
				break;
				
			case "P58_has_section_definition":
				// currently no adequate mapping available
				break;
				
			case "P59_has_section":
				// currently no adequate mapping available
				break;
			
			case "P62_depicts":
				predicates.add(new Iri("http://purl.org/dc/terms/coverage"));
				break;
				
			case "P101_has_general_use":
				// currently no adequate mapping available
				break;
				
			case "P103_was_intended_for":
				predicates.add(new Iri("http://purl.org/dc/terms/isRequiredBy"));
				break;
			
			case "P104_is_subject_to":
				// currently no adequate mapping available
				break;
				
			case "P105_right_held_by":
				// currently no adequate mapping available
				break;
				
			case "P130_shows_features_of":
				predicates.add(new Iri("http://purl.org/dc/terms/isVersionOf"));				
				break;
			
			case "P156_occupies":
				// currently no adequate mapping available
				break;
				
			case "P159_occupied":
				// currently no adequate mapping available
				break;	
		}
		return predicates;
	}
	
	public List<Iri> listSame (Iri iri) {
		
		// get Iris
		URL url = null;
		List<Iri> iris = new ArrayList<Iri>();
		if(iri.toString().equals("http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object")) {
			iris.add(new Iri("http://erlangen-crm.org/current/E22_Man-Made_Object"));
			iris.add(new Iri("http://purl.org/NET/crm-owl#E22_Man-Made_Object"));
		}
//	    List<Object> resultList = new ArrayList<Object>();
		try {
			String encodedIri = URLEncoder.encode(iri.toString(), "UTF-8");
			url = new URL("http://sameas.org/json?uri=" + encodedIri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (InputStream is = url.openStream(); JsonReader rdr = Json.createReader(is)) {		
			JsonObject obj = rdr.readArray().getJsonObject(0);
			JsonArray duplicates = obj.getJsonArray("duplicates");
			for (JsonString duplicate : duplicates.getValuesAs(JsonString.class)) {
				Iri dIri = new Iri(duplicate.toString());				
				List<String> ignoredLocalNames = controller.getIgnoredLocalNames();
				if(ignoredLocalNames.contains(dIri.getLocalName().toLowerCase()) ) {
					System.out.println(dIri.getCurie() + " ignored in Mapping.listSame()");
				} else {
					System.out.println(iri.getCurie() + "-->" + dIri.getCurie() + " in Mapping.listSame()");
					iris.add(dIri);
				}				
			}
		} catch (IOException e) {
			System.out.println("Error in Mapping.listSame(): " + e.getMessage());
		}	
		return iris;
	}

	public OpenAnnoController getController() {
		return controller;
	}

	public void setController(OpenAnnoController controller) {
		this.controller = controller;
	}
}
