package com.guseggert.sensorloggerfeatureextractor.data;

import java.util.Observable;

import com.guseggert.sensorloggerfeatureextractor.DataInstance;
import com.guseggert.sensorloggerfeatureextractor.DataReader;



//Each time window observes TimeWindowMaker for new data points.
//When a time window is full, it calls onTimeWindowFinished(), which
//removes the time window as an observer and then processes the data.
public class TimeWindowMaker extends Observable {
	public final static int MSG_FEATURE_EXTRACTION_COMPLETE = 0; // message code for thread communication
	public final static int MSG_FEATURE_EXTRACTION_FAILURE = 1;

	private final long TIMEWINDOWLENGTH = 5000000000l; // nanoseconds
	private final float TIMEWINDOWOVERLAP = 0.5f; // overlap of time windows
	private TimeWindow mLastTimeWindow = null;
	private final String FILENAME = "1366122332244.csv";
	
	public TimeWindowMaker() {
		
	}
	
	public void run() {
		DataReader dr = new DataReader(FILENAME);
		DataInstance instance;
		while (dr.hasNext())
			addPoint(dr.nextLine());
	}
	
	private void addPoint(DataInstance instance) {
		if (mLastTimeWindow == null || boundaryReached(instance.Time))
			newTimeWindow(instance.Time);
		
		
			
		
	}
	
	private boolean boundaryReached(long time) {
		long interval = time - mLastTimeWindow.getStartTime();
		return interval >= TIMEWINDOWLENGTH * TIMEWINDOWOVERLAP;
	}
	
	private void newTimeWindow(long time) {
		TimeWindow tw = new TimeWindow(time, TIMEWINDOWLENGTH);
		addObserver(tw);
		mLastTimeWindow = tw;
	}

}