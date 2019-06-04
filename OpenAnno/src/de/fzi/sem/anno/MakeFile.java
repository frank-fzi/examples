package de.fzi.sem.anno;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.PropertyConfigurator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;




public class MakeFile {
	public static void main(String[] args) {
		PropertyConfigurator.configure("jena-log4j.properties");
		String name = "839";
//		String uri = "http://www.mae.u-paris10.fr/limc-france/LIMC-objet.php?code_objet=" + name;
		String uri = "http://data.cycladic.gr/artifact/" + name;
		String encUri;
		try {
			encUri = URLEncoder.encode(uri, "UTF-8");
			String request = "http://data.clarosnet.org/doc/?uri=" + encUri;
			Model model = ModelFactory.createDefaultModel();
//			System.out.println(request);
	        model.read(request);
	        model.write(System.out);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
