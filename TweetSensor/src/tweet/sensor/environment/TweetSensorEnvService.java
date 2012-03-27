package tweet.sensor.environment;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

/**
 * 
 * @author Ebby Wiselyn <ebbywiselyn@gmail.com>
 * @author PraveenKumar Bhadrapur <praveenkumar.bhadrapur@gmail.com>
 * 
 *         Bind to the service to get broadcasted tweets on the device
 *         environmental information
 * 
 */
public class TweetSensorEnvService extends Service {

	AudioProcessor audioProcessor;

	public static String TAG = "TweetSensorEnvService";

	/**
	 * 
	 * @author Ebby
	 * 
	 */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				super.handleMessage(msg);
			}
		}
	}

	final Messenger mMessenger = new Messenger(new IncomingHandler());

	/**
	 * 
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		Toast.makeText(getApplicationContext(),
				"binding to env-sensor service", Toast.LENGTH_SHORT).show();

		audioProcessor = new AudioProcessor();
		audioProcessor.startRecording();
		audioProcessor.setSensorService(this);
		return mMessenger.getBinder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	public boolean onUnbind(Intent intent) {
		Toast.makeText(getApplicationContext(), "unbinding env-sensor service",
				Toast.LENGTH_SHORT).show();

		audioProcessor.stopRecording();
		audioProcessor.setSensorService(null);

		return false;
	}

}
