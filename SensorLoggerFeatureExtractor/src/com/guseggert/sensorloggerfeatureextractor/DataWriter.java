//package com.guseggert.sensorloggerfeatureextractor;
//
//import java.io.BufferedWriter;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import com.google.common.base.Joiner;
//import com.guseggert.sensorloggerfeatureextractor.feature.FeatureID;
//import com.guseggert.sensorloggerfeatureextractor.feature.FeatureSet;
//
//public class DataWriter {
//	private BufferedWriter mWriter;
//	private boolean mFirstLine = true;
//	ArrayList<FeatureID> mFeatureIDs = new ArrayList<FeatureID>();
//	ArrayList<SensorID> mSensorIDs = new ArrayList<SensorID>();
//	
//	public DataWriter(String fileName) {
//		try { 
//			mWriter = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8);
//		} 
//		catch (Exception e) { e.printStackTrace(); }
//	}
//	
//	private void writeLine(String string) {
//		try { mWriter.write(string); } 
//		catch (Exception e) { e.printStackTrace(); }
//	}
//	
//	private void writeFirstLine() {
//		
//	}
//	
//	// prepares the list of feature IDs and sensor IDs, which are
//	// used to iterate through new feature sets in the same order
//	private void prepareLists(FeatureSet featureSet) {
//		for (SensorID sensorID : featureSet.keySet()) {
//			mSensorIDs.add(sensorID);
//			for (FeatureID featureID : featureSet.get(sensorID).keySet()) {
//				mFeatureIDs.add(featureID);
//			}
//		}
//	}
//	
//	// write a feature set to the file
//	public void writeLine(FeatureSet featureSet) {
//		if (mFirstLine) {
//			prepareLists(featureSet);
//			mFirstLine = false;
//		} else {
//			
//		}
//	}
//}
