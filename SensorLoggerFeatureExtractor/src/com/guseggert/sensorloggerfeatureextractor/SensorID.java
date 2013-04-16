package com.guseggert.sensorloggerfeatureextractor;


public enum SensorID {
	ACC_X, ACC_Y, ACC_Z, 
	GYRO_X, GYRO_Y, GYRO_Z, 
	LINACC_X, LINACC_Y, LINACC_Z,
	GRAV_X, GRAV_Y, GRAV_Z, 
	ROTVEC_X, ROTVEC_Y, ROTVEC_Z;
	
	public static SensorID parse(String string) {
		for (SensorID sensorID : SensorID.values()) {
			if (sensorID.toString() == string)
				return sensorID;
		}
		return null;
	}
}
