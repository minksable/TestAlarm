package com.test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences settings = context.getSharedPreferences("alarm", 0);
		int serial = settings.getInt(TestAlarmActivity.SERIAL_NUMBER, 0);
		for(int i=0; i<serial; i++){
			// If the alarm i is not cleared
			if(settings.contains(i+"")){
				long time = settings.getLong(i+"", 0);
				String content = settings.getString(i+TestAlarmActivity.ALARM_CONTENT, "");
				Date current = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTime();
				// Here I don't want to set an expired alarm
				if(current.before(new Date(time))){
					Intent alarmIntent = new Intent(context, AlarmReceiver.class);
					Bundle b = new Bundle();
					b.putInt(TestAlarmActivity.SERIAL_NUMBER, i);
					b.putString(TestAlarmActivity.ALARM_CONTENT, content);
					alarmIntent.putExtras(b);
					PendingIntent pending = PendingIntent.getBroadcast(context, i, alarmIntent, 0);					
					AlarmManager manager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
					manager.set(AlarmManager.RTC_WAKEUP, time, pending);
				}
				// And I want to clear the expired alarm
				else{
					settings.edit().remove(i+"").commit();
					settings.edit().remove(i+TestAlarmActivity.ALARM_CONTENT).commit();
				}
			}
		}
	}
}
