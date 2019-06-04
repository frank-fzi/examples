package de.fzi.sem.anno.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fzi.sem.anno.model.Iri;
import de.fzi.sem.anno.model.singleton.Mapping;

public class ObjectVisitor implements RDFVisitor {
	private List<Object> objects;
	private boolean coReference = true;
	private boolean literalObjects = true;
	private int maxDistance;
	private int currentDistance;
	
	public ObjectVisitor(int maxDistance) {
		this(maxDistance, 0);
	}
	
	public ObjectVisitor(int maxDistance, int currentDistance) {
		objects = new ArrayList<Object>();
		this.maxDistance = maxDistance;
		this.currentDistance = currentDistance;		
	}

	@SuppressWarnings("unchecked") // ObjectVisitor does always return List<Object>
	@Override
	public List<Object> visitBlank(Resource r, AnonId id) {
		StmtIterator iterator = r.listProperties();
		if(currentDistance < maxDistance) {
			while(iterator.hasNext()) {
				ObjectVisitor ov = new ObjectVisitor(maxDistance, (currentDistance + 1));
				ov.setCoReference(coReference);
				ov.setLiteralObjects(literalObjects);
				objects.addAll((List<Object>) iterator.next().getObject().visitWith(ov));
			}
		}
		return objects;
	}

	@SuppressWarnings("unchecked") // ObjectVisitor does always return List<Object>
	@Override
	public List<Object> visitURI(Resource r, String uri) {
		Mapping mapping = Mapping.getInstance();
		Iri objectIri = new Iri(uri);
		if(coReference) {
			objects.addAll(mapping.listSame(objectIri));
		} else  {
			objects.add(objectIri);
		}
		if(objects.size() < 2 && currentDistance < maxDistance && coReference) {
			StmtIterator iterator = r.listProperties();
			while(iterator.hasNext()) {
				ObjectVisitor ov = new ObjectVisitor(maxDistance, (currentDistance + 1));
				ov.setCoReference(coReference);
				ov.setLiteralObjects(literalObjects);
				objects.addAll((List<Object>) iterator.next().getObject().visitWith(ov));
			}
		}
		return objects;
	}

	@Override
	public List<Object> visitLiteral(Literal l) {
		if(literalObjects) {
			objects.add(l);
		}		
		return objects;
	}

	public void setCoReference(boolean coReference) {
		this.coReference = coReference;
	}

	public void setLiteralObjects(boolean literalObjects) {
		this.literalObjects = literalObjects;
	}		
}
