package de.fzi.sem.anno.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;

import de.fzi.sem.anno.model.singleton.FilePath;
import de.fzi.sem.anno.model.singleton.NameSpaces;
import de.fzi.sem.anno.model.singleton.Services;
import de.fzi.sem.interfaces.IModel;

public class OpenAnnoModel implements IModel {
	
	private NameSpaces ns;
	private FilePath fp;
	private List<RdfSource> services;
	private RdfSource service;
	private String appName;
	private List<PotentialMatch> results;	
	private List<Iri> targets;
	private List<Agent> agents;
	private OADM anno;
	private List<String> targetNs;
	private Properties settings = null;
	
	public OpenAnnoModel() {
		super();
		this.appName = "OpenAnno 0.4b";
		loadSettings();
	}		
	
	public boolean loadSettings() {
		String filePath = "OpenAnno.properties";
		try {
			FileInputStream input = new FileInputStream(new File(filePath));
			this.settings = new Properties();
			settings.load(input);
			input.close();			

			Enumeration<Object> enuKeys = settings.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = settings.getProperty(key);
				System.out.println(key + ": " + value);
			}
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(filePath + " not found: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("Properties could not be loaded: " + e.getMessage());
			return false;
		}
	}
	
	public String getSetting(String key) {
		return this.settings.getProperty(key);
	}
	
	public int getIntSetting(String key) {
		return Integer.parseInt(this.settings.getProperty(key));
	}
	
	public boolean getBoolSetting(String key) {
		return Boolean.parseBoolean(this.settings.getProperty(key));
	}
	
	public long getLongSetting(String key) {
		return Long.parseLong(this.settings.getProperty(key));
	}
	
	public void setup(String in) {
		this.ns = NameSpaces.getInstance();	
		this.fp = FilePath.getInstance();
		this.services = loadServices();
		this.service = new RdfSource(true, "DBpedia", "dbp");
		this.results = Collections.synchronizedList(new ArrayList<PotentialMatch>());
		this.targetNs = new ArrayList<String>();
		
		// valid namespaces for targets
		this.targetNs.add("http://www.example.org/individuals/");
		this.targetNs.add("http://data.cycladic.gr/artifact/");
		this.targetNs.add("http://collection.britishmuseum.org/id/object/");
		this.targetNs.add("http://data.clarosnet.org/doc/");
		this.targetNs.add("http://www.mae.u-paris10.fr/limc-france/LIMC-objet.php?code_objet=FR/NI/");
		this.targetNs.add("http://www.mae.u-paris10.fr/limc-france/LIMC-objet.php?code_objet=FR/CL/");
		this.targetNs.add("http://www.mae.u-paris10.fr/limc-france/LIMC-objet.php?code_objet=FR/AVS/");
		this.targetNs.add("http://www.mae.u-paris10.fr/limc-france/LIMC-objet.php?code_objet=FR/AN/");
		this.targetNs.add("http://www.mae.u-paris10.fr/limc-france/LIMC-objet.php?code_objet=FR/AKS/");
		this.targets = loadTargetIris(in);
		this.agents = loadAgentIris();
		this.anno = prepareAnno();
	}

	// targets
	private List<Iri> loadTargetIris(String in) {
		List<Iri> targets = new ArrayList<Iri>();
		try {			
			Model input = new RDF(fp.get(in)).getModel();
			input.write(new FileOutputStream(fp.get("test")), "RDF/XML");
			ResIterator subjects = input.listSubjects();			 
			while(subjects.hasNext()) {
				Resource r = subjects.next();
				if(r.isURIResource()) {
					Iri iri = new Iri(r.getURI());
					if(targetNs.contains(iri.getNameSpace())) {
						targets.add(iri);
					} else {
						System.out.println("Namespace " + iri.getNameSpace() + " is not a valid namespace for targer resources.");
					}
				}
			}
			if(targets.size() == 0) {
				System.out.println("WARNING: no targets in " + fp.get(in));
			}
		} catch (Exception e) {
			System.out.println("Could not initialize targets: " + e.getMessage());
			e.printStackTrace();
		}
		return targets;
	}				

	// agents
	private List<Agent> loadAgentIris() {	
		List<Agent> agents = new ArrayList<Agent>();
		try {
			Model input = new RDF(fp.get("agent")).getModel();
			Property pName = input.getProperty(ns.get("foaf"), "name");
			Property pType = input.getProperty(ns.get("rdf"), "type");
			ResIterator subjects = input.listSubjectsWithProperty(pName);
			while(subjects.hasNext()) {
				Resource subject = subjects.next();
				String agentName = subject.getProperty(pName).getLiteral().getString();
				String agentType = subject.getProperty(pType).getResource().getLocalName();
				agents.add(new Agent(agentName, agentType, ns));
			}
		} catch (Exception e) {
			System.out.println("Could not initialize agents: " + e.getMessage());
			e.printStackTrace();
		}
		return agents;
	}
	
	private List<RdfSource> loadServices() {
		List<RdfSource> sources = new ArrayList<RdfSource>();
		List<String> keys = Services.getInstance().getKeys();
		for (String key: keys) {
			sources.add(new RdfSource(true, key, key));
		}
		return sources;
	}
	
	// OADM
	private OADM prepareAnno() {
		OADM  anno = null;
		try {
			anno = new OADM(ns, "anno");
			anno.setMotivation("editing");
			anno.setTarget(targets.get(0));
			anno.addKey(anno.getTarget().getNameSpaceKey());
			anno.setSerializedBy(new Agent(getAppName(), "SoftwareAgent", ns));
			anno.setAnnotatedBy(agents.get(0));
		} catch (Exception e) {
			System.out.println("Could not prepare model for annotation: " + e.getMessage());
			e.printStackTrace();
		}
		return anno;
	}

	public NameSpaces getNs() {
		return ns;
	}

	/* (non-Javadoc)
	 * @see de.fzi.sem.anno.model.IModel#getAppName()
	 */
	@Override
	public String getAppName() {
		return this.appName;
	}

	public List<PotentialMatch> getResults() {
		return results;
	}

	public List<Iri> getTargets() {
		return targets;
	}

	public List<Agent> getAgents() {
		return agents;
	}

	public OADM getAnno() {
		return anno;
	}

	public FilePath getFp() {
		return fp;
	}

	public void clearResults() {
		this.results = new ArrayList<PotentialMatch>();			
	}

	public List<RdfSource> getServices() {
		return services;
	}

	public RdfSource getService() {
		return service;
	}

	public void setService(RdfSource service) {
		this.service = service;
	}

	public Model getInput(String in) {
		Model input = null;
		try {
			input = new RDF(fp.get(in)).getModel();
		} catch (Exception e) {
			System.out.println("Could not open model \"in\", using default model instead.");
			input = ModelFactory.createDefaultModel();
		}
		return input;
	}
}
