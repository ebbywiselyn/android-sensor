package tweet.sensor.environment;

import java.util.ArrayList;

import tweet.sensor.environment.audio.tools.DSPMethods;
import tweet.sensor.environment.audio.tools.FFT;
import tweet.sensor.environment.audio.Bandwidth;
import tweet.sensor.environment.audio.LowEnergyWindow;
import tweet.sensor.environment.audio.RMS;
import tweet.sensor.environment.audio.SpectralCentroid;
import tweet.sensor.environment.audio.SpectralEntropy;
import tweet.sensor.environment.audio.SpectralFlux;
import tweet.sensor.environment.audio.SpectralRolloffPoint;
import tweet.sensor.environment.audio.ZeroCountRate;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 * @version 0.0.2
 * 
 * 
 *          Audio Processor read audio samples from the buffer and applies the
 *          feature extraction and classification
 * 
 *          Reads the samples size of over SAMPLING_SIZE and applies the Audio
 *          classification over processInt
 * 
 */

public class AudioProcessor {
	private int readSize = 1;
	private int readOffset = 0;
	private int trail = 0;
	private int zcrTopCount = 0;
	private int zcrBottomCount = 0;

	private final int processInt = 16;
	private final int sampleRate = 44100;
	private static int processingCount = 0;
	private static int readCount = 0;
	private static int consCount = 0;

	// Store the audio data in a round buffer
	private static int bufferInc = 0;
	private static final int ROUND_BUFFER_SIZE = 50;
	private static final int SAMPLING_SIZE = 4096;

	private static boolean isRecording = true;

	// array of type double to process data
	private double[] dataBuffer;

	// array of type short to pull data from the hardware
	private short[] audioBuffer = new short[5000];

	// ArrayList to save periodic values
	private ArrayList<Double> zcrList = new ArrayList<Double>();
	private ArrayList<Double> rollOffList = new ArrayList<Double>();
	private ArrayList<Double> centroidList = new ArrayList<Double>();
	private ArrayList<Double> fluxList = new ArrayList<Double>();
	private ArrayList<Double> bandwidthList = new ArrayList<Double>();
	private ArrayList<Double> lowEnergyCountList = new ArrayList<Double>();
	private ArrayList<String> classifyList = new ArrayList<String>();
	private ArrayList<Double> powList = new ArrayList<Double>();

	// Locks for threads created
	private Object processingLock = new Object();

	// Data for Flux previous and current
	private static double[][] consecutiveData = new double[2][SAMPLING_SIZE];
	private static double[][] roundBuffer = new double[ROUND_BUFFER_SIZE][SAMPLING_SIZE];
	private static boolean consecutiveDataAvailable = false;

	// Store the recent activity for notification
	private static String recentActivity;

	private AudioRecord audioRecord;
	private TweetSensorEnvService sensor;

