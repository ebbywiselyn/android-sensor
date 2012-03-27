/*
 * 
 * Copyright (C) 2010 The Android Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */

package tweet.sensor.user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * Class for Signal handling of sensor event listener, location listener
 * 
 * @author Ebby Wiselyn	<ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 * @version 0.0.1
 * 
 */
public class UserInfoHandler implements SensorEventListener, LocationListener {

	private boolean internetConnectivity = false;
	private int mSampleNo = 0;
	private static int logCounter = 0;
	private static final int TOTAL_SAMPLES = 64;
	private static final double DURATION_OF_ONE_SAMPLE = 64.0;
	private double mLongitude, mLatitude;
	private long mStartTime = System.currentTimeMillis();
	private long mSamplingTime[] = new long[TOTAL_SAMPLES];
	private String recentActivity = "default";
	private ArrayList<Double> mAccList = new ArrayList<Double>();
	private TweetSensorUserService mSensorService;
	private Address mUserAddress;

	// FIXME check if static is required
	public static String userInfoStr;
	private static UserInfoHandler sHandler;

	/*
	 * Acceleration from x, y, z accelerometer values
	 * 
	 * @param x acceleration in x axis
	 * 
	 * @param y acceleration in y axis
	 * 
	 * @param z acceleration in z axis
	 * 
	 * @return double acceleration
	 */
	private double getAcceleration(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * 
	 * @return UserInformation in Gecoded format
	 */
	private String getUserInfoAddress() {
		StringBuilder userInfo = new StringBuilder();
		if (mUserAddress == null) {
			return "User Location Not Available";
		}

		int max = mUserAddress.getMaxAddressLineIndex();

		for (int i = 0; i < max; i++) {
			userInfo.append(mUserAddress.getAddressLine(i));
			userInfo.append(",");
		}

		userInfo.append("latitude: " + mLatitude);
		userInfo.append(",");
		userInfo.append(" longitutde: " + mLongitude);
		userInfo.append(",");

		return userInfo.toString();
	}

	/**
	 * 
	 * @return GPS location in String format
	 */
	private String getUserInfoGPSLocation() {
		StringBuilder userInfo = new StringBuilder();

		userInfo.append("latitude: " + mLatitude);
		userInfo.append(" ,longitude: " + mLongitude);

		return userInfo.toString();
	}

	/**
	 * Singleton class implementation, Can create only one object
	 * 
	 * @return UserInfoHandler returns instance of UserInfoHandler
	 */
	public static UserInfoHandler getHandler() {
		if (sHandler == null) {
			sHandler = new UserInfoHandler();
		}

		return sHandler;
	}

	/**
	 * To reset the handler to null
	 */
	public static void clearHandler() {
		if (sHandler != null) {
			sHandler = null;
		}
	}

	/**
	 * 
	 * @param state
	 */
	public void setDeviceConnectivity(boolean state) {
		this.internetConnectivity = false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getDeviceConnectivity() {
		return this.internetConnectivity;
	}

	/**
	 * Set the service instance which creates the handler, Required for calling
	 * service specific methods like sending broadcast
	 * 
	 * @param sensorService
	 *            service instance which creates the handler
	 * @return void
	 */
	public void setService(TweetSensorUserService sensorService) {
		this.mSensorService = sensorService;
	}

	/**
	 * Returns the context of the service. Since the Service is a Context. It
	 * returns the service instance that has been set on onBind()
	 * 
	 * @return Context
	 * @see android.app.Context
	 */
	public Context getService() {
		return this.mSensorService;
	}

	/*
	 * onAccuracyChanged Callback method for change in sensor accuracy
	 * 
	 * @param sensor Sensor
	 * 
	 * @param accuracy change in accuracy
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * onSensorChanged Callback method for change in sensor event
	 * 
	 * @param event SensorEvent which occurred
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		String userAddress, userInfoStr, userAct;
		double acc, x, y, z;
		Double linear_data[], smoothed_data[];

		Log.d(TweetSensorUserService.tag, TweetSensorUserService.tag
				+ "mSampleNo:" + mSampleNo);

		x = event.values[0];
		y = event.values[1];
		z = event.values[2];

		mSamplingTime[mSampleNo] = System.currentTimeMillis() - mStartTime;
		acc = getAcceleration(x, y, z);

		mAccList.add(acc);

		// Process every mTotalSamples interval
		if (mSampleNo >= TOTAL_SAMPLES - 1) {

			// Data Interpolation Linearization
			linear_data = UserInfoUtil.linearizeData(DURATION_OF_ONE_SAMPLE,
					TOTAL_SAMPLES, mSamplingTime, mAccList);

			// Smoothen the data
			smoothed_data = UserInfoUtil.smoothenData(linear_data);
			userAct = UserInfoActivityClassifier
					.kNNClassifyActivity(smoothed_data);
			userAct = "Activity: " + userAct;
			

			if (internetConnectivity) {
				userAddress = getUserInfoAddress();
			} else {
				userAddress = getUserInfoGPSLocation();
			}

			userInfoStr = userAct + "\n" + userAddress;
			Log.d(TweetSensorUserService.tag, TweetSensorUserService.tag
					+ "_Activity" + userInfoStr);

			// Broadcast the data
			if (mSensorService != null && recentActivity.equals(userAct)) {
				// TODO: Use appropriate intent action
				Intent i = new Intent();
				i.putExtra("userInfoStr", userInfoStr);
				i.setAction(Intent.ACTION_VIEW);
				mSensorService.sendBroadcast(i);
			}
			recentActivity = userAct;

			// Reset the start time
			mStartTime = System.currentTimeMillis();
			logCounter = logCounter + 1;
			mAccList.clear();
		}

		mSampleNo = mSampleNo + 1;
		mSampleNo = mSampleNo % TOTAL_SAMPLES;
	}

	private Double[] convertToArray(ArrayList<Double> mAccList, int totalSamples) {

		Double d[] = new Double[totalSamples];
		int i = 0;

		for (Double e : mAccList) {
			d[i] = e;
			i++;
		}

		return d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onLocationChanged(android.location.
	 * Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();

		// No Geocoding when no internet
		if (!internetConnectivity) {
			return;
		}

		int noOfResult = 1;
		Geocoder geo = new Geocoder(getService());
		try {
			List<Address> addList = geo.getFromLocation(mLatitude, mLongitude,
					noOfResult);
			for (Address a : addList) {
				this.mUserAddress = a;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public static void appendLog(String text, String nameAppend) {
		int no = UserInfoHandler.logCounter;
		String fileName = "sdcard/sensorAudioLog_" + nameAppend;
		File logFile = new File(fileName + no + "_.file");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
