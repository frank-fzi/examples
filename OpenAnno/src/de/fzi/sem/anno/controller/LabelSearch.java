package de.fzi.sem.anno.controller;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Literal;

import de.fzi.sem.anno.model.PotentialMatch;
import de.fzi.sem.anno.model.singleton.Services;
import de.fzi.sem.anno.util.QueryUtil;

public class LabelSearch implements Runnable {
	private OpenAnnoController controller;
	private PotentialMatch match;
	private int row;

	public LabelSearch(OpenAnnoController controller, PotentialMatch match, int row) {
		super();
		this.controller = controller;
		this.match = match;
		this.row = row;
	}

	@Override
	public void run() {
		
		// get title
		List<Literal> labels = new ArrayList<Literal>();

		Services service = Services.getInstance();
		QueryUtil queryUtil = new QueryUtil(service.get(controller.getModel().getService().getKey()));
		try {
			labels = queryUtil.getLabels(match.getIri());
		} catch (InterruptedException e) {
			System.out.println("Timeout reached when searching labels of " + match.getIri().getCurie());
		}		

		String title = null;
		if (labels.size() > 0) {
			title = labels.get(0).getString();
		} else {
			title = "no label (" + row + ")";
		}	
		match.setTitle(title);
		controller.getView().updateResultsTableData(row, 0, title);
	}
}
