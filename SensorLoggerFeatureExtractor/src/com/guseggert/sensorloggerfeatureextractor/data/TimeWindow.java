package com.guseggert.sensorloggerfeatureextractor.data;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.guseggert.sensorloggerfeatureextractor.SensorID;
import com.guseggert.sensorloggerfeatureextractor.feature.FeatureSet;

public class TimeWindow extends HashMap<SensorID, TimeSeries> implements Observer {
	private static final long serialVersionUID = 4834473217118341864L;
	private long mStartTime; // nanoseconds
	private long mLength;
	FeatureSet mFeatureSet;

	public TimeWindow(long time, long length) {
		mStartTime = time;
		mLength = length;
	}

	public void setStartTime(long t) {
		mStartTime = t;
	}

	public long getStartTime() {
		return mStartTime;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
	}

}
