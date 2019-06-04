package de.fzi.sem.anno.model.singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class Services {
	
	private static Services instance;
	private Map<String, String> services = new HashMap<String, String>();

	private Services() {
		services.put("eu", "http://europeana.ontotext.com/sparql");
		services.put("dbp", "http://dbpedia.org/sparql");
		services.put("lod", "http://lod.openlinksw.com/sparql");
		services.put("cla", "http://data.clarosnet.org/sparql/");
	}
	
	public static synchronized Services getInstance() {
		if(Services.instance == null) {
			Services.instance = new Services();
		}
		return Services.instance;
	}
	
	public String get(String key){
		return this.services.get(key);
	}
	
	public List<String> getKeys() {
		List<String> keys = new ArrayList<String>(services.keySet());
		Collections.sort(keys);
		return keys;
	}
	
	@Override
	public String toString() {
		List<String> keys = new ArrayList<String>(services.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");		
		sb.append("Services:");
		sb.append("<table border=0>");
		sb.append("<tr><td>");
		sb.append("Key");
		sb.append("</td><td>");
		sb.append("URL");
		sb.append("</td><td>");
		sb.append("Is Up");
		sb.append("</td></tr>");
		for(String key: keys) {
			sb.append("<tr><td>");
			sb.append(key);
			sb.append(":</td><td>");
			sb.append(this.get(key));
			sb.append(":</td><td>");
			sb.append(this.isUp(key));
			sb.append("</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	private boolean isUp(String key) {
		boolean status = false;
		QueryExecution qe = QueryExecutionFactory.sparqlService(this.get(key), "ASK{}");
		try{
			if(qe.execAsk()){
				status = true;
				System.out.println(this.get(key) + " is UP");
			} // end if
		}catch(QueryExceptionHTTP e){
			System.out.println(this.get(key) + " is DOWN: " + e.getMessage());
		}catch(Exception e) {
			System.out.println(this.get(key) + " is DOWN: "  + e.getMessage());
		}finally{
			qe.close();
		}
		return status;
	}	
}
