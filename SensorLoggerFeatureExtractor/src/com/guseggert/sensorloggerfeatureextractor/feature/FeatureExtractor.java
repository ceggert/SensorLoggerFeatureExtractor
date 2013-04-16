package com.guseggert.sensorloggerfeatureextractor.feature;

import com.guseggert.sensorloggerfeatureextractor.data.TimeSeries;

public abstract class FeatureExtractor {
	protected TimeSeries mTimeSeries;
	protected FeatureSet mFeatureSet;

	public FeatureExtractor(TimeSeries timeSeries) {
		mTimeSeries = timeSeries;
	}
}
