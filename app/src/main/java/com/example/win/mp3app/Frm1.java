package com.example.win.mp3app;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Frm1 extends Fragment
{
	
	ListView list;
	DbsHandler dbs;
	SQLiteDatabase db;
	String dt="";
	ArrayList name=new ArrayList();
	ArrayList num=new ArrayList();
	ArrayList path=new ArrayList();
	ArrayList ids=new ArrayList();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		dbs=new DbsHandler(getActivity());
		View v=inflater.inflate(R.layout.frm1,null,true);
		list=(ListView)v.findViewById(R.id.lst);
		getData();
		return v;
	}
	public void getData()
	{
		final Calendar cl=Calendar.getInstance();
		SimpleDateFormat dc=new SimpleDateFormat("dd/MM/yyyy");
		dt=dc.format(cl.getTime());
		//Toast.makeText(getActivity(),dt,Toast.LENGTH_LONG).show();
		db=dbs.getWritableDatabase();
		db.execSQL("delete from mlist");
		ContentValues vl=new ContentValues();
		Cursor c=getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
		while(c.moveToNext())
		{
			String title=c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			String path2=c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

			String art=c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			
			vl.put("title",title);
			vl.put("path",path2);
			vl.put("artist",art);
			//vl.put("date",dt);
			db.insert("mlist",null,vl);
		}
		getlist();
	
	}
	public void getlist()
	{

		name.clear();
		num.clear();
		path.clear();
		db=dbs.getReadableDatabase();
		Cursor c=db.rawQuery("select * from mlist",null);
		while(c.moveToNext())
		{
			name.add(c.getString(c.getColumnIndex("title")));
			num.add(c.getString(c.getColumnIndex("artist")));
			path.add(c.getString(c.getColumnIndex("path")));
			ids.add(c.getString(c.getColumnIndex("id")));
		}
		Task dp=new Task(getActivity(),name,num);
		//ArrayAdapter dp=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,name);
		list.setAdapter(dp);
		list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					ContentValues vls=new ContentValues();
					db=dbs.getReadableDatabase();
					String til=name.get(p3).toString();
					String pth=path.get(p3).toString();
					String vl=path.get(p3).toString();
					String sts=num.get(p3).toString();
					String id=ids.get(p3).toString();
					//Toast.makeText(getActivity(),vl,Toast.LENGTH_LONG).show();
					Intent i=new Intent(getActivity(),Serv.class);
					i.putExtra("path",vl);
					getActivity().stopService(i);
					getActivity().startService(i);
					Intent j=new Intent(getActivity(),Player.class);
					//getActivity().startActivity(j);
					SharedPreferences.Editor sp=getActivity().getSharedPreferences("pp",getActivity().MODE_PRIVATE).edit();
					sp.putString("id",id);
					sp.putString("name",til);
					sp.putString("path",pth);
					sp.putString("art",sts);
					sp.putString("date",dt);

					sp.commit();
					Cursor c2=db.rawQuery("select * from mlist2 where id='"+id+"'",null);
					if(c2.getCount()>0)
					{
						db.execSQL("update mlist2 set date='"+dt+"' where id='"+id+"'");
						//Toast.makeText(getActivity(),"present",Toast.LENGTH_LONG).show();
					}
					else
					{
						vls.put("id",id);
						vls.put("title",til);
						vls.put("path",pth);
						vls.put("artist",sts);
						vls.put("date",dt);
						long p=db.insert("mlist2",null,vls);
						if(p !=-1)
						{
							Toast.makeText(getActivity(),"done",Toast.LENGTH_LONG).show();
						}
					}
				}



			});
	}
	private class Task extends ArrayAdapter
	{
		TextView title0,n0;
		ImageView img;
		private ArrayList name0;
		private ArrayList num0;
         public Task(Context ctx,ArrayList name0,ArrayList num0)
		 {
			 super(ctx,R.layout.contactlist,name0);
			 this.name0=name0;
			 this.num0=num0;
		 }
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater lv=getActivity().getLayoutInflater();
			View v=lv.inflate(R.layout.contactlist,null,true);
			title0=(TextView)v.findViewById(R.id.title);
			//img=(ImageView)v.findViewById(R.id.mp3);
			title0.setText(name0.get(position).toString());
			n0=(TextView)v.findViewById(R.id.num);
			n0.setText(num0.get(position).toString());
			//img.setImageURI(Uri.parse(num0.get(position).toString()));
			return v;
		}
		
	}
	
}
