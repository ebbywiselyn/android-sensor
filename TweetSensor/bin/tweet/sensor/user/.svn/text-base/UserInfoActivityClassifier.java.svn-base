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

import android.util.Log;

/**
 * Classifies user activity for providing user information
 * User activities like Stand, Walk, Run 
 * 
 * Provides helper methods
 * 
 * @author Ebby Wiselyn	<ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur	<praveenkumar.bhadrapur@gmail.com>
 * @version 0.0.1
 * 
 */

public class UserInfoActivityClassifier {

	/*
	 * Compute Peek to Peek for an array of data
	 */
	public static double computePeakToPeak(Double[] smoothedData) {
		double peakToPeakVal = 0.0;
		double rmsVal = 0.0;
		double sumSqVal = 0.0;

		for (Double item : smoothedData) {
			double itemSqVal = Math.pow(item.doubleValue(), 2);
			sumSqVal += itemSqVal;
		}

		rmsVal = Math.sqrt(sumSqVal / smoothedData.length);
		peakToPeakVal = 2.8 * rmsVal;

		return peakToPeakVal;
	}

	/*
	 * Compute Standard Deviation for an array of data
	 * 
	 * @param smoothedData Data after linearization and 5 point smoothening
	 * algorithm
	 * 
	 * @param avgVal Average of the data
	 * 
	 * @return double Standard Deviation of the data
	 */
	public static double computeDiffSq(Double[] smoothedData, double avgVal) {
		double stddev = 0.0;
		double sumVal = 0.0;

		for (Double item : smoothedData) {
			double sqVal = Math.pow((item.doubleValue() - avgVal), 2);
			sumVal += sqVal;
		}

		stddev = Math.sqrt(sumVal / smoothedData.length);

		return stddev;
	}

	/*
	 * Compute Average for an array of data
	 * 
	 * @param smoothedData Data after data linearization and 5-point smootehning
	 * algorithm
	 * 
	 * @return double Average of the data
	 */
	public static double computeAvg(Double[] smoothedData) {
		double sum = 0.0;
		double avg = 0.0;

		for (Double data : smoothedData) {
			sum += data.doubleValue();
		}

		avg = sum / smoothedData.length;
		return avg;
	}

	/*
	 * kNN Classification Algorithm implementation 1NN Classification and no
	 * voting
	 * 
	 * @param name Name of the activity to compare the distance with
	 * 
	 * @param avg Average of the data
	 * 
	 * @param stdDev Standard Deviation of the data
	 * 
	 * @param peekToPeek Peek To Peek value of the data
	 * 
	 * @return double 1NN local distance between the default values of the
	 * activity and the provided values.
	 */
	private static double kNNClassifyDistance(
			UserInfoActivityFeature.Name name, double avg, double stdDev,
			double peekToPeek) {

		double avgDist, stdDevDist, peekToPeekDist, globalDist;
		double avgAct, stdDevAct, peekToPeekAct;
		double maxAvg, minAvg, maxStdDev, minStdDev, maxPeekToPeek, minPeekToPeek;
		
		maxAvg = UserInfoActivityFeature.maxAvg;
		minAvg = UserInfoActivityFeature.minAvg;
		maxPeekToPeek = UserInfoActivityFeature.maxPeekToPeek;
		minPeekToPeek = UserInfoActivityFeature.minPeekToPeek;
		maxStdDev =  UserInfoActivityFeature.maxStdDev;
		minStdDev = UserInfoActivityFeature.minStdDev;

		if (name == UserInfoActivityFeature.Name.ACTIVITY_RUN) {
			avgAct = UserInfoActivityFeature.runAvg;
			stdDevAct = UserInfoActivityFeature.runStdDev;
			peekToPeekAct = UserInfoActivityFeature.runPeekToPeek;
		} else if (name == UserInfoActivityFeature.Name.ACTIVITY_WALK) {
			avgAct = UserInfoActivityFeature.walkAvg;
			stdDevAct = UserInfoActivityFeature.walkStdDev;
			peekToPeekAct = UserInfoActivityFeature.walkPeekToPeek;
		} else {
			avgAct = UserInfoActivityFeature.standAvg;
			stdDevAct = UserInfoActivityFeature.standStdDev;
			peekToPeekAct = UserInfoActivityFeature.walkPeekToPeek;
		}

		// Local Distance of Avg
		avgDist = Math.abs((avg - avgAct) / (maxAvg - minAvg));

		// Local Distance of StdDev
		stdDevDist = Math.abs((stdDev - stdDevAct) / (maxStdDev - minStdDev));
		//stdDevDist = 0;

		// Local Distance of PeekToPeek
		peekToPeekDist = Math.abs((peekToPeek - peekToPeekAct) / (maxPeekToPeek - minPeekToPeek));

		System.out.println ("TweetSensor" + name + "avgDist" + avgDist + "stdDevDist" + stdDevDist+ "peekToPeekDist" + peekToPeekDist);
		globalDist = UserInfoActivityFeature.weightAvg * avgDist
				+ UserInfoActivityFeature.weightStdDev * stdDevDist
				+ UserInfoActivityFeature.weightPeekToPeek * peekToPeekDist;

		return globalDist;
	}

