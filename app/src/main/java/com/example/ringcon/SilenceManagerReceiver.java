package com.example.ringcon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;

import com.example.ringcon.sql.SQLiteAdapter;
import com.example.ringcon.structure.Rule;
import com.example.ringcon.utils.DateUtils;

public class SilenceManagerReceiver extends BroadcastReceiver {

	final public static String KEY_RULE = "rule";
	final public static String KEY_TIME = "time";
	final public static String KEY_START = "start";
	final public static String KEY_RULE_ID = "rule_id";
	final public static String KEY_MODE = "mode";
	final public static String KEY_PREFERENCES = "ring_prefs";
	final public static String KEY_VOLUME_LEVEL = "volume";
	
	private OnRuleRefreshListener listener;
	private Context context;

	public OnRuleRefreshListener getListener() {
		return listener;
	}

	@SuppressLint("Wakelock")
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		PowerManager powerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "Silance control");
		wakeLock.acquire();

		AudioManager am = (AudioManager) context.getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);

		Bundle extras = intent.getExtras();

		if (extras != null && extras.containsKey(KEY_START)
				&& extras.containsKey(KEY_RULE_ID)) {
			
			long id = extras.getLong(KEY_RULE_ID);
			Rule rule = new SQLiteAdapter(context).getRule(id);
			if (!rule.isActive()) {
				cancelAlarm(context, rule);
				wakeLock.release();
				return;
			}

			boolean start = extras.getBoolean(KEY_START);
			SharedPreferences prefs = context
					.getApplicationContext()
					.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
			int volumeLevel = prefs.getInt(KEY_VOLUME_LEVEL, 0);

			if (start) {
				volumeLevel = am.getStreamVolume(AudioManager.STREAM_RING);
				am.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
				if (rule.getMode()==1){
					am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				}
				

				//
				// AudioManager.RINGER_MODE_SILENT;

				// am.setStreamVolume(streamType, index, flags)
				prefs.edit().putInt(KEY_VOLUME_LEVEL, volumeLevel).commit();
			} else {
				if (am.getStreamVolume(AudioManager.STREAM_RING) == 0) {
					am.setStreamVolume(AudioManager.STREAM_RING, volumeLevel, 0);
				}
				if (rule.getMode()==1){
					am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
				
				if (rule.getWeekdays() == 0) {
					rule.setActive(false);
					new SQLiteAdapter(context).editRule(id, rule);
					if (listener != null) {
						listener.refreshRuleList();
					}
				}
				
				context.sendBroadcast(new Intent(MainActivity.ACTION_RULELIST_UPDATE));
			}

		}
		

		// Release the lock
		wakeLock.release();
	}


	public void setRule(Context context, Rule rule) {
		if (!rule.isActive()) {
			cancelAlarm(context, rule);
			return;
		}
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		ArrayList<Integer> ruleDays = new DateUtils(context).getWeekDays(rule
				.getWeekdays());

		// get today's date
		Calendar calendarToday = Calendar.getInstance();
		long currentTime = calendarToday.getTimeInMillis();

		Calendar now = Calendar.getInstance();

		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);

		if (ruleDays.size() == 0) {

			// start rule action
			long startdate = now.getTimeInMillis() + rule.getStartDate();
			if (startdate < currentTime) {
				startdate += AlarmManager.INTERVAL_DAY;
			}
			// Log.d("RingCon","Start  "+String.valueOf(startdate));
			setAlarm(context, rule, false, startdate, true);
			// finish rule action
			long finishdate = startdate - rule.getStartDate()
					+ rule.getFinishDate();
			if (rule.getFinishDate() < rule.getStartDate()) {
				finishdate += AlarmManager.INTERVAL_DAY;
			}
			// Log.d("RingCon","finish "+String.valueOf(finishdate));
			setAlarm(context, rule, false, finishdate, false);

		} else {
			for (int i = 0; i < 7; i++) {
				calendarToday.add(Calendar.DAY_OF_YEAR, 1);
				int todaysDay = calendarToday.get(Calendar.DAY_OF_WEEK);
				if (ruleDays.indexOf(todaysDay) >= 0) {
					// start rule action
					long startdate = now.getTimeInMillis()
							+ rule.getStartDate();
					if (startdate < currentTime) {
						startdate += AlarmManager.INTERVAL_DAY * 7;
					}
					setAlarm(context, rule, true, startdate, true);
					// finish rule action
					long finishdate = startdate - rule.getStartDate()
							+ rule.getFinishDate();
					if (rule.getFinishDate() < rule.getStartDate()) {
						finishdate += AlarmManager.INTERVAL_DAY;
					}
					setAlarm(context, rule, true, finishdate, false);
				}
			}
		}
	}

	public void setAlarm(Context context, Rule rule, Boolean repeat, long time,
			Boolean param) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, SilenceManagerReceiver.class);
		intent.putExtra(KEY_START, param);
		intent.putExtra(KEY_RULE_ID, rule.getId());

		alarmManager.setRepeating(
				AlarmManager.RTC_WAKEUP,
				time,
				AlarmManager.INTERVAL_DAY * 7 * (repeat ? 1 : 0),
				PendingIntent.getBroadcast(context, (int) rule.getId()
						* (param ? 1 : -1), intent, 0));

	}

	public void cancelAlarm(Context context, Rule rule) {
		Intent intent = new Intent(context, SilenceManagerReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context,
				(int) rule.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

}
