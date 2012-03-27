package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */

/**
 * Get Bandwidth value, Calculate denominator, Calculate Numerator
 * and divide it by denominator according to the formula in Sound Sensing
 * pdf
 */
public class Bandwidth {
	/**
	 * 
	 * @param specCentroid
	 * @param powerSpectrum
	 * @return
	 */
	public double process(double specCentroid, double[] powerSpectrum) {
		double denom = calculateDenom(powerSpectrum);
		double bandwidth = 0;

		double numerator = 0;
		for (int i = 0; i < powerSpectrum.length; i++) {
			numerator += Math.pow((i - specCentroid), 2)
					* Math.pow(powerSpectrum[i], 2);
		}

		bandwidth = numerator / denom;

		return bandwidth;
	}

	/**
	 * 
	 * @param powerSpectrumData
	 * @return
	 */
	private double calculateDenom(double[] powerSpectrumData) {
		double denomVal = 0;
		for (int i = 0; i < powerSpectrumData.length; i++) {
			denomVal += Math.pow(powerSpectrumData[i], 2);
		}
		return denomVal;
	}
}
