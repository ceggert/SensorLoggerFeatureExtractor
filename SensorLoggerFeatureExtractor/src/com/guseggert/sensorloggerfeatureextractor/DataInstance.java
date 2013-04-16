package com.guseggert.sensorloggerfeatureextractor;

import java.util.HashMap;

// represents a row in the dataset
public class DataInstance {
	public HashMap<SensorID, Float> Values;
	public String Activity;
	public long Time;
	
	public DataInstance(HashMap<SensorID, Float> values, String activity, long time) {
		Values = values;
		Activity = activity;
		Time = time;
	}
}
