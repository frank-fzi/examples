package de.fzi.sem.anno.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.fzi.sem.anno.controller.OpenAnnoController;
import de.fzi.sem.anno.controller.OpenAnnoSettings;
import de.fzi.sem.anno.model.OADM;
import de.fzi.sem.anno.model.PotentialMatch;
import de.fzi.sem.anno.model.singleton.FilePath;
import de.fzi.sem.anno.model.singleton.NameSpaces;

public class CreateListener implements ActionListener {
	
	private OpenAnnoController controller;
	private OpenAnnoSettings settings;

	public CreateListener(OpenAnnoController controller) {
		this.controller = controller;
		this.settings = controller.getSettings();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		OADM anno = controller.getModel().getAnno();
		anno.setBody();
		try {		
			List<PotentialMatch> results = controller.getModel().getResults();			
			NameSpaces ns = NameSpaces.getInstance();
			String filePath = FilePath.getInstance().get("out");
			
			// add selected results to OADM
			int truePositive = 0;
			int trueNegative = 0;
			int falsePositive = 0;
			int falseNegative = 0;
			for(PotentialMatch body: results){				
				if(body.isSelected()) {
					anno.addBody(body.getIri());
					anno.addKey(body.getIri().getNameSpaceKey());
					if(body.isSuggested()) truePositive++; else falseNegative++;
				} else {
					if(body.isSuggested()) falsePositive++; else trueNegative++;
				}
			}
			
			// calculate confusion matrix
			int precision = 0;
			int recall = 0;
			int Fmeasure = 0;
			try{
				precision = truePositive * 100 / (truePositive + falsePositive);
				recall = truePositive * 100 / (truePositive + falseNegative);
				Fmeasure = (2*precision*recall) / (precision+recall);
			} catch (Exception e) {
				System.out.println("Could not calculate confusion matrix: " + e.getMessage());
			}
			
			// adopt classifier
			double tolerance = settings.getTolerance();
			int minMatches = settings.getMinMatches();
			if(Fmeasure < settings.getDesiredFmeasure()) {
				if (recall > (precision + tolerance)) {
					minMatches = settings.incMinMatches();
				} else if (precision > (recall + tolerance)) {
					minMatches = settings.decMinMatches();
				}
			}
			
			// prepare confusion matrix
			StringBuilder cMatrix = new StringBuilder();
			cMatrix.append("<table><tr><td>True Positive:</td><td>");
			cMatrix.append(truePositive);
			cMatrix.append("</td><td>False Negative:</td><td>");
			cMatrix.append(falseNegative);
			cMatrix.append("</td></tr><tr><td>False Positive:</td><td>");
			cMatrix.append(falsePositive);
			cMatrix.append("</td><td>True Negative:</td><td>");
			cMatrix.append(trueNegative);			
			cMatrix.append("</td></tr></table><br>Precision: ");
			cMatrix.append(precision);
			cMatrix.append("% Recall: ");
			cMatrix.append(recall);
			cMatrix.append("% F-measure: ");
			cMatrix.append(Fmeasure);
			cMatrix.append("%\nMinimum matches set to: ");
			cMatrix.append(minMatches);
			cMatrix.append(" (at least ");
			cMatrix.append(settings.getMinPercent());
			cMatrix.append("%)");			
			
			// prepare output model
			Model outputModel = ModelFactory.createDefaultModel();
			
			// set prefixes
			List<String> keys = new ArrayList<String>();
			keys.addAll(anno.getKeys());
			keys.addAll(anno.getSerializedBy().getKeys());
			for(String k: keys) {
				outputModel.setNsPrefix(k, ns.get(k));
			}
			
			// include statements
			outputModel.add(anno.toModel());
			outputModel.add(anno.getAnnotatedBy().toModel());
			outputModel.add(anno.getSerializedBy().toModel());
//			outputModel.add(new RDF(path.get("OA")).getModel());
			
			// write file
			FileOutputStream outRDF = new FileOutputStream(filePath + "rdf");
			FileOutputStream outTTL = new FileOutputStream(filePath + "ttl");							
			outputModel.write(outRDF, "RDF/XML");
			outRDF.close();
			outputModel.write(outTTL, "Turtle");
			outTTL.close();
			
			// show info-box
			StringBuilder successMessage = new StringBuilder();
			successMessage.append("The following Annotation has been successfully created:\n\n");
			successMessage.append("<html>");
			successMessage.append(anno.getHtml());			
			successMessage.append("<br><br>");
			successMessage.append("Confusion Matrix:<br>");
			successMessage.append(cMatrix);
			JOptionPane.showMessageDialog(null, successMessage.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
//			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Creaton failed: " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}

}
