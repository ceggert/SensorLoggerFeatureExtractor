//package com.guseggert.sensorloggerfeatureextractor.feature;
//
//import java.awt.Point;
//import java.util.ArrayList;
//
//import com.guseggert.sensorloggerfeatureextractor.data.TimeSeries;
//
///**
// * This class extracts the correlation between two given time series.
// * 
// * */
//public class CorrelationFeatureExtractor extends MultiattributeFeatureExtractor {	
//	private final String CORR_TAG = "Correlation";
//	
//	public CorrelationFeatureExtractor(ArrayList <TimeSeries> seriesSet) {
//		super(seriesSet);
//	}
//	
//	public FeatureSet computeFeatures() {
//		for (int i = 0; i < seriesSet.size()-1; i++) {
//			for (int j = i+1; j < seriesSet.size(); j++) {
//				String id = CORR_TAG + "_" + seriesSet.get(i).getId() + "_" + seriesSet.get(j).getId();
//				featureSet.put(id, computeCorrelation(seriesSet.get(i), seriesSet.get(j)));
//			}
//		}
//		return featureSet;
//	}
//	
//	public void recomputeFeatures(ArrayList <TimeSeries> seriesSet) {
//		this.seriesSet = seriesSet;
//		computeFeatures();
//	}
//	/** The correlation between two variables X on x-axis and Y on y-axis,
//	  *   with standard deviations rX and rY is defined as:
//	  *     correlation(X, Y) = cov(X, Y) / (rX*rY),
//	  *   where "cov" stands for covariance. *
//	  *   
//	  *   @param seriesA
//	  *   @param seriesB
//	  */
//	public static double computeCorrelation(TimeSeries seriesA, TimeSeries seriesB){
//		int size = seriesA.size();
//		if (size != seriesB.size()) {
//			throw new RuntimeException("TimeSeries Correlation size mismatch!");
//		}
//		double meanA = 0.0, meanB = 0.0, meanAB = 0.0;
//		for (int i = 0; i < size; i++) {
//			meanA += seriesA.get(i).getValue();
//			meanB += seriesB.get(i).getValue();
//			meanAB += seriesA.get(i).getValue() * seriesB.get(i).getValue();
//		}
//		meanA = meanA/size;
//		double varA = computeVariance(seriesA, meanA);
//		meanB = meanB/size;
//		double varB = computeVariance(seriesB, meanB);
//		meanAB = meanAB/size;
//		double corr = (meanAB-meanA*meanB)/(varA*varB);
//		if (corr == Double.POSITIVE_INFINITY) {
//			return 1.0;
//		}
//		else if (corr == Double.NEGATIVE_INFINITY) {
//			return -1.0;
//		}
//		else if (Double.isNaN(corr)) {
//			return 0.0;
//		}
//		return corr;
//	}
//	
//	/** Two-Pass Variance 
//	 * 
//	 * @param series
//	 * @param mean 
//	 */
//	private static double computeVariance(TimeSeries series, double mean) {
//		double sumVar = 0.0;
//		for (Point p : series) {
//			sumVar += Math.pow(p.getValue()-mean, 2);
//		}
//		return sumVar/(series.size() - 1);
//	}	
//}