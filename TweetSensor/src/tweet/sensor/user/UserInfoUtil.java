package tweet.sensor.user;

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

import java.util.ArrayList;

/**
 * Utility class for UserInformation processing
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar <praveenkumar.bhadrapur@gmail.com>
 */

public class UserInfoUtil {

	/**
	 * 5-Point Smoothening Algorithm Implementation
	 * 
	 * @param linearData double array of linearized data using linearization and
	 * data interpolation
	 * 
	 * @return double[] double array of smoothened data, using 5-point
	 * smoothening algorithm
	 */
	public static Double[] smoothenData(Double[] linearData) {
		int size = linearData.length - 1;
		Double[] smooth = new Double[size + 1];

		smooth[0] = linearData[0];
		smooth[1] = linearData[1];

		for (int i = 2; i <= size - 2; i++) {
			smooth[i] = (linearData[i - 2] + linearData[i - 1] + linearData[i]
					+ linearData[i + 1] + linearData[i + 2]) / 5.0;
		}

		smooth[size - 1] = linearData[size - 1];
		smooth[size] = linearData[size];

		return smooth;
	}

	/**
	 * Find element position which is largest number lesser than the timeToIndex
	 * element in provided values of sampleTime
	 * 
	 * @param timeToIndex key whose left bounded value to find
	 * 
	 * @param sampleTime ordered haystack of sampleTime to search
	 * 
	 * @return index of the left bounded value (The Element lesser than
	 * timeToIndex) Returns the element if it's equal to timeToIndex or lesser
	 * than it. If all elements are bigger, returns the last element position
	 */
	public static int findLeftBound(int timeToIndex, long[] sampleTime) {
		int len = sampleTime.length - 1;

		if ((sampleTime[0] > timeToIndex) || (sampleTime[0] == timeToIndex)) {
			return 0;
		}

		for (int i = 1; i < len - 1; i++) {
			if ((sampleTime[i] > timeToIndex) || (sampleTime[i] == timeToIndex)) {
				return i - 1;
			}
		}

		return len - 1;
	}

	/**
	 * Find element position which is the smallest number greator than the
	 * timeToIndex element in provided values of sampleTime
	 * 
	 * @param timeToIndex key whose left bounded value to find
	 * 
	 * @param sampleTime ordered haystack of sampleTime to search
	 * 
	 * @return index of the left bounded value (The Element lesser than
	 * timeToIndex) Returns the element if it's equal to timeToIndex or greater
	 * than it. If all elements are lesser, returns the last element position
	 */
	public static int findRightBound(int timeToIndex, long[] sampleTime) {
		int len = sampleTime.length - 1;

		if (sampleTime[0] > timeToIndex) {
			return 0;
		}

		for (int i = 1; i < len - 1; i++) {
			if ((sampleTime[i] > timeToIndex) || (sampleTime[i] == timeToIndex)) {
				return i;
			}
		}

		return len - 1;
	}

	/**
	 * Data Interpolation and Linearization
	 * 
	 * If the two known points are given by the coordinates and the linear
	 * interpolant is the straight line between these points. For a value x in
	 * the interval , the value y along the straight line is given from the
	 * equation
	 * 
	 * Here Data Linerization is done to make sure we get data in correct
	 * sampled intervals, here x-axis is the time and y-axis is the acceleration
	 * 
	 * y=y0+(x-x0)*(y1-y0)/x-x0
	 * 
	 * @param durationSample Total duration of one sample
	 * 
	 * @param totalSamples Total Number of samples
	 * 
	 * @param samplingTime Array of sampled time intervals
	 * 
	 * @param accelerationList List of acceleration values
	 * 
	 * @return double array of data
	 */
	public static Double[] linearizeData(double durationSample,
			int totalSamples, long[] samplingTime,
			ArrayList<Double> accelerationList) {

		int dur = (int) durationSample;
		int index = 0, posl, posr;

		double offsetCalc, x, x0_msec, x1_msec, x0, x1, y0, y1;
		Double linear[] = new Double[totalSamples];

		// Special case first index
		linear[0] = 0.0;

		// General case
		for (int time = dur; time < totalSamples * dur; time = time + dur) {

			index = time / dur;
			index = index % totalSamples;

			// Finding the nearest bounding points
			posl = UserInfoUtil.findLeftBound(time, samplingTime);
			posr = UserInfoUtil.findRightBound(time, samplingTime);
			
			System.out.println("Linearized position of left bound "+ posl);
			System.out.println("Linearized position of right bound "+ posr);

			// X Scale in milli seconds
			x0_msec = samplingTime[posl];
			x1_msec = samplingTime[posr];
			
			

			// X scaled down to single interval mDurationSample
			x0 = x0_msec / (double) durationSample;
			x1 = x1_msec / (double) durationSample;
			
			System.out.println("Linearized x0 "+ x0);
			System.out.println("Linearized x1 "+ x1);

			// Y Scale in acceleration
			y0 = accelerationList.get(posl).doubleValue();
			y1 = accelerationList.get(posr).doubleValue();
			
			System.out.println("Linearized y1 " + y1);
			System.out.println("Linearized  y0 " + y0);
			x = index;
			System.out.println("Linearized x " + x);

			// Calculate y
			if (x1 > x0) {
				offsetCalc = (y1 - y0) * (x - x0) / (x1 - x0);
			} else {
				offsetCalc = 0;
			}

			linear[index] = y0 + offsetCalc;
			System.out.println("Linearized  calculated linearized value"+linear[index]+" for index " + index );
		}

		return linear;
	}

}
