package de.fzi.sem.anno.controller;

import de.fzi.sem.anno.model.OpenAnnoModel;

public class OpenAnnoSettings {
	
	private OpenAnnoModel model = null;
	private int minMatches;
	private int minPercent;
	private int adoptPercent;

	public OpenAnnoSettings(OpenAnnoModel model) {
		this.model = model;
		this.minMatches = model.getIntSetting("minMatches") ;
		this.minPercent = model.getIntSetting("minPercent");
		this.adoptPercent = model.getIntSetting("adoptPercent");
	}

	public String getInputFileId() {
		return model.getSetting("inputFileId").toString();
	}
	
	public int getMinMatches() {
		return minMatches;
	}
	
	public int incMinMatches() {
		minMatches++;
		if (minPercent <= (100 - adoptPercent)) {
			minPercent += adoptPercent;
		}
		return minMatches;
	}
	
	public int decMinMatches() {
		if (minMatches > 1) {
			minMatches--;
		}
		if (minPercent > adoptPercent) {
			minPercent -= adoptPercent;
		}
		return minMatches;
	}

	public int getMinPercent() {
		return minPercent;
	}

	public int getDesiredFmeasure() {
		return model.getIntSetting("desiredFmeasure");
	}

	public int getTolerance() {
		return model.getIntSetting("tolerance");
	}

	public boolean isPredicateMapping() {
		return model.getBoolSetting("predicateMapping");
	}

	public boolean isCoReference() {
		return model.getBoolSetting("coReference");
	}

	public boolean isLitealObjects() {
		return model.getBoolSetting("litealObjects");
	}
	
	public boolean isQueryLabels() {
		return model.getBoolSetting("queryLabels");
	}

	public long getTimeout() {
		return  model.getLongSetting("timeout");
	}

	public int getMaxDistance() {
		return  model.getIntSetting("maxDistance");
	}

	public int getLabelSearchLimit() {
		return model.getIntSetting("labelSearchLimit");
	}

}
