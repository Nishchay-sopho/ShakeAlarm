package com.example.nishchay.shake_alarm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//This class runs what happens when the alarm goes off


public class Alert extends Activity  {
	
	//Makes sure that shaking the device will not remove multiple entries
	boolean shakeFlag = false;
	
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	//MediaPlayer media_song;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		
		
		//This should help treat the symptoms of ghost alarms
		//Checks to make sure there is actually an alarm set, if there is not it closes.
		if (Set_Alarm.getAlarmSentenceLength() < 1 ){
			System.exit(0);
		}
		else{
		
		//Changes the screen to the alarm
			// int r=R.layout.alarm_pop_up.xml
		 setContentView(R.layout.alarm_pop_up);
		 
		 
		 
		 
		 //accelerometer set up
		 mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		 mSensorListener = new ShakeEventListener(); 
	       
		 
		 //Makes a bunch of awful noise.
		 //It takes the user's current phone settings for alarm and plays it
		 //If no alarm is currently set it falls back to the ringtone and if that fails
		 //it resorts to using the notification sound



		 Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		 
	     if(alert == null){
	   
	         alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
	         
	         if(alert == null){  
	        	 
	             alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);               
	         }
	     }
	     
	     final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alert);
	     
	   
		   r.play();

			//Tried to put in notification but the app closed as soon as
			// it tries to launch the notification the app closes


			/*NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Intent intent_main_activity=new Intent(this.getApplicationContext(),Set_Alarm.class);
			PendingIntent pending_intent_main_activity =PendingIntent.getActivity(this,0,intent_main_activity,0);


			Notification notification_popup = new Notification.Builder(this)
					.setContentTitle("An alarm going to go off !")
					.setContentText("Click me")
					.setContentIntent(pending_intent_main_activity)
					.setAutoCancel(true)
					.build();

			//set notification call command
			notify_manager.notify(0,notification_popup);*/

		   //Sets up the listener so that if you shake the phone the alarm will stop
		   mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

				 public void onShake() {
				    //Stops the terrible noise
				    r.stop();
				    
				    if (!shakeFlag){
				    	
				    	if (Set_Alarm.getAlarmSentenceLength() > 0) {
				    //These remove the alarm from the current alarms list once it has gone off
				    Set_Alarm.removeAlarmSentence(0);
			           
			        Set_Alarm.removeCampers(0);
				    	}
			        
			        shakeFlag = true;
				    }
			        //Closes the activity that pops up
				 	    finish();
				      }
		   
		   });}}
		
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener,
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_UI);
	  }

	  @Override
	  protected void onPause() {
	    mSensorManager.unregisterListener(mSensorListener);
	    super.onPause();
	  }
	  
	  @Override
		 public void onBackPressed(){
			 
		 }

}
