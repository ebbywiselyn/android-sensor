package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */

public class SpectralCentroid {

	public double process(double[] pow_spectrum) {

		double total = 0.0;
		double weighted_total = 0.0;
		for (int bin = 0; bin < pow_spectrum.length; bin++) {
			weighted_total += bin * pow_spectrum[bin];
			total += pow_spectrum[bin];
		}

		double result;
		if (total != 0.0) {
			result = weighted_total / total;
		} else {
			result = 0.0;
		}

		return result;
	}

}