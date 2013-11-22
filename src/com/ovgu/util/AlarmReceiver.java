package com.ovgu.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.ovgu.jsondb.JSONConnector;
import com.ovgu.zim.AlarmActivity;
import com.ovgu.zim.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

/**
 * This class gets called if an alarm occures. It plays a sound or vibrates (depends on user preferences) and creates a new notification.
 * @author Igor Lueckel
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// Get the audio service to determine the current sound mode (normal, vibration only, silent)
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				
		// Vibrate the mobile phone
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (preferences.getBoolean("CheckBoxVibration", false) == true && audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(750);
		}
		
		// Play the ringtone sound when the sound mode is set to normal
		String alarms = preferences.getString("RingtonePreference", "default ringtone");
		if (alarms.length()>0 && audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
			Uri uri = Uri.parse(alarms);
		    playSound(context, uri);
		}
	    
	    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
	    Calendar now = Calendar.getInstance();
	    String currentAlarmTime = format.format(now.getTime());
	    
	    
	    // Creates an explicit intent for an Activity in your app
	    Intent resultIntent = new Intent(context, AlarmActivity.class);
	    resultIntent.putExtra("currentAlarmTime", currentAlarmTime);
	    
	    // Set the last Alarm
	    ApplicationValues.setLastAlarmTime(ApplicationValues.getCurrentAlarmTime(context), context);
	    ApplicationValues.setCurrentAlarmTime(currentAlarmTime, context);
	    
		String lastAlarmTime=ApplicationValues.getLastAlarmTime(context);
		String lastAnsweredAlarm=ApplicationValues.getLastSavedAlarmTime(context);
		
		if (!lastAlarmTime.equals(lastAnsweredAlarm)){
	    	// The last event was not saved. Save the object with the -77's here
			DatabaseEntry data = new DatabaseEntry();
			data.setContacts("-77");
			data.setContactTime("-77");
			data.setAnswerTime("-77");
			data.setDate(lastAlarmTime.split(" ")[0]);
			data.setTime(lastAlarmTime.split(" ")[1]);
			data.setUserID(ApplicationValues.getUserID(context));
			JSONConnector.addEntry(data, context);
	    }
		
	    PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    // Creates a new notification
	    NotificationCompat.Builder mBuilder =
	            new NotificationCompat.Builder(context)
	            .setSmallIcon(R.drawable.ic_social_group)
	            .setContentTitle("ZIM-Alarm")
	            .setContentText("Auswertung der sozialen Kontakte n�tig!")
	            .setAutoCancel(true)
	            .setContentIntent(resultPendingIntent);
	    
	    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    int mId=1000;
	    
		// mId allows you to update the notification later on.
	    mNotificationManager.notify(mId, mBuilder.build());
	}
	
	private MediaPlayer mMediaPlayer;

	/**
	 * Plays a sound from a Uri
	 * @param context The current context
	 * @param alert Uri to the soundfile
	 */
	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
			
			mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                }
            });
			
			mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
			
		} catch (IOException e) {
			System.out.println("OOPS");
		}
	}

}
