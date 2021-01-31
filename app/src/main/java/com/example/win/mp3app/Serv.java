package com.example.win.mp3app;
import android.app.*;
import android.os.*;
import android.content.*;
import android.media.*;

public class Serv extends Service
{

	public static MediaPlayer mp;
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}
    
	@Override
	public void onCreate()
	{
		
		super.onCreate();
	}
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		mp=new MediaPlayer();
		String path=intent.getStringExtra("path");
		try
		{
			mp.setDataSource(path);
			//mp.setScreenOnWhilePlaying(true);
			mp.prepare();
		}
		catch(Exception e)
		{
			
		}
		if(mp.isPlaying()==true)
		{
			
			
			
			
		}
		else
		{
			mp.start();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		mp.reset();
		// TODO: Implement this method
		super.onDestroy();
	}

	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		Intent i=new Intent(this,Serv.class);
		PendingIntent pi=PendingIntent.getService(this,1,i,PendingIntent.FLAG_ONE_SHOT);
		AlarmManager al=(AlarmManager)getSystemService(ALARM_SERVICE);
		al.set(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime()+1000,pi);
		super.onTaskRemoved(rootIntent);
	}
	
	
	
}
