package de.fzi.sem.anno.util;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;

import de.fzi.sem.anno.model.Iri;

public class LiteralQuery extends QueryUtil{
	
	
	
	public LiteralQuery(Model model) {
		super(model);
	}
	
	public LiteralQuery(String service) {
		super(service);
	}

//	private String queryRequest = null;
	
//	public LiteralQuery(String property, String criteria) {
//		super();
//		this.queryRequest = buildSelectQuery(property, criteria, false, "");
//	}
//	
//	public LiteralQuery(String property, String criteria, String language) {
//		super();
//		this.queryRequest = buildSelectQuery(property, criteria, true, language);
//	}
	
//	public void runQuery() throws Exception {
//		super.runQuery();
//		this.literalResponse();
//	}
	
//	public void runQuery(Model model) throws Exception {
//		super.runQuery(queryRequest, model);
//		this.literalResponse();
//	}
//	
//	public void runQuery(String service) throws Exception {
//		super.runQuery(queryRequest, service);
//		this.literalResponse();
//	}
	
	public void buildSelectQuery(Iri property, String criteria) {
		buildSelectQuery(property, criteria, false, "");
	}
	
	public void buildSelectQuery(Iri property, String criteria, String language) {
		buildSelectQuery(property, criteria, true, language);
	}
	
	private void buildSelectQuery(Iri property, String criteria, boolean filter, String language) {
		StringBuilder select = new StringBuilder();
		select.append("select ?class ?literal where {?class ");
		select.append("<" + property.toString() + ">");
		select.append(" ?literal ");
		select.append(criteria);
		if(filter) select.append(". FILTER(langMatches(lang(?literal), '"+ language +"' ))");
		select.append("}");
		this.queryRequest = select.toString();
	}
	
	// literalResponse() does only work for select queries containing ?class and ?literal
	public String literalResponse() {
		StringBuffer responseStr = new StringBuffer();	
		try {
			ResultSet response = this.getQueryExecution().execSelect();
			while(response.hasNext()){
				QuerySolution soln = response.nextSolution();
				Literal literal = soln.getLiteral("?literal");	
				String name = soln.getResource("?class").getLocalName();
				if(literal != null){
					String literalValue = literal.getString();
					String literalLanguage = literal.getLanguage();
					responseStr.append("\nLiteral value of '" + name + "' (" + literalLanguage.toUpperCase() + "): " + literalValue);				
				}
				else{
					responseStr.append("\nLiteral not found.");
				}		
			}
		} catch (Exception e){
		responseStr.append(e.getMessage());
	} finally { getQueryExecution().close(); }
		return responseStr.toString();
	} // end literalResponse()

}
