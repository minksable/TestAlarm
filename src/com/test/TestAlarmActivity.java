package com.test;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class TestAlarmActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Context mContext;
	
	public final static String SERIAL_NUMBER = "sn";
	public final static String ALARM_CONTENT = "content";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext = this;
  
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setAlarmTime();
			}        	
        });
    }
    
    private void setAlarmTime(){
    	final Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
    	 new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
 			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
 				time.set(Calendar.HOUR_OF_DAY, hourOfDay);
 				time.set(Calendar.MINUTE, minute);
 				time.set(Calendar.SECOND, 0);
 				
 				SharedPreferences settings = getSharedPreferences("alarm", 0);
 				// When there's no alarm in settings, we set serial back to 0
				int serial = settings.getAll().size() == 1 ? 0: settings.getInt(SERIAL_NUMBER, 0);
				String content = "Alarm at "+time.getTime().toString();
				
				Intent intent = new Intent(mContext, AlarmReceiver.class);
				Bundle b = new Bundle();
				b.putInt(SERIAL_NUMBER, serial);
				b.putString(ALARM_CONTENT, content);
				intent.putExtras(b);
				 
				PendingIntent pending = PendingIntent.getBroadcast(mContext, serial, intent, 0);
				AlarmManager manager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
				manager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pending);

				// Keep alarm time and content also in settings
				SharedPreferences.Editor editor = settings.edit();
				editor.putLong(serial+"", time.getTimeInMillis());
				editor.putString(serial+ALARM_CONTENT, content);
				editor.putInt(SERIAL_NUMBER, ++serial);
				editor.commit();
 			}
 		}, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), false).show();
    }
}