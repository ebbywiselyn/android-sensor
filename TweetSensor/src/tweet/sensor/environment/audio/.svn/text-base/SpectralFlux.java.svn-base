package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */

public class SpectralFlux {

	private double[] curMagnitudeSpectrum = null;
	private double[] prevMagnitudeSpectrum = null;
	double spectralFluxData;

	/**
	 * 
	 * @param prevMagnitudeSpectrum
	 * @param curMagnitudeSpectrum
	 */
	public SpectralFlux(double[] prevMagnitudeSpectrum, double[] curMagnitudeSpectrum) {
		this.prevMagnitudeSpectrum = prevMagnitudeSpectrum;
		this.curMagnitudeSpectrum = curMagnitudeSpectrum;
	}

	/**
	 * 
	 */
	private void process() {
		double[][] magnitudeSpectrumData = new double[2][];
		magnitudeSpectrumData[0] = curMagnitudeSpectrum;
		magnitudeSpectrumData[1] = prevMagnitudeSpectrum;
		try {
			spectralFluxData = extractFeature(magnitudeSpectrumData);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	/**
	 * 
	 * @param other_feature_values
	 * @return
	 * @throws Exception
	 */
	private double extractFeature(double[][] other_feature_values) throws Exception {
		double[] this_magnitude_spectrum = other_feature_values[0];
		double[] previous_magnitude_spectrum = other_feature_values[1];

		double sum = 0.0;
		for (int bin = 0; bin < this_magnitude_spectrum.length; bin++) {
			double difference = this_magnitude_spectrum[bin]
					- previous_magnitude_spectrum[bin];
			double differences_squared = difference * difference;
			sum += differences_squared;
		}

		return sum;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getSpectralFlux() {
		process();
		return spectralFluxData;
	}
}