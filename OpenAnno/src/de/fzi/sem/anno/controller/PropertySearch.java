package de.fzi.sem.anno.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fzi.sem.anno.model.Iri;
import de.fzi.sem.anno.model.PotentialMatch;
import de.fzi.sem.anno.model.singleton.Mapping;
import de.fzi.sem.anno.model.singleton.Services;
import de.fzi.sem.anno.util.ObjectVisitor;
import de.fzi.sem.anno.util.QueryUtil;
import de.fzi.sem.anno.util.StringVisitor;

public class PropertySearch implements Runnable {
	private OpenAnnoController controller;
	private OpenAnnoSettings settings;
	private Iri predicateIri;
	private RDFNode object;
	private List<PotentialMatch> results;
	private int row;

	public PropertySearch(Iri predicateIri, RDFNode object, int row, OpenAnnoController controller) {
		this.controller = controller;
		this.settings = controller.getSettings();		
		results = controller.getModel().getResults();
		this.row = row;
		this.predicateIri = predicateIri;
		this.object = object;
	}

	@Override
	@SuppressWarnings("unchecked") // ObjectVisitor does always return List<Object>
	public void run() {
		final double timeStart = System.currentTimeMillis(); 
		Mapping mapping = Mapping.getInstance();
		String info = "";
		
		// prepare list of predicates
		List<Iri> predicates;
		if(settings.isPredicateMapping())  {
			predicates = mapping.listEquivalentPredicates(predicateIri);
		} else {
			predicates = new ArrayList<Iri>();
			predicates.add(predicateIri);
		}
		controller.getView().updateSearchTableData(row, 1, predicates);
		
		// prepare list of objects
		List<Object> objects;
		ObjectVisitor visitor = new ObjectVisitor(settings.getMaxDistance());
		visitor.setCoReference(settings.isCoReference());
		visitor.setLiteralObjects(settings.isLitealObjects());
		objects = (List<Object>) object.visitWith(visitor);
		controller.getView().updateSearchTableData(row, 2, objects);
		
		// get all matches from SPARQL endpoint
		Services service = Services.getInstance();
		QueryUtil queryUtil = new QueryUtil(service.get(controller.getModel().getService().getKey()));	
		queryUtil.setTimeout(settings.getTimeout());
		Model subjects = null;
		try{
			subjects = queryUtil.getSubjectStatements(predicates, objects);
		} catch (Exception e) {
			System.out.println("Could not get subjects: "+ e.getMessage());
			info = "Error: " + e.getMessage();
			subjects = null;
		}
		
		
		// show query results	
		if(subjects != null) {
			StmtIterator iterator = subjects.listStatements();
			
			while(iterator.hasNext()) {
				
				// get next match
				Statement statement = iterator.next();
				Iri subjectIri = new Iri(statement.getSubject().getURI());
				Iri predicateIri = new Iri(statement.getPredicate().getURI());
				String objectString = (String) statement.getObject().visitWith(new StringVisitor());		
				PotentialMatch match = new PotentialMatch(subjectIri, controller.getNumberOfProperties(row) );	
				
				// check whether this match is new, add accordance to existing match if not
				Boolean contains = false;
				String newAccordance = subjectIri.getLocalName() + " " + predicateIri.getLocalName() + " " + objectString;
				synchronized(results) {
					for(PotentialMatch compare: results) {
						if(compare.getIri().equals(match.getIri())) {
							contains = true;
							if(!compare.getAccordances().contains(newAccordance)) {
								compare.addAccordance(newAccordance, row);
							}						
						}					
					}
				}			
				
				// add match to list if new
				if(!contains) {		
					match.addAccordance(newAccordance, row);	
					synchronized(results) {
						results.add(match);
					}					
				} // if(!contains)
			} // while(iterator.hasNext())
			Long size = subjects.size();
			final double timeEnd = System.currentTimeMillis(); 
			double multiplier = 1000;
			double execTime = (timeEnd - timeStart) / multiplier;
			DecimalFormat dFormat = new DecimalFormat("#0.00"); 
			info = size.toString() + " (" + dFormat.format(execTime) + "s)"; // by thread " + Thread.currentThread().getId() + "-" + row;
		}
		controller.getView().updateSearchTableData(row, 3, info);		
		if(results.size() > 0) {
			controller.setPropertyMatch(row, true);
			controller.updateResults();
		}		
		controller.setPropertySearched(row, true);
	} // searchAccordances()
}
