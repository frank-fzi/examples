package de.fzi.sem.anno.util;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class SparqlService {
	private String service = null;
	private QueryExecution queryExecution = null;
	private boolean run = true;
	
	public SparqlService(String service, String queryString) {
		this(service, queryString, true);
	}

	public SparqlService(String service, String queryString, boolean testService) {
		super();
		this.service = service;
		if(testService) testService();
		if(run) {
			QueryExecution queryExecution = QueryExecutionFactory.sparqlService(service, queryString);		
			this.queryExecution = queryExecution;
		}
	}
	
	private void testService(){
		QueryExecution qe = null;
		String askQuery = "ASK{}";
		qe = QueryExecutionFactory.sparqlService(this.service, askQuery);
		try{
//			if(qe.execAsk()){
//				System.out.println(this.service + " is UP");
//			} // end if
		}catch(QueryExceptionHTTP e){
			System.out.println(this.service + " is DOWN");
			this.run = false;
		}finally{
			qe.close();
		} // end finally
	} // end testService()

	public QueryExecution getQueryExecution() {
		return queryExecution;
	}
}
