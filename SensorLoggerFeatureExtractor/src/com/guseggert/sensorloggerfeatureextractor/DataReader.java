package com.guseggert.sensorloggerfeatureextractor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class DataReader {
	Scanner mScanner;
	ArrayList<SensorID> mSensorIDs = new ArrayList<SensorID>();
	
	public DataReader (String fileName) {
		try { mScanner = new Scanner(new File(fileName)); } 
		catch (Exception e) { e.printStackTrace(); }
		readHeader();
	}
	
	private void readHeader() {
		parseHeader(Lists.newArrayList(Splitter.on(",").trimResults().split(mScanner.nextLine())));
	}
	
	private void parseHeader(ArrayList<String> headerString) {
		for (String str : headerString) {
			SensorID parsed = SensorID.parse(str);
			if (parsed != null) mSensorIDs.add(parsed);
		}
	}
	
	public ArrayList<SensorID> getSensorIDs() {
		return mSensorIDs;
	}
	
	public DataInstance nextLine() {
		ArrayList<String> dataList = Lists.newArrayList(Splitter.on(",").trimResults().split(mScanner.nextLine()));
		
		String cls;
		long time;
		HashMap<SensorID, Float> values = new HashMap<SensorID, Float>();
		
		// build the data values
		int i = 0;
		for ( ; i < mSensorIDs.size(); i++)
			values.put(mSensorIDs.get(i), Float.parseFloat(dataList.get(i)));
		time = Long.parseLong(dataList.get(i++));
		cls = dataList.get(i);
		
		return new DataInstance(values, cls, time);
	}
	
	public boolean hasNext() {
		return mScanner.hasNext();
	}
}
