

package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */


public class SpectralRolloffPoint {
	protected double cutoff = 0.85;

	/**
	 * 
	 * @param pow_spectrum
	 * @return
	 */
	public double process(double[] pow_spectrum) {

		double total = 0.0;
		for (int bin = 0; bin < pow_spectrum.length; bin++)
			total += pow_spectrum[bin];
		double threshold = total * cutoff;

		total = 0.0;
		int point = 0;
		for (int bin = 0; bin < pow_spectrum.length; bin++) {
			total += pow_spectrum[bin];
			if (total >= threshold) {
				point = bin;
				bin = pow_spectrum.length;
			}
		}

		double result;
		result = ((double) point) / ((double) pow_spectrum.length);
		return result;
	}
	
}