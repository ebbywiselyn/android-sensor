package tweet.sensor.environment;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 * 
 *         Environment Information Activity Classifier Activity Classifier using
 *         Features, kNN Classification Algorithm
 * 
 */
public class EnvInfoActivityClassifier {

	/**
	 * 
	 * Calculates the distance of the Different Activities with the acquired
	 * values.
	 * 
	 * @param name
	 * @param zcrDiff
	 * @param spectralRollOff
	 * @param spectralFlux
	 * @param spectralCentroid
	 * @param bandwidth
	 * @return
	 */
	private static double kNNClassifyDistance(EnvInfoActivityFeature.Name name,
			double zcrDiff, double lowEnergy, double spectralRollOff,
			double spectralFlux, double spectralCentroid, double bandwidth) {

		double defZcrDiff, defSpectralRollOff, defSpectralFlux, defSpectralCentroid, defBandwidth, defLowEnergyFrameRate;
		double zcrDist = 0.0, rollOffDist = 0.0, fluxDist = 0.0, centroidDist = 0.0, bandwidthDist = 0.0, lowEnergyFrameRateDist = 0.0;
		double globalDist;

		if (name == EnvInfoActivityFeature.Name.ENVIRONMENT_SPEECH) {
			defZcrDiff = EnvInfoActivityFeature.speechZCRDiff;
			defSpectralRollOff = EnvInfoActivityFeature.speechSpectralRollOff;
			defSpectralFlux = EnvInfoActivityFeature.speechSpectralFlux;
			defSpectralCentroid = EnvInfoActivityFeature.speechSpectralCentroid;
			defBandwidth = EnvInfoActivityFeature.speechBandwidth;
			defLowEnergyFrameRate = EnvInfoActivityFeature.speechLowEnergyFrameRate;
		} else if (name == EnvInfoActivityFeature.Name.ENVIRONMENT_MUSIC) {
			defZcrDiff = EnvInfoActivityFeature.musicZCRDiff;
			defSpectralRollOff = EnvInfoActivityFeature.musicSpectralRollOff;
			defSpectralFlux = EnvInfoActivityFeature.musicSpectralFlux;
			defSpectralCentroid = EnvInfoActivityFeature.musicSpectralCentroid;
			defBandwidth = EnvInfoActivityFeature.musicBandwidth;
			defLowEnergyFrameRate = EnvInfoActivityFeature.musicLowEnergyFrameRate;
		} else {
			defZcrDiff = EnvInfoActivityFeature.ambientNoiseZCRDiff;
			defSpectralRollOff = EnvInfoActivityFeature.ambientNoiseSpectralRollOff;
			defSpectralFlux = EnvInfoActivityFeature.ambientNoiseSpectralFlux;
			defSpectralCentroid = EnvInfoActivityFeature.ambientNoiseSpectralCentroid;
			defBandwidth = EnvInfoActivityFeature.ambientNoiseBandwidth;
			defLowEnergyFrameRate = EnvInfoActivityFeature.ambientNoiseLowEnergyFrameRate;
		}

		zcrDist = Math.abs(defZcrDiff - zcrDiff) / 16.0 - 0.0;
		lowEnergyFrameRateDist = Math.abs(defLowEnergyFrameRate - lowEnergy) / 20.0 - 0.0;
		// fluxDist = Math.abs(defSpectralFlux - spectralFlux) / 0.02 - 0.0;
		// rollOffDist = Math.abs(defSpectralRollOff - spectralRollOff) / 0.5 -
		// 0.0;
		// centroidDist = Math.abs(defSpectralCentroid - spectralCentroid);
		// bandwidthDist = Math.abs(defBandwidth - bandwidth);

		globalDist = zcrDist + lowEnergyFrameRateDist + rollOffDist + fluxDist
				+ centroidDist + bandwidthDist;

		return globalDist;
	}

	/**
	 * 
	 * @param musicDist
	 *            global Distance of music
	 * @param ambientNoiseDiff
	 *            global Distance of ambient noise
	 * @param speechDiff
	 *            global Distance of speech
	 * @return
	 */
	private static String kNNClassifyVoting(double ambientNoiseDiff,
			double speechDiff, double musicDist) {

		if (musicDist < ambientNoiseDiff && musicDist < speechDiff) {
			return EnvInfoActivityFeature
					.getName(EnvInfoActivityFeature.Name.ENVIRONMENT_MUSIC);
		} else if (speechDiff < ambientNoiseDiff) {
			return EnvInfoActivityFeature
					.getName(EnvInfoActivityFeature.Name.ENVIRONMENT_SPEECH);
		} else {
			return EnvInfoActivityFeature
					.getName(EnvInfoActivityFeature.Name.ENVIRONMENT_AMBIENT_NOISE);
		}

	}

	/**
	 * 
	 * @param zcrDiff
	 *            local distance of feature ZCR
	 * @param spectralRollOff
	 *            local distance of feature spectralRollOff
	 * @param spectralFlux
	 *            local distance of spectral flux
	 * @param spectralCentroid
	 *            local distance of spectral centroid.
	 * @param bandwidth
	 *            local distance of bandwidth
	 * @return
	 */
	public static String kNNClassifyActivity(double zcrDiff, double lowEnergy,
			double spectralRollOff, double spectralFlux,
			double spectralCentroid, double bandwidth) {
		double musicDist, speechDist, ambientNoiseDist;

		musicDist = kNNClassifyDistance(
				EnvInfoActivityFeature.Name.ENVIRONMENT_MUSIC, zcrDiff,
				lowEnergy, spectralRollOff, spectralFlux, spectralCentroid,
				bandwidth);

		speechDist = kNNClassifyDistance(
				EnvInfoActivityFeature.Name.ENVIRONMENT_SPEECH, zcrDiff,
				lowEnergy, spectralRollOff, spectralFlux, spectralCentroid,
				bandwidth);

		ambientNoiseDist = kNNClassifyDistance(
				EnvInfoActivityFeature.Name.ENVIRONMENT_AMBIENT_NOISE, zcrDiff,
				lowEnergy, spectralRollOff, spectralFlux, spectralCentroid,
				bandwidth);

		System.out.println("Classifier dist:" + musicDist + "," + speechDist
				+ "," + ambientNoiseDist);

		return kNNClassifyVoting(ambientNoiseDist, speechDist, musicDist);
	}
}
