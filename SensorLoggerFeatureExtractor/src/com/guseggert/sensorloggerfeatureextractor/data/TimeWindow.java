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
	private TimeWindowMaker mTimeWindowMaker;
	
	// tracks how often we see each activity, since we use the most common activity
	// as the time window's activity:
	private HashMap<String, Integer> mActivityCount = new HashMap<String, Integer>();

	// need to have an instance to make a new time window
	public TimeWindow(DataInstance instance, long length, TimeWindowMaker twm) {
		mLength = length;
		mStartTime = instance.Time;
		mTimeWindowMaker = twm;
		initTable(instance);
		addInstance(instance);
	}

	public long getStartTime() {
		return mStartTime;
	}
	
	// initialize the hash table based on an instance
	private void initTable(DataInstance instance) {
		for (SensorID sensorID : instance.Values.keySet()) {
			this.put(sensorID, new TimeSeries());
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		addInstance((DataInstance)arg1);
	}
	
	private void addInstance(DataInstance instance) {
		// check if the time window is full
		if (willFit(instance.Time)) { 
			
			// for each sensor, add a point to the respective time series
			for (Map.Entry<SensorID, Float> entry : instance.Values.entrySet()) {
				SensorID sensorID = entry.getKey();
				float value = entry.getValue();
				
				DataPoint dp = new DataPoint(value, sensorID, instance.Time);
				this.get(sensorID).add(dp);
			}
			
			udpateActivityCount(instance.Activity);
			updateActivity();
		} else {
//			System.out.println("tw full: " + mStartTime);
			mTimeWindowMaker.onTimeWindowFull(this);
		}
	}
	
	private void udpateActivityCount(String str) {
		if (mActivityCount.containsKey(str)) 
			mActivityCount.put(str, mActivityCount.get(str) + 1);
		else
			mActivityCount.put(str, 1);
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
	
	// given the time of a new instance, checks if it will fit in this time window
	private boolean willFit(long time) {
		long curLength = time - mStartTime;
		return curLength <= mLength;
	}
	
	public String getActivity() {
		return mActivity;
	}
}
