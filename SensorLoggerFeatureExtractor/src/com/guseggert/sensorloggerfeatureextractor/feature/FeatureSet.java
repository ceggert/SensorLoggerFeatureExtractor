package com.guseggert.sensorloggerfeatureextractor.feature;

import java.util.HashMap;
import java.util.Map;

import com.guseggert.sensorloggerfeatureextractor.SensorID;
import com.guseggert.sensorloggerfeatureextractor.data.TimeSeries;
import com.guseggert.sensorloggerfeatureextractor.data.TimeWindow;
import com.guseggert.sensorloggerfeatureextractor.data.TimeWindowMaker;

public class FeatureSet extends HashMap<SensorID, HashMap<FeatureID, Float>> implements Runnable {
	private static final long serialVersionUID = 2504830754416754646L;
	private TimeWindow mTimeWindow;

	// calculates feature set from a time window
	public FeatureSet(TimeWindow timeWindow) {
		mTimeWindow = timeWindow;
		computeFeatures();
	}

	private void computeFeatures() {
		// for each time series, compute the features
		for (Map.Entry<SensorID, TimeSeries> entry : mTimeWindow.entrySet()) {
			SensorID sensorID = entry.getKey();
			TimeSeries timeSeries = entry.getValue();
			this.put(sensorID, new HashMap<FeatureID, Float>());

			computeStatisticalFeatures(timeSeries, sensorID);
			sendMessage();
		}
	}

	private void sendMessage() {
//		Message msg = Message.obtain(mHandler, TimeWindowMaker.MSG_FEATURE_EXTRACTION_COMPLETE, this);
//		msg.sendToTarget();
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

	private void computeStructuralFeatures() {
		//StructuralFeatureExtractor sfe = new StructuralFeatureExtractor(timeSeries);
	}

	public TimeWindow getTimeWindow() {
		return mTimeWindow;
	}

	@Override
	public void run() {
		computeFeatures();
	}
}
