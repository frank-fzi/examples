package de.fzi.sem.anno.util;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fzi.sem.anno.model.Iri;

public class StringVisitor implements RDFVisitor {

	@Override
	public String visitBlank(Resource r, AnonId id) {
		StringBuilder sb = new StringBuilder();
		StmtIterator iterator = r.listProperties();
		while(iterator.hasNext()) {
			sb.append(" ");
			sb.append(iterator.next().getObject().visitWith(new StringVisitor()));
		}
		return sb.toString();
	}

	@Override
	public String visitURI(Resource r, String uri) {
		Iri iri = new Iri(uri);
		return (iri.getNameSpaceKey() + ":" + iri.getLocalName());
	}

	@Override
	public String visitLiteral(Literal l) {
		return ("\"" + l.getString() + "\"");
	}

}
