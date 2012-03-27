package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */


public class ZeroCountRate {
	static double zcr = 0.0;

	/**
	 * 
	 * @param samples
	 * @return
	 */
	public static double process(double[] samples) {
		double zcr=0.0;
		try {
			zcr = findZCR(samples, 44100);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zcr;
	}

	/**
	 * 
	 * @param samples
	 * @param sampling_rate
	 * @return
	 */
	private static double findZCR(double[] samples, double sampling_rate)  {
		long count = 0;
		for (int samp = 0; samp < samples.length - 1; samp++) {
			if (samples[samp] > 0.0 && samples[samp + 1] < 0.0)
				count++;
			else if (samples[samp] < 0.0 && samples[samp + 1] > 0.0)
				count++;
			else if (samples[samp] == 0.0 && samples[samp + 1] != 0.0)
				count++;
		}
		double result = (double)count;
		return result;
	}
}
