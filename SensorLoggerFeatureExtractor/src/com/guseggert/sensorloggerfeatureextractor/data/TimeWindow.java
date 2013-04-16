package com.guseggert.sensorloggerfeatureextractor.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.guseggert.sensorloggerfeatureextractor.DataInstance;
import com.guseggert.sensorloggerfeatureextractor.SensorID;
import com.guseggert.sensorloggerfeatureextractor.feature.FeatureSet;

public class TimeWindow extends HashMap<SensorID, TimeSeries> implements Observer {
	private static final long serialVersionUID = 4834473217118341864L;
	private long mStartTime; // nanoseconds
	private long mLength;
	FeatureSet mFeatureSet;
	private String mActivity;
	
	// tracks how often we see each activity, since we use the most common activity
	// as the time window's activity:
	private HashMap<String, Integer> mActivityCount = new HashMap<String, Integer>();

	public TimeWindow(DataInstance instance, long length) {
		mLength = length;
		mStartTime = instance.Time;
		for (SensorID sensorID : instance.Values.keySet()) {
			// add each value to a new time series
		}
	}

	public long getStartTime() {
		return mStartTime;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		addInstance((DataInstance)arg1);
	}
	
	private void addInstance(DataInstance instance) {
		// for each sensor, add the point
		for (SensorID sensorID : instance.Values.keySet()) {
			DataPoint dp = new DataPoint(instance.Values.get(sensorID), 
			this.get(sensorID).add(new DataPoint(instance.Values.get))
		}
		udpateActivityCount(instance.Activity);
		updateActivity();
	}
	
	private void udpateActivityCount(String str) {
		mActivityCount.put(str, mActivityCount.get(str) + 1);
	}
	
	// this is not very efficient, because we count the max every time we add a new instance,
	// but we don't expect to have too many activities so it will suffice:
	private void updateActivity() {
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : mActivityCount.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue())
				maxEntry = entry;
		}
		mActivity = maxEntry.getKey();
	}
}
