package de.fzi.sem.anno.util;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class RunQuery extends Thread {
	private QueryUtil util;
	private QueryExecution queryExecution;	

	public RunQuery(QueryUtil util) {
		this.util = util;
	}

	@Override
	public void run() {
		synchronized(this) {
			if(util.isService()) {
				SparqlService sparqlService = new SparqlService((String) util.getSource(), util.getQueryString());
				this.queryExecution = sparqlService.getQueryExecution();
			} else {
				Model model = (Model) util.getSource();
				Query query = QueryFactory.create(util.getQueryString());
				this.queryExecution = QueryExecutionFactory.create(query, model);
			}
			notify();
		}		
	}

	public QueryExecution getQueryExecution() {
		return queryExecution;
	}	
}
