package de.fzi.sem.anno.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fzi.sem.anno.controller.listener.ComboListener;
import de.fzi.sem.anno.controller.listener.CreateListener;
import de.fzi.sem.anno.controller.listener.SearchListener;
import de.fzi.sem.anno.controller.listener.ShowResultsListener;
import de.fzi.sem.anno.controller.listener.TabListener;
import de.fzi.sem.anno.controller.listener.TableListener;
import de.fzi.sem.anno.model.Button;
import de.fzi.sem.anno.model.Motivation;
import de.fzi.sem.anno.model.OpenAnnoModel;
import de.fzi.sem.anno.model.PotentialMatch;
import de.fzi.sem.anno.model.Select;
import de.fzi.sem.anno.model.singleton.Mapping;
import de.fzi.sem.anno.view.OpenAnnoView;
import de.fzi.sem.interfaces.IController;

public class OpenAnnoController implements IController {
	
	// Attributes
	private OpenAnnoModel model;
	private OpenAnnoView view;	
	private OpenAnnoSettings settings;
	private boolean searched = false;	
	private Boolean[] propertiesSearched;
	private Boolean[] propertiesMatch;
	private Mapping mapping;
	List<String> ignoredLocalNames;

	// Constructor
	public OpenAnnoController(OpenAnnoModel model, OpenAnnoView view) {
		super();
		this.model = model;
		this.view = view;
		this.settings = new OpenAnnoSettings(model);
		this.ignoredLocalNames = new ArrayList<String>();
		ignoredLocalNames.add("class");
		ignoredLocalNames.add("resource");
		ignoredLocalNames.add("thing");
		mapping = Mapping.getInstance();
		mapping.setController(this);
	}		
	
	// Methods
	@Override
	public void initView() {
		String inputFileId = settings.getInputFileId();
		model.setup(inputFileId);
		
		// input tab	(--> View)	
		view.addInputSelect(new Select("Select agent for annotation:", model.getAgents().toArray(), model.getAgents().get(0), new ComboListener(this, "agent")));
		view.addInputSelect(new Select("Select target for annotation:", model.getTargets().toArray(), model.getTargets().get(0), new ComboListener(this, "target")));
		view.addInputSelect(new Select("Select motivation for annotation:", Motivation.getPossibleTypes(), model.getAnno().getMotivation(), new ComboListener(this, "motivation")));
		view.addInputSelect(new Select ("Select service:", model.getServices().toArray(), model.getServices().get(1), new ComboListener(this, "service")));
		view.setInputButton(new Button("Search accordances", new SearchListener(this)));
		
		// search tab	(--> View)	
		view.setSearchLabel("Properties of " + model.getAnno().getTarget());
		view.setSearchColumnNames(new String[]{ "Nr.", "Predicate", "Object", "Accordances" });
		view.setSearchButton(new Button("Show results", new ShowResultsListener(this)));
		
		// result tab	(--> View)	
		view.setResultsLabel("Select resources to annotate with " + model.getAnno().getTarget());
		view.setResultsColumnNames(new String[]{ "Title", "Name Space", "Local Name", "Accordances", "Select" });
		view.setResultsButton(new Button("Create Annotation", new CreateListener(this)));
		view.setResultTableListener(new TableListener(this));
		
		// show view
		view.setTabListener(new TabListener(this));
		try {
			view.build();
		} catch (Exception e) {
			System.out.println("Could not initialize view. Reason: " + e.getMessage());
			e.printStackTrace();
		}	
	}
	
	public void updateResults() {
		// prepare result table			
		List<PotentialMatch> results = model.getResults();
		synchronized(results) {
			Collections.sort(results);	
			Object[][] tableData = new Object[results.size()][5];
			int i = 0;
			for(PotentialMatch match: results){
//				String[] accordances = match.getAccordances().toArray(new String[0]);
				List<String> accordances = new ArrayList<String>();
				Integer matches = match.numberOfPropertyMatches();
				Integer total = propertiesSearched.length;
				StringBuilder sb = new StringBuilder();
				sb.append(matches);
				sb.append(" of ");
				sb.append(total);	
				sb.append(" (");
				sb.append(matches*100/total);
				sb.append("%)");			
				accordances.add(sb.toString());
				accordances.addAll(match.getAccordances());
				match.setSuggested(getSuggestion(match));
				match.setSelected(match.isSuggested());			
				String title = new Integer(i).toString();
				if(match.hasTitle()) {
					title = match.getTitle();
				}			
				Object[] row =	{title, match.getNameSpace(), match.getLocalName(), accordances, match.isSelected() };			
				tableData[i] = row;
				i++;
			}
			String tableLabel = "Select resources to annotate with " + model.getAnno().getTarget();
			view.setResultsLabel(tableLabel); 
			view.setResultsTableData(tableData);
		}
	}
	
	private boolean getSuggestion(PotentialMatch match) {
		int matches = match.getAccordances().size();
		int total = propertiesSearched.length;
		
		if(matches < settings.getMinMatches()) {
			return false;
		} else if((matches*100/total) < settings.getMinPercent()) {
			return false;
		} else {
			return true;
		}
	}
	
	public void searchLabels() {
		// update titles for result table
		int count = 0;
		int labelSearchLimit = settings.getLabelSearchLimit();
		for(PotentialMatch match: model.getResults()) {			
			if(!match.hasTitle()) {
				// get title
				Thread labelSearch = new Thread( new LabelSearch(this, match, count));
				labelSearch.start();	 
			}
			count++;
			if(count >= labelSearchLimit && labelSearchLimit != 0) {
				break;
			}
		}
	}
	
	public void resetResults() {
		model.clearResults();
		updateResults();
		Object[][] searchData = new Object[0][5];
		String searchLabel = "Properties of " + model.getAnno().getTarget();
		view.setSearchLabel(searchLabel); 
		view.setSearchTableData(searchData);
		view.showSearch();		
	}

	public void setTotalSize(Integer size) {
//		view.addInfo(size);
		propertiesSearched = new Boolean[size];
		propertiesMatch = new Boolean[size];
		for(int i = 0; i < size; i++) {
			propertiesSearched[i] = false;
			propertiesMatch[i] = false;
		}				
	}
	
	public void setPropertyMatch(int row, boolean bool) {
		propertiesMatch[row] = bool;	
	}

	public void setPropertySearched(int row, boolean bool) {
		propertiesSearched[row] = bool;	
		if(searchComplete()) {
			System.out.println("Search completed! Results: " + model.getResults().size());
			if(settings.isQueryLabels()) {
				searchLabels();
			}			
		}
	}
	
	public boolean searchComplete() {
		for(int i = 0; i < propertiesSearched.length; i++) {
			if(!propertiesSearched[i]) {
				return false;
			}
		}
		return true;
	}
	
	// Getter & Setter
	@Override
	public OpenAnnoModel getModel() {
		return model;
	}

	@Override
	public OpenAnnoView getView() {
		return view;
	}

	public boolean isSearched() {
		return searched;
	}
	
	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public OpenAnnoSettings getSettings() {
		return settings;
	}

	public List<String> getIgnoredLocalNames() {
		return this.ignoredLocalNames;
	}

	public Integer getNumberOfMatches(int row) {
		Integer count = 0;
		for (boolean pMatch: propertiesMatch) {
			if(pMatch == true) {
				count++;
			}
		}
		return count;
	}

	public Integer getNumberOfProperties(int row) {
		return propertiesSearched.length;
	}
} // class OpenAnnoController
