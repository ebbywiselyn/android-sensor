package tweet.sensor.environment;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 *
 * Feature Set for classifying environment
 */
public class EnvInfoActivityFeature {
	public enum Name {
		ENVIRONMENT_MUSIC,
		ENVIRONMENT_AMBIENT_NOISE,
		ENVIRONMENT_SPEECH
	};
	
	public static double musicZCRDiff = 0.0;
	public static double musicLowEnergyFrameRate = 18.2;
	public static double musicSpectralRollOff = 0.17;
	public static double musicSpectralFlux = 0.01;
	public static double musicSpectralCentroid = 130.0;
	public static double musicBandwidth = 10000.0;
	//TODO
	//public static double musicNormalizedWeightPhaseDetection=0;
	//public static double musicRelativeSpectralEntropy=0;
	
	public static double speechZCRDiff = 3.5;
	public static double speechLowEnergyFrameRate = 18.9;
	public static double speechSpectralRollOff = 0.15;
	public static double speechSpectralFlux = 0.01;
	public static double speechSpectralCentroid = 120.0;
	public static double speechBandwidth = 6000.0;
	//TODO
	//public static double speechNormalizedWeightPhaseDetection=0;
	//public static double speechRelativeSpectralEntropy=0;
	
	public static double ambientNoiseZCRDiff = 14.0;
	public static double ambientNoiseLowEnergyFrameRate = 0.0;
	public static double ambientNoiseSpectralRollOff = 0.0;
	public static double ambientNoiseSpectralFlux = 0.2;
	public static double ambientNoiseSpectralCentroid = 0.0;
	public static double ambientNoiseBandwidth = 0.0;
	//TODO
	//public static double ambientNormalizedWeightPhaseDetection=0;
	//public static double ambientRelativeSpectralEntropy=0;
	
	/*
	 * Return the String description of the activity Name
	 * 
	 * @param Name Enumeration value of Activity
	 * 
	 * @return String Returns the String description of the Activity
	 */
	public static String getName(Name a) {

		if (a == Name.ENVIRONMENT_AMBIENT_NOISE) {
			return "Noise";
		} else if (a == Name.ENVIRONMENT_MUSIC) {
			return "Music";
		} else if (a == Name.ENVIRONMENT_SPEECH) {
			return "Speech";
		} 
		
		return "Unclassified: Noise Enum";
	}
	
}
