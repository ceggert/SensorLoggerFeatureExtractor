/**
 * This class extracts transient features from a given time series. The features are
 * the slope and y-intersect of the linear regression.
 * 
 * */

package com.guseggert.sensorloggerfeatureextractor.feature;

import com.guseggert.sensorloggerfeatureextractor.data.DataPoint;
import com.guseggert.sensorloggerfeatureextractor.data.TimeSeries;


public class TransientFeatureExtractor extends FeatureExtractor{
	private final double TREND_THRESHOLD = 2-Math.sqrt(3); 
	private final double MOC_SERIES_PERCENTAGE = 0.2; 
	
	
	public TransientFeatureExtractor(TimeSeries series) {
		super(series);
	}

//	public FeatureSet computeFeatures() {
//		mFeatureSet.put(FeatureID.TREND, value)
////		featureSet.put(Parameters.TREND_ID + "_" + series.getId(), computeTrend(linearRegression()[0]) );
////		featureSet.put(Parameters.MAG_ID + "_" + series.getId(), computeMagnitude() );
//		return mFeatureSet;
//	}
	
	/** This method performs a linear regression on a TimeSeries,
	 *     returning the slope as reg[0] and y-intersect as reg[1].*/
	private double[] linearRegression() {

		double[] reg = new double[2];
		
        int n = 0;
        double[] x = new double[mTimeSeries.size()];
        double[] y = new double[mTimeSeries.size()];

        // first pass: read in data, compute xbar and ybar
        double sumx = 0.0, sumy = 0.0;//, sumx2 = 0.0;
        for (DataPoint p : mTimeSeries) {
            x[n] = p.getTime();
            y[n] = p.getValue();
            sumx  += x[n];
            //sumx2 += x[n] * x[n];
            sumy  += y[n];
            n++;
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, /*yybar = 0.0,*/ xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            //yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        reg[0] = xybar / xxbar;
        reg[1] = ybar - reg[0] * xbar;
		
		return reg;
	}
	
	/** This function takes the slope of linear regression and
	 *   determines whether the associated set of points are increasing,
	 *   decreasing, or remaining constant. */
	private double computeTrend (double slope) {
		if (slope >= TREND_THRESHOLD) {
			return 1.0;
		}
		else if (slope <= -TREND_THRESHOLD) {
			return -1.0;
		}
		return 0;
	}
	
	public double computeTrend() {
		return computeTrend(linearRegression()[0]);
	}
	
	/** Let S be a given time series defined from tmin to tmax.
	 *    Let Sp- be a subset of S which contains all measurements
	 *    between tmin and tmin+(tmax-tmin)p, where 0 < p < 1 is
	 *    a percentage of the series. Let Sp+ be a subset of S
	 *    which contains all samples between tmin+(tmax-tmin)*(1-p)
	 *    and tmax. Then, the magnitude of change k(S; p) is defined as:
     *      k(S; p) = max{ |max{Sp+} - min{Sp-}|, |max{Sp-} - min{Sp+}| }*/
	public double computeMagnitude () {
		
		if (mTimeSeries.size() == 1) {
			return 0.0;
		}
		
		double start = mTimeSeries.get(0).getTime();
		double end = mTimeSeries.get(mTimeSeries.size()-1).getTime();
		
		double spMinus = start+(end-start)*MOC_SERIES_PERCENTAGE;
		double spPlus = start+(end-start)*(1.0-MOC_SERIES_PERCENTAGE);
		
		double maxPlus = mTimeSeries.get(0).getValue();
		double minPlus = maxPlus;
		double maxMinus = mTimeSeries.get(mTimeSeries.size()-1).getValue();
		double minMinus = maxMinus;
		
		for (int i = 0; mTimeSeries.get(i).getTime() <= spMinus; i++) {
			double value = mTimeSeries.get(i).getValue();
			maxMinus = Math.max(maxMinus, value);
			minMinus = Math.min(minMinus, value);
		}
		for (int i = mTimeSeries.size()-1; mTimeSeries.get(i).getTime() >= spPlus; i--) {
			double value = mTimeSeries.get(i).getValue();
			maxPlus = Math.max(maxPlus, value);
			minPlus = Math.min(minPlus, value);
		}
		return Math.max(Math.abs(maxPlus - minMinus), Math.abs(maxMinus - minPlus));
	}

}
