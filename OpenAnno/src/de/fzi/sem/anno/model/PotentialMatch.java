package de.fzi.sem.anno.model;

import java.util.ArrayList;
import java.util.List;

import de.fzi.sem.anno.model.singleton.NameSpaces;

public class PotentialMatch implements Comparable<PotentialMatch> {
	private String title = null;
	private Iri subjectIri = null;
	private List<String> accordances;
	private Boolean suggested = null;
	private Boolean selected = null;
	private Boolean hasTitle = null;
	private boolean[] propertiesMatch;
	
//	public PotentialMatch(String title, NameSpaces ns, String nameSpaceKey, String localName) {
//		this.title = title;
//		this.subjectIri = new Iri(ns, nameSpaceKey, localName);
//	}
	
	public PotentialMatch(String title, String nameSpace, String localName, Integer numberOfProperties) throws Exception {
		this.title = title;
		NameSpaces ns = NameSpaces.getInstance();
		this.subjectIri = new Iri(ns, ns.getKey(nameSpace), localName);
		hasTitle = false;
		this.accordances = new ArrayList<String>();
		this.propertiesMatch = new boolean[numberOfProperties];
	}

	public PotentialMatch(String title, Iri subjectIri, Integer numberOfProperties) {
		this.title = title;
		this.subjectIri = subjectIri;
		hasTitle = false;
		this.accordances = new ArrayList<String>();
		this.propertiesMatch = new boolean[numberOfProperties];
	}

	public PotentialMatch(Iri subjectIri, Integer numberOfProperties) {
		this.subjectIri = subjectIri;
		hasTitle = false;
		this.accordances = new ArrayList<String>();
		this.propertiesMatch = new boolean[numberOfProperties];
	}
	
	public int numberOfPropertyMatches() {
		int number = 0;
		for(boolean b: propertiesMatch) {
			if(b) {
				number++;
			}
		}
		return number;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		this.hasTitle = true;
	}

	public String getNameSpace() {
		return subjectIri.getNameSpace();
	}

	public String getLocalName() {
		return subjectIri.getLocalName();
	}
	
	public Iri getIri() {
		return this.subjectIri;
	}
	
	public List<String> getAccordances() {
		return accordances;
	}
	
	public void addAccordance(String triple, int row) {
		accordances.add(triple);
		propertiesMatch[row] = true;
	}
	
	public void setSuggested(Boolean suggest){
		suggested = suggest;
	}
	
	public Boolean isSuggested() {
		return suggested;
	}
	
	public void setSelected(Boolean select){
		selected = select;
	}
	
	public Boolean isSelected(){
		return selected;
	}	

	public Boolean hasTitle() {
		return hasTitle;
	}

	@Override
	public int compareTo(PotentialMatch match) {
		int thisAccordances = this.accordances.size();
		int compareAccordances = match.getAccordances().size();
		return (compareAccordances - thisAccordances);
	}
}
