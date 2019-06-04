package de.fzi.sem.anno.model;

import java.util.Arrays;

public class Motivation {
	private enum possibleTypes {
		bookmarking, classifying, commenting, describing, editing, highlighting,
		identifying, linking, moderating, questioning, replying, tagging
	} 
	private String type = null;
	
	public Motivation(String type){
		try {
			possibleTypes.valueOf(type);
			this.type = type;
		} catch (Exception e) {
			System.out.println("Not a valid type for motivation. Possible types are " + Arrays.asList(possibleTypes.values()));
		}	
	}
	
	public String getType() {
		return this.type;
	}
	
	static public String[] getPossibleTypes() {
		possibleTypes[] values = possibleTypes.values();
		String[] types = new String[values.length];
		
		for(int i = 0; i < values.length; i++) {
			types[i] = values[i].name();
		}
		return types;
	}

	@Override
	public String toString() {
		return "Motivation [type=" + type + "]";
	}	
}
