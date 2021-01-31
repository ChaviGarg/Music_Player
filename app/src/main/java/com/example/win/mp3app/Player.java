package com.example.win.mp3app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Player extends Activity
{

	TextView til;
	Handler hl;
	Runnable rn;
	TextView total,current;
	SeekBar bar;
	Button b1;
	DbsHandler dbs;
	SQLiteDatabase db;
	WebView wb;
	SharedPreferences sp;
	int j=0;
	int len=0;
	int k=0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		dbs=new DbsHandler(this);
		wb=(WebView)findViewById(R.id.wb);
		wb.loadUrl("file:///android_asset/data.html");
		bar=(SeekBar)findViewById(R.id.sk);
		til=(TextView)findViewById(R.id.tl);
		b1=(Button)findViewById(R.id.b1);
		total=(TextView)findViewById(R.id.total);
		current=(TextView)findViewById(R.id.current);
		sp=getSharedPreferences("pp",0);
		til.setText(sp.getString("name",null));
		k=Integer.valueOf(sp.getString("id",null));
		hl=new Handler();
		rn=new Runnable()
		{

			@Override
			public void run()
			{
				getPlayerInfo();
				hl.postDelayed(this,1000);
			}
			
			
		};
		hl.post(rn);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar p1, int p2, boolean p3)
				{
					
					if(p3==true)
					{
						Serv.mp.seekTo(p2);
					}
					
					
				}

				@Override
				public void onStartTrackingTouch(SeekBar p1)
				{
					
					// TODO: Implement this method
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}
				
			
			
		});
		getlen();
		
	}
	public void getlen()
	{
		db=dbs.getReadableDatabase();
		Cursor c=db.rawQuery("select * from mlist",null);
		len=c.getCount();
	}
	public void getPlayerInfo()
	{
		if(Serv.mp.isPlaying())
		{
			b1.setText("Pause");
			b1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						Serv.mp.pause();
					}
					
				
			});
		}
		else
		{
			b1.setText("Play");
			b1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						Serv.mp.start();
						// TODO: Implement this method
					}
					
				
			});
		}
		if(Serv.mp.isPlaying())
		{
			bar.setMax(Serv.mp.getDuration());
			bar.setProgress(Serv.mp.getCurrentPosition());
			float total1=Serv.mp.getDuration()/1000;
			float tl=total1/60;
			SimpleDateFormat dp=new SimpleDateFormat("HH:mm");
			float current1=Serv.mp.getCurrentPosition();
			int seconds1 =(int)(((Serv.mp.getDuration() %(1000*60*60))%(1000*60))/1000);
			int minutes1 =(int)((Serv.mp.getDuration() %(1000*60*60))/(1000*60));
			int seconds =(int)(((Serv.mp.getCurrentPosition() %(1000*60*60))%(1000*60))/1000);
			int minutes =(int)((Serv.mp.getCurrentPosition() %(1000*60*60))/(1000*60));
			total.setText(String.valueOf(minutes1)+":"+String.valueOf(seconds1));
			current.setText(String.valueOf(minutes)+":"+String.valueOf(seconds));
		}
		
	}

	@Override
	protected void onDestroy()
	{
		hl.removeCallbacks(rn);
		
		// TODO: Implement this method
		super.onDestroy();
	}
	public void bass(View v)
	{
		BassBoost bss=new BassBoost(0,Serv.mp.getAudioSessionId());
		bss.setEnabled(true);
		bss.setStrength((short)1000);
		Toast.makeText(getApplicationContext(),"Bass",Toast.LENGTH_LONG).show();
	}
	public void add(View v)
	{
		String id=sp.getString("id",null);
		String name=sp.getString("name",null);
		String path=sp.getString("path",null);
		String art=sp.getString("art",null);
		String date=sp.getString("date",null);
		ContentValues vl=new ContentValues();
		db=dbs.getWritableDatabase();
		vl.put("id",id);
		vl.put("title",name);
		vl.put("path",path);
		vl.put("artist",art);
		vl.put("date",date);
		long p=db.insert("mlist3",null,vl);
		if(p !=-1)
		{
			Toast.makeText(getApplicationContext(),"Added in PlayList",Toast.LENGTH_LONG).show();
			//Snackbar.make(null,"Added",Snackbar.LENGTH_LONG).show();
		}
		
	}
	public void repeat(View v)
	{
		if(j==0)
		{

			Serv.mp.setLooping(true);
			j++;
			Toast.makeText(getApplicationContext(),"Repeated",Toast.LENGTH_LONG).show();
		}
		else
		{
			Serv.mp.setLooping(false);
			j=0;
			Toast.makeText(getApplicationContext(),"All",Toast.LENGTH_LONG).show();
		}
		}
       public void next(View v)
	   {
		   db=dbs.getReadableDatabase();
		   if(k<=len)
		   {
                k++;
		   }
		   else
		   {
             k=1;
		   }
		   Cursor c=db.rawQuery("select * from mlist where id='"+k+"'",null);
		   while(c.moveToNext())
		   {
			   String path=c.getString(c.getColumnIndex("path"));
			   String title=c.getString(c.getColumnIndex("title"));
			   til.setText(title);
			   Intent i=new Intent(this,Serv.class);
			   i.putExtra("path",path);
			   stopService(i);
			   startService(i);
		   }
	   }
	public void pre(View v)
	{
		db=dbs.getReadableDatabase();
		if(k>=1)
		{
			k--;
		}
		else
		{
			k=1;
		}
		Cursor c=db.rawQuery("select * from mlist where id='"+k+"'",null);
		while(c.moveToNext())
		{
			String path=c.getString(c.getColumnIndex("path"));
			String title=c.getString(c.getColumnIndex("title"));
			til.setText(title);
			Intent i=new Intent(this,Serv.class);
			i.putExtra("path",path);
			stopService(i);
			startService(i);
		}
	}
	}
	

