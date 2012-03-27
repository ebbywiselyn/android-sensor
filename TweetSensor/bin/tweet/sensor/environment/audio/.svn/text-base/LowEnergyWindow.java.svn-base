package tweet.sensor.environment.audio;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 */

public class LowEnergyWindow {

	/**
	 * 
	 * @param audio data
	 * @return lowEnergyWindow percentile
	 */
	public double process(double[] data) throws Exception {
		double avg = 0.0;

		for (int i = 0; i < data.length; i++)
			avg += data[i];

		avg = avg / ((double) data.length);

		int count = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i] < avg) {
				count++;
			}
		}

		double result = ((double) count) / ((double) data.length);
		return result;
	}

}