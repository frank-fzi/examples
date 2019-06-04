package de.fzi.sem.anno.model.singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilePath {
	
	private static FilePath instance;
	private Map<String, String> filePath = new HashMap<String, String>();

	private FilePath() {
		filePath.put("in", "Input/input.owl");
		filePath.put("data", "Input/data.rdf");		
		filePath.put("agent", "Input/agents.rdf");
		
		filePath.put("BCE33914", "Input/data/BCE33914.ttl");
		filePath.put("BCE33989", "Input/data/BCE33989.ttl");
		filePath.put("BCE33994", "Input/data/BCE33994.ttl");
		filePath.put("BCE34005", "Input/data/BCE34005.ttl");
		filePath.put("BCF303", "Input/data/BCF303.ttl");
		filePath.put("COC33890", "Input/data/COC33890.ttl");
		filePath.put("COC33898", "Input/data/COC33898.ttl");
		filePath.put("COC33934", "Input/data/COC33934.ttl");
		filePath.put("COC33957", "Input/data/COC33957.ttl");
		filePath.put("COC33976", "Input/data/COC33976.ttl");
		
		filePath.put("839", "Input/data/839.rdf");
		filePath.put("910", "Input/data/910.rdf");
		filePath.put("916", "Input/data/916.rdf");
		filePath.put("1003", "Input/data/1003.rdf");
		filePath.put("1106", "Input/data/1106.rdf");
		
		filePath.put("FR00410", "Input/data/FR00410.rdf");
		filePath.put("DZ01842", "Input/data/DZ01842.rdf");
		filePath.put("FR00167", "Input/data/FR00167.rdf");
		filePath.put("YU04073", "Input/data/YU04073.rdf");
		filePath.put("GR00014", "Input/data/GR00014.rdf");
		
		
		filePath.put("oa", "Ontologies/oa.owl");
		filePath.put("crm", "Ontologies/cidoc_crm_v5.1-draft-2014March.owl");
		filePath.put("lupa", "Ontologies/lupaCapitolina.owl");
		filePath.put("oa-rdf", "Ontologies/OADM.rdf");
		filePath.put("oa-ttl", "Ontologies/OADM.ttl");
		filePath.put("out", "Output/oadm.");
		filePath.put("test", "Output/test.rdf");		
	}
	
	public static synchronized FilePath getInstance() {
		if(FilePath.instance == null) {
			FilePath.instance = new FilePath();
		}
		return FilePath.instance;
	}
	
	public String get(String key) throws Exception{
		if(filePath.containsKey(key)) {
			return this.filePath.get(key);
		} else {
			throw new Exception("Key " + key + " is not a valid file path key!");
		}
	}
	
	@Override
	public String toString() {
		List<String> keys = new ArrayList<String>(filePath.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");		
		sb.append("File Paths:");
		sb.append("<table border=0>");
		for(String key: keys) {
			sb.append("<tr><td>");
			sb.append(key);
			sb.append(":</td><td>");
			sb.append(filePath.get(key));
			sb.append("</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
}
