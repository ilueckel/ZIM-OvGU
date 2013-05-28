package com.ovgu.util;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AlarmSetter {
	public void setAlarms(Context View){
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(View);
	    int alarm1=Integer.parseInt(preferences.getString("WakeTime1", "-1"));
	    int alarm2=Integer.parseInt(preferences.getString("WakeTime2", "-1"));
	    int alarm3=Integer.parseInt(preferences.getString("WakeTime3", "-1"));
	    int alarm4=Integer.parseInt(preferences.getString("WakeTime4", "-1"));
	    
	    Intent i1 = new Intent("com.ovgu.zim.AlarmActivity");
		i1.putExtra("alarmtime", Integer.toString(alarm1));
		
		Intent i2 = new Intent("com.ovgu.zim.AlarmActivity");
		i2.putExtra("alarmtime", Integer.toString(alarm1));
		
		Intent i3 = new Intent("com.ovgu.zim.AlarmActivity");
		i3.putExtra("alarmtime", Integer.toString(alarm1));
		
		Intent i4 = new Intent("com.ovgu.zim.AlarmActivity");
		i4.putExtra("alarmtime", Integer.toString(alarm1));
		
	    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(View.getApplicationContext(), 8601, i1, PendingIntent.FLAG_ONE_SHOT);
	    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(View.getApplicationContext(), 8602, i2, PendingIntent.FLAG_ONE_SHOT);
	    PendingIntent pendingIntent3 = PendingIntent.getBroadcast(View.getApplicationContext(), 8603, i3, PendingIntent.FLAG_ONE_SHOT);
	    PendingIntent pendingIntent4 = PendingIntent.getBroadcast(View.getApplicationContext(), 8604, i4, PendingIntent.FLAG_ONE_SHOT);
	    
	    AlarmManager alarmManager = (AlarmManager) View.getSystemService(Activity.ALARM_SERVICE);
	    
	    Calendar cal1 = Calendar.getInstance();
	    // This sets the alarm to the next day, if the alarm hour already left today.
	    // Without this, the past alarms will be also broadcasted
	    if (cal1.get(Calendar.HOUR_OF_DAY) >= alarm1)
	    	cal1.add(Calendar.DATE, 1);
	    cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), alarm1, 0);
	    
	    Calendar cal2 = Calendar.getInstance();
	    if (cal2.get(Calendar.HOUR_OF_DAY) >= alarm2)
	    	cal2.add(Calendar.DATE, 1);
	    cal2.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), alarm2, 0);
	    
	    Calendar cal3 = Calendar.getInstance();
	    if (cal3.get(Calendar.HOUR_OF_DAY) >= alarm3)
	    	cal3.add(Calendar.DATE, 1);
	    cal3.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), alarm3, 0);
	    
	    Calendar cal4 = Calendar.getInstance();
	    if (cal4.get(Calendar.HOUR_OF_DAY) >= alarm4)
	    	cal4.add(Calendar.DATE, 1);
	    cal4.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), alarm4, 0);

	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent1);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent2);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal3.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent3);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal4.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent4);
	}
	
	public static int nextAlarmHour(Context context){
		Calendar cal = Calendar.getInstance();
		int currenthour = cal.get(Calendar.HOUR_OF_DAY);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int alarm1=Integer.parseInt(preferences.getString("WakeTime1", "-1"));
		int alarm2=Integer.parseInt(preferences.getString("WakeTime2", "-1"));
		int alarm3=Integer.parseInt(preferences.getString("WakeTime3", "-1"));
		int alarm4=Integer.parseInt(preferences.getString("WakeTime4", "-1"));
		
		if (alarm1 == -1 || alarm2 == -1 || alarm3 == -1 || alarm4 == -1)
			return -1;
		
		if (alarm4 <= currenthour || currenthour < alarm1)
			return alarm1;
		if (alarm1 <= currenthour && currenthour < alarm2)
			return alarm2;
		if (alarm2 <= currenthour && currenthour < alarm3)
			return alarm3;
		if (alarm3 <= currenthour && currenthour < alarm4)
			return alarm4;
				
		return -1;
	}
	
	public void deleteAlarms(Context View){
		Intent i = new Intent("com.ovgu.zim.AlarmActivity");
	    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(View.getApplicationContext(), 8601, i, PendingIntent.FLAG_ONE_SHOT);
	    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(View.getApplicationContext(), 8602, i, PendingIntent.FLAG_ONE_SHOT);
	    PendingIntent pendingIntent3 = PendingIntent.getBroadcast(View.getApplicationContext(), 8603, i, PendingIntent.FLAG_ONE_SHOT);
	    PendingIntent pendingIntent4 = PendingIntent.getBroadcast(View.getApplicationContext(), 8604, i, PendingIntent.FLAG_ONE_SHOT);
	    
	    AlarmManager alarmManager = (AlarmManager) View.getSystemService(Activity.ALARM_SERVICE);
	    alarmManager.cancel(pendingIntent1);
	    alarmManager.cancel(pendingIntent2);
	    alarmManager.cancel(pendingIntent3);
	    alarmManager.cancel(pendingIntent4);	    
	}
}
