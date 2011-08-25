package com.test;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle b = intent.getExtras();
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Intent open = new Intent(context, TestAlarmActivity.class);
		open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent appIntent = PendingIntent.getActivity(context, 0, open, 0);
		
		Notification notify = new Notification();
		notify.icon = R.drawable.icon;
		notify.tickerText = "Notification text";
		notify.defaults = Notification.DEFAULT_ALL;
		notify.flags = Notification.FLAG_AUTO_CANCEL;
		notify.setLatestEventInfo(context, "Test Alarm", b.getString(TestAlarmActivity.ALARM_CONTENT), appIntent);
		
		manager.notify(0, notify);

		// Clear this alarm in settings
		SharedPreferences settings = context.getSharedPreferences("alarm", 0);
		String serial = b.getInt(TestAlarmActivity.SERIAL_NUMBER)+"";
		settings.edit().remove(serial).commit();
		settings.edit().remove(serial+TestAlarmActivity.ALARM_CONTENT).commit();
	}

}
