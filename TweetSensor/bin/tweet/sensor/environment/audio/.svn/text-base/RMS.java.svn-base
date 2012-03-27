package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 *
 */
public class RMS

{
	
	/**
	 * 
	 * @param samples
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static double getRMS(double[] samples, int startIndex, int endIndex) {
		double sum = 0.0;
		for (int samp = startIndex; samp < endIndex; samp++)
			sum += Math.pow(samples[samp], 2);
		double rms = Math.sqrt(sum / samples.length);
		return rms;
	}
	
	/**
	 * 
	 * @param rmsVal
	 * @return
	 */
	public static double getPowerDb(double rmsVal) {

		System.out.println(" rms value " + rmsVal);
		double powerDBmVal = Math.log10(rmsVal) * 10 + 30;
		// Convert to dBm.
		System.out.println(" Power in Db values" + powerDBmVal);
		return powerDBmVal;
	}

}