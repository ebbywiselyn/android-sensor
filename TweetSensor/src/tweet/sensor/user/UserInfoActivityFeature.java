/**
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

/**
 * Class for different features of measurement in every User Activity
 * like average, standard deviation, peek to peek
 * 
 * Provides default values for every activity's feature 
 * 
 * @author Ebby Wiselyn	<ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur	<praveenkumar.bhadarpur@gmail.com>
 * @version 0.0.1
 * 
 */
public class UserInfoActivityFeature {
	public enum Name {
		ACTIVITY_STAND, ACTIVITY_WALK, ACTIVITY_RUN
	};

	public static double maxStdDev = 0.0;
	public static double minStdDev = 7.0;
	public static double minAvg = 8.0;
	public static double maxAvg = 20.0;
	public static double minPeekToPeek = 20.0;
	public static double maxPeekToPeek = 55.0;
	
	// Walk Standard Deviation values
	public static double walkStdDev = 4.814436111629097;
	
	// Walk Peek to Peek values
	public static double walkPeekToPeek = 35.28362518120852;

	// Walk Average values
	public static double walkAvg = 11.895184752921534;

	// Run Standard Deviation Values
	public static double runStdDev = 6.04963780127633523;

	// Run Peek to Peek Values
	public static double runPeekToPeek = 43.6701736221696024;

	// Run Average Values
	public static double runAvg = 14.374379100371538;

	// Stand Standard Deviation
	public static double standStdDev = 1.0472167012925262;

	// Stand Peek to Peek
	public static double standPeekToPeek = 25.1736325226226;
	
	// Stand Average
	public static double standAvg = 9.07368031875503;

	
	//Weights
	public static double weightStdDev = 1.0;
	public static double weightPeekToPeek = 1.0;
	public static double weightAvg = 1.0;

	/*
	 * Return the String description of the activity Name
	 * 
	 * @param Name Enumeration value of Activity
	 * 
	 * @return String Returns the String description of the Activity
	 */
	public static String getName(Name a) {

		if (a == Name.ACTIVITY_RUN) {
			return "Running";
		} else if (a == Name.ACTIVITY_STAND) {
			return "Standing";
		} else {
			return "Walking";
		}
	}
}
