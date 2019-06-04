package de.fzi.sem.anno.model;


public class RdfSource {
	private Boolean isService;
	private String name;
	private String key;

	public RdfSource(Boolean isService, String name, String key) {
		this.isService = isService;
		this.name = name;
		this.key = key;
	}

	public Boolean isService() {
		return isService;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if(isService) {
			sb.append(" (Service)");
		}
		return sb.toString();
	}	
	
}
