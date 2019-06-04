package de.fzi.sem.anno.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.atlas.web.HttpException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import de.fzi.sem.anno.model.Iri;
import de.fzi.sem.anno.model.singleton.NameSpaces;

public class QueryUtil {
	
	private Object source = null;
	private Boolean isService = null;
	protected String queryRequest = null;
	private String queryString = null;
	private QueryExecution queryExecution = null;	
	private long timeout = 0;
	
	public QueryUtil(Model model) {
		this.source = model;
		this.isService = false;
	}
	
	public QueryUtil(String service) {
		this.source = service;
		this.isService = true;
	}
	
	public void runQuery() throws InterruptedException {		
		this.queryString = prepareQueryString(queryRequest);
		System.out.println(this.queryString);
		long startTime = System.currentTimeMillis();
		RunQuery runQuery = new RunQuery(this);
		runQuery.start();
//		synchronized(runQuery) {
//			try {
//				System.out.print("wait for runQuery... ");
//				runQuery.wait(timeout);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			this.queryExecution = runQuery.getQueryExecution();
//		}
		
		while (runQuery.isAlive()) {
			runQuery.join(1000);
            if (((System.currentTimeMillis() - startTime) > timeout) && runQuery.isAlive()) {
            	runQuery.interrupt();
            	runQuery.join();
            }
        }
		this.queryExecution = runQuery.getQueryExecution();
    }
	
	public Model getSubjectStatements(List<Iri> predicates, List<Object> objects) throws HttpException, InterruptedException {
		StringBuilder queryRequest = new StringBuilder();
		queryRequest.append("SELECT ?subject ?predicate ?object WHERE { ");
		queryRequest.append("VALUES (?predicate) {");
		for(Iri predicate: predicates) {
			queryRequest.append("(<");
			queryRequest.append(predicate.toString()); // for full IRI			
//			queryRequest.append(predicate.getCurie());  // prefix:localName
			queryRequest.append(">)");
		}
		queryRequest.append("} ");
		queryRequest.append("VALUES (?object) {");
		for(Object object: objects) {
//			if (object instanceof Literal)  // Only debugging in case of mad literals (type Lexical error)
//				break;
			queryRequest.append("(");
			if(object.getClass() == Iri.class) {
				Iri iri = (Iri) object;
				queryRequest.append("<" + iri.toString() + ">"); // for full IRI
//				queryRequest.append(iri.getCurie()); // prefix:localName
			} else if (object instanceof Literal) {
				Literal literal = (Literal) object;
				queryRequest.append("\'");
				queryRequest.append(literal.getString());
				queryRequest.append("\'");
				if (literal.getLanguage() != "") {
					queryRequest.append("@" + literal.getLanguage());
				}
				if (literal.getDatatypeURI() != null) {
					queryRequest.append("^^<" + literal.getDatatypeURI() + ">");
				}
			} else if (object.getClass() == String.class) {
				queryRequest.append("\'");
				queryRequest.append(object);
				queryRequest.append("\'");
			} else {
				System.out.println("!Unhandled class: " + object.getClass());
			}
			queryRequest.append(")");
		}
		queryRequest.append("} ");
		queryRequest.append("?subject ?predicate ?object");
		queryRequest.append("}");
		queryRequest.append("LIMIT 1000");		
		this.queryRequest = queryRequest.toString();
		
		return this.getSubjectStatements();
	}
	
	private Model getSubjectStatements() throws HttpException, InterruptedException {
		runQuery();				
		Model subjectModel = ModelFactory.createDefaultModel();
		ResultSet response = this.getQueryExecution().execSelect();
		while(response.hasNext()){
			QuerySolution soln = response.nextSolution();				
			Resource subject = soln.getResource("?subject");
			Resource p = soln.getResource("?predicate");
			Property predicate = subjectModel.createProperty(p.getURI());				
			RDFNode object = soln.get("?object");	
			subjectModel.add(subject, predicate, object);					
		}
		return subjectModel;
}
	
