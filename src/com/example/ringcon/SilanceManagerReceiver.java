package com.example.ringcon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;

import com.example.ringcon.structure.Rule;

public class SilanceManagerReceiver extends BroadcastReceiver {

	final public static String KEY_RULE = "rule";

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Silance control");

		wakeLock.acquire();

		Bundle extras = intent.getExtras();

		Rule rule;
		if(extras != null && extras.containsKey(KEY_RULE)){
			rule = (Rule) extras.getSerializable(KEY_RULE);
		}

		//TODO: add switcher and checker for previous resource
		AudioManager am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

		//Release the lock
		wakeLock.release();
	}

	public void setRule(Context context, Rule rule) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		
		Intent intent = new Intent(context, SilanceManagerReceiver.class);
		intent.putExtra(KEY_RULE, rule);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, rule.getId(), intent, 0);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, rule.getStartDate() * 1000, AlarmManager.INTERVAL_DAY * 7 , pendingIntent);
	}

	public void CancelAlarm(Context context, Rule rule) {
		Intent intent = new Intent(context, SilanceManagerReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, rule.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
