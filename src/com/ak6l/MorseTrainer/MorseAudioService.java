package com.ak6l.MorseTrainer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MorseAudioService extends Service {
	private NotificationManager mNotificationMgr;
	
	private final double TONE_FREQUENCY = 700.0;
	private final double WORDS_PER_MINUTE = 15.0;
	private final static double SAMPLE_RATE = 8000.0;
	
	private WaveformGenerator waveformGenerator;

	public class MorseAudioBinder extends Binder {
        MorseAudioService getService() {
            return MorseAudioService.this;
        }
    }
    
    @Override
    public void onCreate() {
        mNotificationMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        waveformGenerator = new WaveformGenerator(TONE_FREQUENCY, WORDS_PER_MINUTE, SAMPLE_RATE);
        
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MorseAudioService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNotificationMgr.cancel(R.string.morse_audio_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.morse_audio_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new MorseAudioBinder();


    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.morse_audio_service_started);

        // Set the icon, scrolling text and time stamp
        Notification notification = new Notification(R.drawable.stat_sys_warning, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MorseTrainer.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.morse_audio_service_label),
                       text, contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNotificationMgr.notify(R.string.morse_audio_service_started, notification);
    }
    
    public void playMorseString(final String text) {
    	//try {
    		Thread t = new Thread(new Runnable() {
    			public void run() {
    		    	short audioBuffer[];

    		    	Log.i("MorseAudioService(Thread)", "Before waveformGenerator call");
        	    	audioBuffer = waveformGenerator.audioBufferFromText(text);
        	    	Log.i("MorseAudioService(Thread)", "audioBuffer.length=" + new Integer(audioBuffer.length).toString());
        	    	
        	        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
        	                (int)MorseAudioService.SAMPLE_RATE,
        	                AudioFormat.CHANNEL_CONFIGURATION_MONO,
        	                AudioFormat.ENCODING_PCM_16BIT,
        	                audioBuffer.length,
        	                AudioTrack.MODE_STREAM);
        	        audioTrack.play();
        	        audioTrack.write(audioBuffer, 0, audioBuffer.length);
    			}
    		});
    		
    		Log.i("MorseAudioService", "Running the thread...");
    		t.run();
    	//} catch (Throwable t) {
    	//	Log.e("MorseAudioService", t.getMessage());
    	//}
    }
}