	private Runnable audioReadThread = new Runnable() {

		public void run() {
			int size;

			audioRecord.startRecording();

			while (isRecording) {

				// Read size of readSize and store in audioBuffer
				size = audioRecord.read(audioBuffer, readOffset, readSize);
				readOffset = readOffset + size;

				// Loop until sampleSize length of data
				if (readOffset < SAMPLING_SIZE) {
					// Marker has not been reached
					continue;
				}

				// Process audioBuffer when marker is reached
				dataBuffer = shortToDouble(audioBuffer, readOffset);

				readOffset = 0;
				readCount = readCount + 1;
				trail = readCount - processingCount;

				// processing thread to process dataBuffer
				new Thread(new FeatureProcessor(dataBuffer, processingCount))
						.start();

				while (trail > ROUND_BUFFER_SIZE) {
					trail = readCount - processingCount;
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		}
	};

	/**
	 * 
	 * Uses a roundedBuffer
	 * 
	 * @param sdata
	 * @return
	 */
	private double[] shortToDouble(short[] sdata, int length) {
		double d[];

		d = roundBuffer[bufferInc];

		for (int i = 0; i < length; i++) {
			d[i] = sdata[i];
		}

		bufferInc = bufferInc + 1;
		bufferInc = bufferInc % ROUND_BUFFER_SIZE;
		return d;
	}

	/**
	 * 
	 */
	private AudioRecord.OnRecordPositionUpdateListener listener = new AudioRecord.OnRecordPositionUpdateListener() {

		@Override
		public void onPeriodicNotification(AudioRecord recorder) {
		}

		@Override
		public void onMarkerReached(AudioRecord recorder) {
		}
	};

	/**
	 * 
	 */
	public void startRecording() {

		int buffer_size = AudioRecord.getMinBufferSize(sampleRate,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, buffer_size);

		// audioRecord.setPositionNotificationPeriod(frameSize);
		audioRecord.setRecordPositionUpdateListener(listener);
		isRecording = true;
		new Thread(audioReadThread).start();
	}

	/**
	 * 
	 */
	public void stopRecording() {
		isRecording = false;
		processingCount = 0;
		consCount = 0;
		readCount = 0;
		bufferInc = 0;
		recentActivity = null;
		audioRecord.stop();
	}

	/**
	 * 
	 * @param obj
	 *            TweetSensorEnvService Object
	 * 
	 */
	public void setSensorService(TweetSensorEnvService obj) {
		this.sensor = obj;
	}

	/**
	 * 
	 * @param classifyList
	 *            List of classified data
	 * @return Maximum occurence of the activity in the activity list
	 * 
	 */
	public String maximumOf(ArrayList<String> classifyList) {
		int speechCount = 0, musicCount = 0, noiseCount = 0;
		for (String s : classifyList) {
			if (s.equalsIgnoreCase("Speech")) {
				speechCount += 1;
			} else if (s.equalsIgnoreCase("Music")) {
				musicCount += 1;
			} else {
				noiseCount += 1;
			}
		}

		if (speechCount > musicCount && speechCount > noiseCount) {
			return "Speech";
		} else if (musicCount > noiseCount) {
			return "Music";
		} else {
			return "Noise Unclassified";
		}
	}

	/**
	 * 
	 * @param index
	 *            Check if index of 100 has been reached
	 * @return
	 */
	private static boolean verifyReachedWindowIndex(int index) {
		int number_windows = 100;

		if ((index + 1) == number_windows) {
			// index is 100, 200
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @author Ebby Wiselyn
	 * @author PraveenKumar Bhadrapur
	 * 
	 *         FeatureProcessor concurrent processing of every frame of
	 *         SAMPLING_SIZE bytes, data provided during initialization
	 * 
	 */
	class FeatureProcessor implements Runnable {
		private final int smoothenOverFrames = 5;
		private static final int RMS_SILENCE = 150;

		private double rollOff, spectralCentroid, spectralEntropy, rms;
		private double lowEnergyCount, fluxData, bandwidth, power, zcr;
		private double avgZcr, avgFlux, avgBandwidth, avgRollOff, avgCentroid,
				avgLowEnergy, varianceCentroid, varianceZCR;
		private double[] data, normalizedData, powerSpectrum,
				magnitudeSpectrum;

		FeatureProcessor(double[] d, int lastSampleCount) {
			this.data = d;
		}

		private void sendBroadcastMessage(String act) {
			if (sensor != null) {
				Intent i = new Intent();
				i.putExtra("envInfoStr", act + " Power:" + (int) power + " db");
				i.setAction(Intent.ACTION_VIEW);
				sensor.sendBroadcast(i);
			}
		}

		@Override
		public void run() {
			synchronized (processingLock) {
				FFT fftData = null, fftNormData = null;
				rms = RMS.getRMS(data, 0, data.length);
				power = RMS.getPowerDb(rms);

				System.out.println ("rms" + rms);
				
				if (rms < RMS_SILENCE) {
					if (recentActivity == null
							|| (!recentActivity.equals("Silence"))) {
						recentActivity = "Silence";
						System.out.println("Classifier Silence");
						sendBroadcastMessage("Silence");
					} else {
						System.out.println("No processing, Silence");
					}

					processingCount = processingCount + 1;
					return;
				}

				normalizedData = DSPMethods.normalizeSamples(data);

				try {
					fftData = new FFT(data, null, false, true);
					fftNormData = new FFT(normalizedData, null, false, true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				magnitudeSpectrum = fftNormData.getMagnitudeSpectrum();
				powerSpectrum = fftData.getPowerSpectrum();

				// DC component
				powerSpectrum[0] = 0;
				magnitudeSpectrum[0] = 0;

				normalizedData = SpectralEntropy
						.performFFTNormalisation(powerSpectrum);
				spectralEntropy = SpectralEntropy
						.calculateSpectralEntropy(normalizedData);

				// ZCR
				zcr = ZeroCountRate.process(data);
				zcrList.add(zcr);

				// Low Energy Frame Rate
				lowEnergyCount = getLowEnergyFrameRate(data);
				lowEnergyCountList.add(lowEnergyCount);

				// Spectral Flux, Consecutive Frames
				consCount = consCount % 2;
				consecutiveData[consCount] = magnitudeSpectrum;

				if (!consecutiveDataAvailable) {
					consecutiveData[0] = magnitudeSpectrum;
					consecutiveDataAvailable = true;
				} else {
					SpectralFlux fluxP = null;
					if (consCount == 0) {
						fluxP = new SpectralFlux(consecutiveData[0],
								consecutiveData[1]);
					} else {
						fluxP = new SpectralFlux(consecutiveData[1],
								consecutiveData[0]);
					}

					fluxData = fluxP.getSpectralFlux();
					fluxList.add(fluxData);
				}
				consCount = consCount + 1;

				// Spectral Rolloff
				SpectralRolloffPoint spectralRollOff = new SpectralRolloffPoint();
				rollOff = spectralRollOff.process(powerSpectrum);
				rollOffList.add(rollOff);

				// Spectral Centroid
				SpectralCentroid sCentroid = new SpectralCentroid();
				spectralCentroid = sCentroid.process(powerSpectrum);
				centroidList.add(spectralCentroid);

				// Bandwidth
				Bandwidth bandwidthP = new Bandwidth();
				bandwidth = bandwidthP.process(spectralCentroid, powerSpectrum);
				bandwidthList.add(bandwidth);

				// Power calculation
				powList.add(power);

				// Average Processing
				if ((processingCount % processInt) >= (processInt - 1)) {

					// ZCR mean
					double sum = 0.0;
					for (Double d : zcrList) {
						sum += d;
					}
					avgZcr = sum / processInt;

					// ZCR variance
					double sum2 = 0.0;
					for (Double d : zcrList) {
						sum2 += (d - avgZcr) * (d - avgZcr);
					}
					varianceZCR = sum2 / zcrList.size() - 1;

					for (Double zcr : zcrList) {
						if (zcr > (avgZcr + (avgZcr / 6.0))) {
							zcrTopCount = zcrTopCount + 1;
						} else if (zcr < (avgZcr - (avgZcr / 6.0))) {
							zcrBottomCount = zcrBottomCount + 1;
						}
					}

					zcrList.clear();

					// ZCR Peak
					double zcrPeak = Math.abs(zcrBottomCount - zcrTopCount);
					System.out.println("avg zcr:" + avgZcr + ",variance,"
							+ varianceZCR + ",diff:" + ",bot:" + zcrBottomCount
							+ ",top: " + zcrTopCount + ",diff: " + zcrPeak);

					zcrBottomCount = 0;
					zcrTopCount = 0;

					// Low Energy Frame Rate
					avgLowEnergy = 0.0;
					sum = 0.0;
					for (Double d : lowEnergyCountList) {
						sum += d;
					}

					avgLowEnergy = sum / lowEnergyCountList.size();
					lowEnergyCountList.clear();

					// Spectral Centroid
					sum = 0.0;
					for (Double d : centroidList) {
						sum += d;
					}

					avgCentroid = sum / processInt;

					// Spectral Centroid variance
					sum2 = 0.0;
					for (Double d : centroidList) {
						sum2 += (d - avgCentroid) * (d - avgCentroid);
					}

					varianceCentroid = sum2 / centroidList.size() - 1;
					centroidList.clear();

					// Spectral Flux
					sum = 0.0;
					for (Double d : fluxList) {
						sum += d;
					}

					avgFlux = sum / processInt;
					fluxList.clear();

					// Spectral Rolloff
					sum = 0.0;
					for (Double d : rollOffList) {
						sum += d;
					}

					avgRollOff = sum / processInt;
					rollOffList.clear();

					// Spectral Bandwidth
					sum = 0.0;
					for (Double d : bandwidthList) {
						sum += d;
					}

					avgBandwidth = sum / processInt;
					bandwidthList.clear();

					// Spectral Power
					sum = 0.0;
					for (Double d : powList) {
						sum += d;
					}

					power = sum / powList.size();
					powList.clear();

					// Classify
					String classify = EnvInfoActivityClassifier
							.kNNClassifyActivity(zcrPeak, avgLowEnergy,
									avgRollOff, avgFlux, avgCentroid,
									avgBandwidth);
					classifyList.add(classify);

					// Smoothening over number intervals
					if (classifyList.size() > smoothenOverFrames) {
						String act = maximumOf(classifyList);

						System.out.println("Classifier " + act);
						//if (recentActivity == null) || (!act.equals(recentActivity))) {
							System.out.println("Classifier Tweet Sent");
							sendBroadcastMessage(act);
						//}
						recentActivity = act;
						classifyList.clear();
					}
				}

				// Processing Count
				processingCount = processingCount + 1;
			}
		}

		/**
		 * 
		 * @param data
		 * @return
		 */
		private double getLowEnergyFrameRate(double[] data) {
			int num_win = 100;
			int rmsBufCnt = 0;
			double rmsVal = 0;
			double lowCount = 0.0;
			double[] rmsDataBuf = new double[num_win];

			LowEnergyWindow fractionOfLowEnergyWindows = new LowEnergyWindow();

			for (int i = 0; i < data.length; i++) {
				if ((i + num_win) < data.length) {
					rmsVal = RMS.getRMS(data, i, i + num_win);
				} else {
					// last part of the data
					rmsVal = RMS.getRMS(data, i, data.length);
				}

				if (verifyReachedWindowIndex(rmsBufCnt)) {
					double result = 0.0;
					try {
						result = fractionOfLowEnergyWindows.process(rmsDataBuf);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Count of number of low frames
					lowCount += result;

					// Resetting the buffer counter
					rmsBufCnt = 0;
					rmsDataBuf[rmsBufCnt++] = rmsVal;
				} else {
					rmsDataBuf[rmsBufCnt++] = rmsVal;
				}

			}

			return lowCount;
		}
	}

}
