package de.fzi.sem.anno.model;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Literal;

import de.fzi.sem.anno.model.singleton.NameSpaces;
import de.fzi.sem.anno.model.singleton.Services;
import de.fzi.sem.anno.util.LiteralQuery;
import de.fzi.sem.anno.util.QueryUtil;

public class Iri {
	private NameSpaces ns = null;
	private String nameSpaceKey = null;
	private String localName = null;
	
	public Iri(String iri) {
		this.ns = NameSpaces.getInstance();
		String nameSpace = null;
		if(iri.contains("#")) {
			this.localName = iri.substring( iri.lastIndexOf('#') + 1 );
			nameSpace = iri.substring( 0, iri.lastIndexOf('#') +1 );
		} else {
			this.localName = iri.substring( iri.lastIndexOf('/') + 1 );
			nameSpace = iri.substring( 0, iri.lastIndexOf('/') +1 );
		}
		this.nameSpaceKey = ns.getKey(nameSpace);
	}
	
	public Iri(String nameSpace, String localName) throws Exception {
		this.ns = NameSpaces.getInstance();
		this.nameSpaceKey = ns.getKey(nameSpace);
		this.localName = localName;
	}
	
	public Iri(NameSpaces ns, String nameSpaceKey, String localName) {
		this.ns = ns;
		this.nameSpaceKey = nameSpaceKey;
		this.localName = localName;
	}
	
	public String getNameSpaceKey() {
		return this.nameSpaceKey;
	}
	
	public String getNameSpace() {
		try {
			return this.ns.get(nameSpaceKey);
		} catch (Exception e) {
			return ("Name space \"" + nameSpaceKey + "\" not found: " + e.getMessage());
		}
	}	

	public String getLocalName() {
		return localName;
	}
	
	public String getCurie() {
		StringBuilder sb = new StringBuilder();
		sb.append(nameSpaceKey);
		sb.append(":");
		sb.append(localName);
		return sb.toString();
	}

	@Override
	public String toString() {
		try {
			return (ns.get(nameSpaceKey) + localName);
		} catch (Exception e) {
			return ("Could not create IRI: "+ e.getMessage());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localName == null) ? 0 : localName.hashCode());
		result = prime * result
				+ ((nameSpaceKey == null) ? 0 : nameSpaceKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Iri other = (Iri) obj;
		if (localName == null) {
			if (other.localName != null)
				return false;
		} else if (!localName.equals(other.localName))
			return false;
		if (nameSpaceKey == null) {
			if (other.nameSpaceKey != null)
				return false;
		} else if (!nameSpaceKey.equals(other.nameSpaceKey))
			return false;
		return true;
	}

	public List<Literal> getLabels() throws InterruptedException {		
		Services services = Services.getInstance();
		QueryUtil qUtil = new LiteralQuery(services.get("lod"));
		return qUtil.getLabels(this);
	}
}
