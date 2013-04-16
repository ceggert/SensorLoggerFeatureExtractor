package com.guseggert.sensorloggerfeatureextractor;

import java.util.HashMap;

// represents a row in the dataset
public class DataInstance {
	public HashMap<SensorID, Float> Values;
	public String Class;
	public long Time;
	
	public DataInstance(HashMap<SensorID, Float> values, String cls, long time) {
		Values = values;
		Class = cls;
		Time = time;
	}
}