	/*
	 * Voting:
	 * 
	 * We don't need voting for 1NN Just compare
	 * 
	 * @param standDist Local distance of the stand activity
	 * 
	 * @param walkDist Local distance of the walk activity
	 * 
	 * @param runDist Local distance of the run activity
	 */
	private static String kNNClassifyVoting(double standDist, double walkDist,
			double runDist) {
		if (standDist < walkDist && standDist < runDist) {
			return UserInfoActivityFeature
					.getName(UserInfoActivityFeature.Name.ACTIVITY_STAND);
		} else if (runDist < walkDist) {
			return UserInfoActivityFeature
					.getName(UserInfoActivityFeature.Name.ACTIVITY_RUN);
		} else {
			return UserInfoActivityFeature
					.getName(UserInfoActivityFeature.Name.ACTIVITY_WALK);
		}

	}

	/*
	 * Public method for kNN Classification and Voting Algorithm
	 * 
	 * @smoothedData Data to be classified after linerization and 5-point
	 * smoothening algorithm
	 * 
	 * @return String The activity classified
	 */
	public static String kNNClassifyActivity(Double[] smoothedData) {
		double avg, stdDev, peekToPeek;
		double distRun, distWalk, distStand;
		String act;

		avg = UserInfoActivityClassifier.computeAvg(smoothedData);

		stdDev = UserInfoActivityClassifier.computeDiffSq(smoothedData, avg);

		peekToPeek = UserInfoActivityClassifier.computePeakToPeak(smoothedData);

		Log.d(TweetSensorUserService.tag, TweetSensorUserService.tag
				+ "_Activity" + "avg: " + avg + ", stdDev: " + stdDev
				+ ", peek: " + peekToPeek);

		// local distance of run
		distRun = UserInfoActivityClassifier.kNNClassifyDistance(
				UserInfoActivityFeature.Name.ACTIVITY_RUN, avg, stdDev,
				peekToPeek);

		// local distance of walk
		distWalk = UserInfoActivityClassifier.kNNClassifyDistance(
				UserInfoActivityFeature.Name.ACTIVITY_WALK, avg, stdDev,
				peekToPeek);

		// local distance of stand
		distStand = UserInfoActivityClassifier.kNNClassifyDistance(
				UserInfoActivityFeature.Name.ACTIVITY_STAND, avg, stdDev,
				peekToPeek);

		System.out.println ("TweetSensor" + "walk:" + distWalk + "stand:" + distStand + "run:" + distRun);
		// Compute global distance and classify
		String text = "avg: " + avg + ", stdDev: " + stdDev + ", peek: "
				+ peekToPeek;
		//UserInfoHandler.appendLog(text, "acc");
		act = UserInfoActivityClassifier.kNNClassifyVoting(distStand, distWalk,
				distRun);

		return act;
	}

}
