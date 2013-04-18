package com.guseggert.sensorloggerfeatureextractor.feature;

import java.util.HashMap;
import java.util.Map;

import com.guseggert.sensorloggerfeatureextractor.SensorID;
import com.guseggert.sensorloggerfeatureextractor.data.TimeSeries;
import com.guseggert.sensorloggerfeatureextractor.data.TimeWindow;

public class FeatureSet extends HashMap<SensorID, HashMap<FeatureID, Float>>{
	private static final long serialVersionUID = 2504830754416754646L;
	private TimeWindow mTimeWindow;

	// calculates feature set from a time window
	public FeatureSet(TimeWindow timeWindow) {
		mTimeWindow = timeWindow;
		computeFeatures();
		//logContents();
	}

	private void computeFeatures() {
		// for each time series, compute the features
		for (Map.Entry<SensorID, TimeSeries> entry : mTimeWindow.entrySet()) {
			SensorID sensorID = entry.getKey();
			TimeSeries timeSeries = entry.getValue();
			this.put(sensorID, new HashMap<FeatureID, Float>());

			computeStatisticalFeatures(timeSeries, sensorID);
			computeStructuralFeatures(timeSeries, sensorID);
			computeTransientFeatures(timeSeries, sensorID);
		}
	}

	private void computeStatisticalFeatures(TimeSeries timeSeries, SensorID sensorID) {
		StatisticalFeatureExtractor sfe = new StatisticalFeatureExtractor(timeSeries);
		this.get(sensorID).put(FeatureID.MEAN, (float)sfe.getMean());
		this.get(sensorID).put(FeatureID.STDDEV, (float)sfe.computeSTDV());
		this.get(sensorID).put(FeatureID.IQR, (float)sfe.computeIQR());
		this.get(sensorID).put(FeatureID.MAD, (float)sfe.computeMAD());
		this.get(sensorID).put(FeatureID.RMS, (float)sfe.computeRMS());
		this.get(sensorID).put(FeatureID.ENERGY, (float)sfe.computeEnergy());
	}

	private void computeStructuralFeatures(TimeSeries timeSeries, SensorID sensorID) {
		StructuralFeatureExtractor sfe = new StructuralFeatureExtractor(timeSeries);
		double[] coeffs2 = sfe.computeLeastSquares(2);
		double[] coeffs3 = sfe.computeLeastSquares(3);
		double[] coeffs4 = sfe.computeLeastSquares(4);
		this.get(sensorID).put(FeatureID.POLYFIT_1_0, (float)coeffs2[0]);
		this.get(sensorID).put(FeatureID.POLYFIT_1_1, (float)coeffs2[1]);
		this.get(sensorID).put(FeatureID.POLYFIT_2_0, (float)coeffs3[0]);
		this.get(sensorID).put(FeatureID.POLYFIT_2_1, (float)coeffs3[1]);
		this.get(sensorID).put(FeatureID.POLYFIT_2_2, (float)coeffs3[2]);
		this.get(sensorID).put(FeatureID.POLYFIT_3_0, (float)coeffs4[0]);
		this.get(sensorID).put(FeatureID.POLYFIT_3_1, (float)coeffs4[1]);
		this.get(sensorID).put(FeatureID.POLYFIT_3_2, (float)coeffs4[2]);
		this.get(sensorID).put(FeatureID.POLYFIT_3_3, (float)coeffs4[3]);
	}

	private void computeTransientFeatures(TimeSeries timeSeries, SensorID sensorID) {
		TransientFeatureExtractor tfe = new TransientFeatureExtractor(timeSeries);
		double mag = tfe.computeMagnitude();
		double trend = tfe.computeTrend();
		this.get(sensorID).put(FeatureID.MAG, (float)mag);
		this.get(sensorID).put(FeatureID.TREND, (float)trend);
		this.get(sensorID).put(FeatureID.SIGNED_MAG, (float)(trend*mag));
	}
	
	public TimeWindow getTimeWindow() {
		return mTimeWindow;
	}
	
	public String getActivity() {
		return mTimeWindow.getActivity();
	}
	
	public void logContents() {
		for(Map.Entry<SensorID, HashMap<FeatureID, Float>> entry : this.entrySet()) {
			SensorID sensorID = entry.getKey();
			HashMap<FeatureID, Float> hm = entry.getValue();
			for (Map.Entry<FeatureID, Float> entry1 : hm.entrySet()) {
				FeatureID featureID = entry1.getKey();
				float value = entry1.getValue();
				System.out.println(featureID + ": " + value + " Sensor: " + 
						sensorID + " Time: " + this.getTimeWindow().getStartTime());
			}
		}
	}
}
