package com.example.ringcon;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;

import com.example.ringcon.structure.Rule;
import com.example.ringcon.utils.DateUtils;

public class SilenceManagerReceiver extends BroadcastReceiver {

	final public static String KEY_RULE = "rule";
	final public static String KEY_TIME = "time";
	final public static String KEY_START = "start";
	final public static String KEY_RULE_ID = "rule_id";
	
	final public static String KEY_PREFERENCES = "ring_prefs";
	final public static String KEY_VOLUME_LEVEL = "volume";
	

	@SuppressLint("Wakelock")
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Silance control");

		wakeLock.acquire();

		AudioManager am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

		Bundle extras = intent.getExtras();

		if(extras != null && extras.containsKey(KEY_START)){
			boolean start = extras.getBoolean(KEY_START);
			SharedPreferences prefs = context.getApplicationContext()
					.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
			int volumeLevel = am.getStreamVolume(AudioManager.STREAM_RING);

			if (start) {
				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				prefs.edit().putInt(KEY_VOLUME_LEVEL, volumeLevel).commit();
			} else {
				if (am.getStreamVolume(AudioManager.STREAM_RING) == 0) {
					am.setStreamVolume(AudioManager.STREAM_RING, volumeLevel, 0);
				}
			}
		}

		//Release the lock
		wakeLock.release();
	}

	public void setRule(Context context, Rule rule) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		ArrayList<Integer> ruleDays = DateUtils.getWeekDays(rule.getWeekdays());
		
		// get today's date
		Calendar calendarToday = Calendar.getInstance();

		for (int i = 0; i < 7; i++)
			calendarToday.add(Calendar.DAY_OF_YEAR, i);
			int todaysDay = calendarToday.get(Calendar.DAY_OF_WEEK);
			if (ruleDays.indexOf(todaysDay) >= 0) {
				Intent startIntent = new Intent(context, SilenceManagerReceiver.class);
				startIntent.putExtra(KEY_START, true);
				PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, rule.getId(), startIntent, 0);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, rule.getStartDate(), AlarmManager.INTERVAL_DAY * 7 , startPendingIntent);

				Intent finishIntent = new Intent(context, SilenceManagerReceiver.class);
				finishIntent.putExtra(KEY_START, false);
				PendingIntent finishPendingIntent = PendingIntent.getBroadcast(context, rule.getId(), finishIntent, 0);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, rule.getFinishDate(), AlarmManager.INTERVAL_DAY * 7 , finishPendingIntent);
			}
	}

	public void CancelAlarm(Context context, Rule rule) {
		Intent intent = new Intent(context, SilenceManagerReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, rule.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
	
	//TODO: add cancelrule + setrule after edit;
}
