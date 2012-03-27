package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */

public class SpectralEntropy {

	/**
	 * 1. Get FFT Spectrum for given data sample 2. Get Power Spectrum of the
	 * FFT spectrum 3. Perform Normalisation of the power spectrum 4. Calculate
	 * Spectral Entropy
	 */

	public static double calculateSpectralEntropy(double[] normalizedData) {
		double spectralEntropy = 0;
		for (int i = 0; i < normalizedData.length / 2; i++) {
			// Ex: Getting log_2(5) = log_10(5) /log_10(2)
			spectralEntropy += normalizedData[i]
					* (Math.log(normalizedData[i]) / Math.log(2));
		}

		double result = -spectralEntropy;
		return result;
	}

	/**
	 * Calculating the denominator in the equation 3 of Speech Recognition Paper
	 * 
	 * @param data
	 * @return
	 */
	public static double[] performFFTNormalisation(double[] data) {

		double denom = calculateDenominator(data);
		double[] normalizedData = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			normalizedData[i] = data[i] / denom;
		}
		return normalizedData;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private static double calculateDenominator(double[] data) {
		/*
		 * M is the number of data points , we have set it to the data length
		 * itself while performing FFT
		 */

		int M = data.length;
		double denominator = 0;
		for (int i = 0; i < M / 2; i++) {
			denominator += data[i];
		}
		return denominator;
	}

}
