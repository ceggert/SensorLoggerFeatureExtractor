/**
 * MultiattributeFeatureSet.java
 * 
 * This class is a set of features indexed by an alphanumeric id. It is an alias
 * for Hashtable <String, Double>, and is intended to perform calculations
 * involving multiple parameters (such as comparisons or correlations).
 * 
 * Author: Aaron S. Young
 * */
package com.guseggert.sensorloggerfeatureextractor.feature;

import java.util.ArrayList;

import com.guseggert.sensorloggerfeatureextractor.data.TimeSeries;
import com.guseggert.sensorloggerfeatureextractor.data.TimeWindow;


public abstract class MultiattributeFeatureExtractor {
	
	/** The multiple TimeSeries from which the features will be extracted*/
	protected ArrayList <TimeSeries> seriesSet;
	
	/** The set of extracted features*/
	protected FeatureSet featureSet;
	
	/** The labels for the extracted features. TODO: It might or might not be necessary*/
	protected String[] featureLabels;
	
	public MultiattributeFeatureExtractor (ArrayList <TimeSeries> seriesSet, TimeWindow timeWindow) {
		if (seriesSet.size() <= 1) {
			System.out.println("Warning:  ArrayList<TimeSeries> is too small in " +
					"MultiattributeFeatureExtractor(ArrayList<String>, String)!");
		}
		this.seriesSet = seriesSet;
		featureSet = new FeatureSet(timeWindow);
	}
	
	public final FeatureSet getFeatureSet() {
		return featureSet;
	}

	/** Every single class that extends from this one must implement a method to compute
	 * features*/
	public abstract FeatureSet computeFeatures();
	
}