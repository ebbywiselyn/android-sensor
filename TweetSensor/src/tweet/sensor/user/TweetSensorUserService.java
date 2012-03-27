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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

/*
 * 
 * TweetSensorService Provides sensor events for accelerometer, location
 * Bind to the service class using bindService
 * Start the sensor signalling by sending a message
 * 
 * @author Ebby Wiselyn	<ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 * @version 0.0.1
 * 
 */
public class TweetSensorUserService extends Service {

	public static final int MSG_START_SENSING = 1;
	public static final int MSG_STOP_SENSING = 2;
	// Time in milliseconds
	public static final int LOCATION_MIN_TIME_CHANGE_UPDATE = 1000;
	// In meters
	public static final int LOCATION_MIN_DISTANCE_CHANGE_UPDATE = 1;

	private static boolean sHandlerRegistered = false;
	private SensorManager mSensorMgr;
	private LocationManager mLocationMgr;

	class IncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MSG_START_SENSING:
				break;

			case MSG_STOP_SENSING:
				break;

			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Register the listener for accelerometer and location
	 */
	private void startSensors() {
		mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorMgr.registerListener(UserInfoHandler.getHandler(),
				mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		mLocationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				LOCATION_MIN_TIME_CHANGE_UPDATE,
				LOCATION_MIN_DISTANCE_CHANGE_UPDATE,
				UserInfoHandler.getHandler());

		sHandlerRegistered = true;
	}

	/**
	 * Unregister the listener for all sensors
	 */
	private void stopSensors() {
		if (sHandlerRegistered) {
			mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
			mSensorMgr.unregisterListener(UserInfoHandler.getHandler());
		}
	}

	final Messenger mMessenger = new Messenger(new IncomingHandler());
	static final String tag = "TweetSensorUserService";

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(getApplicationContext(),
				"binding to sensor user-info service", Toast.LENGTH_SHORT)
				.show();

		UserInfoHandler.getHandler().setService(this);
		boolean state = isOnline();
		// Toast.makeText(getApplicationContext(), "deviceOnline?" + state,
		// Toast.LENGTH_SHORT).show();

		UserInfoHandler.getHandler().setDeviceConnectivity(state);
		startSensors();
		return mMessenger.getBinder();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(getApplicationContext(),
				"unbinding sensor user-info service", Toast.LENGTH_SHORT)
				.show();

		stopSensors();
		UserInfoHandler.getHandler().setService(null);
		UserInfoHandler.clearHandler();
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

}
