package com.guseggert.sensorloggerfeatureextractor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.common.base.Joiner;
import com.guseggert.sensorloggerfeatureextractor.feature.FeatureID;
import com.guseggert.sensorloggerfeatureextractor.feature.FeatureSet;

public class DataWriter {
	private boolean mFirstLine = true;
	ArrayList<FeatureID> mFeatureIDs = new ArrayList<FeatureID>();
	ArrayList<SensorID> mSensorIDs = new ArrayList<SensorID>();
	String mFileName;
	
	public DataWriter(String fileName) {
			mFileName = fileName;
	}
	
	private void writeLine(String string) {
		try { 
			BufferedWriter mWriter = new BufferedWriter(new FileWriter(mFileName, true));
			if (!mFirstLine) mWriter.newLine();
			mWriter.write(string);
			mWriter.close();
		} 
		catch (Exception e) { e.printStackTrace(); }
	}
	
	// builds the string for the first line and writes it
	private void writeFirstLine() {
		ArrayList<String> line = new ArrayList<String>();
		for (SensorID sensorID : mSensorIDs) {
			for (FeatureID featureID : mFeatureIDs) {
				line.add(sensorID.toString() + "_" + featureID.toString());
			}
		}
		line.add("CLASS");
		writeLine(Joiner.on(",").join(line));
	}
	
	// prepares the list of feature IDs and sensor IDs, which are
	// used to iterate through new feature sets in the same order
	private void prepareLists(FeatureSet featureSet) {
		SensorID anySensorID = null;
		for (SensorID sensorID : featureSet.keySet()) {
			mSensorIDs.add(sensorID);
			if (anySensorID == null) anySensorID = sensorID;
		}
		for (FeatureID featureID : featureSet.get(anySensorID).keySet()) {
			mFeatureIDs.add(featureID);
		}
	}
	
	// writes a feature set to the file
	public void writeLine(FeatureSet featureSet) {
		if (mFirstLine) {
			prepareLists(featureSet);
			writeFirstLine();
			mFirstLine = false;
		}
		
		ArrayList<String> line = new ArrayList<String>();
		for (SensorID sensorID : mSensorIDs) {
			for (FeatureID featureID : mFeatureIDs) {
				line.add(Float.toString(featureSet.get(sensorID).get(featureID)));
			}
		}
		line.add(featureSet.getActivity());
		writeLine(Joiner.on(",").join(line));
	}
}