	public List<Literal> getLabels (Iri iri) throws InterruptedException {
		List<Literal> labels = new ArrayList<Literal>();
		NameSpaces ns = NameSpaces.getInstance();
		Iri predicate = new Iri(ns, "rdfs", "label");
		
		// select ?label where {<http://dbpedia.org/resource/Capitoline_Wolf> <http://www.w3.org/2000/01/rdf-schema#label> ?label}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ?label WHERE {<");
		sb.append(iri.toString());
		sb.append("> <");
		sb.append(predicate.toString());
		sb.append("> ?label}");
		this.queryRequest = sb.toString();
		runQuery();
		try {
			ResultSet response = this.getQueryExecution().execSelect();
			while(response.hasNext()){
				QuerySolution soln = response.nextSolution();
				labels.add(soln.getLiteral("?label"));				
			}
		}
		catch (Exception e) {
			System.out.println("No label for " + iri.getLocalName() + ": " + e.getMessage() + "(" + e.getClass() + ")");				
		}	
		return labels;
	}
	
	public List<Literal> getLabels (List<Iri> iris) throws InterruptedException {
		List<Literal> labels = new ArrayList<Literal>();
		NameSpaces ns = NameSpaces.getInstance();
		Iri predicate = new Iri(ns, "rdfs", "label");
		
		// select ?label where { VALUES (?subject) { ( dbpr:Iron ) ( dbpr:Copper) } ?subject <http://www.w3.org/2000/01/rdf-schema#label> ?label}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ?label WHERE {");
		sb.append("VALUES (?subject) { ");
		for(Iri iri: iris) {
			sb.append("(<");
			sb.append(iri.toString());
			sb.append(">)");
		}
		sb.append("} ?subject <");
		sb.append(predicate.toString());
		sb.append("> ?label}");
		this.queryRequest = sb.toString();
		runQuery();
		ResultSet response = this.getQueryExecution().execSelect();
		while(response.hasNext()){
			QuerySolution soln = response.nextSolution();
			labels.add(soln.getLiteral("?label"));	
		}
		return labels;
	}

	public List<Resource> getSubjectList(Iri predicate, Iri object) {
		String p = predicate.getCurie();  // prefix:localName
		String o = object.getCurie();  // prefix:localName
		return this.getSubjectList(p, o);
	}
	
	public List<Resource> getSubjectList(String predicate, String object) {
		System.out.print(predicate + " " + object);
		this.queryRequest = "select ?subject where {?subject " + predicate + " " + object + "}";
		return this.getSubjectList();
	}
	
	private List<Resource> getSubjectList() {		
		try {
			runQuery();
			List<Resource> subjectList = new ArrayList<Resource>();
			ResultSet response = this.getQueryExecution().execSelect();
			while(response.hasNext()){
				QuerySolution soln = response.nextSolution();
				subjectList.add(soln.getResource("?subject"));		
			}
			return subjectList;
		} catch (Exception e) {
			return null;
		}		
	}
	
	private String prepareQueryString(String queryRequest) {
		StringBuffer queryStr = new StringBuffer();
		
		// Establish Prefixes
		List<String> keys = new ArrayList<String>();
		keys.add("crm");
		keys.add("rdfs");
		keys.add("rdf");
		keys.add("owl");
		keys.add("dbpo");
		keys.add("dbpr");
		keys.add("dbpp");
		keys.add("dc");
		keys.add("dcterms");
				
		// Add query
		queryStr.append(queryRequest);
		return queryStr.toString();
	}	

	public String getQueryString() {
		return this.queryString;
	}	
	
	protected QueryExecution getQueryExecution() {
		return this.queryExecution;
	}

	public void setQueryRequest(String queryRequest) {
		this.queryRequest = queryRequest;
	}

	public void setQueryExecution(QueryExecution queryExecution) {
		this.queryExecution = queryExecution;
	}

	public boolean isService() {
		return isService;
	}

	public Object getSource() {
		return this.source;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout * 1000;
	}	
	
} // end class
