package com.example.win.mp3app;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.text.*;
import java.util.*;

public class Frm2 extends Fragment
{

	DbsHandler dbs;
	SQLiteDatabase db;
	ListView list;
    ArrayList name=new ArrayList();
	ArrayList artist=new ArrayList();
	ArrayList path=new ArrayList();
	ArrayList ids=new ArrayList();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		dbs=new DbsHandler(getActivity());
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.frm2,null,true);
		list=(ListView)v.findViewById(R.id.lst);
		getData();
		return v;
	}
public void getData()
{
	final Calendar cl=Calendar.getInstance();
	SimpleDateFormat dc=new SimpleDateFormat("dd/MM/yyyy");
	final String dt=dc.format(cl.getTime());
	db=dbs.getReadableDatabase();
	Cursor c=db.rawQuery("select * from mlist2 ORDER BY date DESC LIMIT 5",null);
	while(c.moveToNext())
	{
		name.add(c.getString(c.getColumnIndex("title")));
		path.add(c.getString(c.getColumnIndex("path")));
		artist.add(c.getString(c.getColumnIndex("artist")));
		ids.add(c.getString(c.getColumnIndex("id")));
	}
	Task dp=new Task(getActivity(),name,artist);
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
				String sts=artist.get(p3).toString();
				String id=ids.get(p3).toString();
				//Toast.makeText(getActivity(),vl,Toast.LENGTH_LONG).show();
				Intent i=new Intent(getActivity(),Serv.class);
				i.putExtra("path",vl);
				getActivity().stopService(i);
				getActivity().startService(i);
				Intent j=new Intent(getActivity(),Player.class);
				//getActivity().startActivity(j);
				Cursor c2=db.rawQuery("select * from mlist2 where id='"+id+"'",null);
				if(c2.getCount()>0)
				{
					db.execSQL("update mlist2 set date='"+dt+"' where id='"+id+"'");
					//Toast.makeText(getActivity(),"present",Toast.LENGTH_LONG).show();
				}
				else
				{
					/*
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
					*/
				}
			}



		});
	
}
	
	
	private class Task extends ArrayAdapter
	{
		TextView title0,nm;
		ImageView img;
		private ArrayList name0;
		private ArrayList num0;
		public Task(Context ctx,ArrayList name0,ArrayList num0)
		{
			super(ctx,R.layout.contactlist2,name0);
			this.name0=name0;
			this.num0=num0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			LayoutInflater lv=getActivity().getLayoutInflater();
			View v=lv.inflate(R.layout.contactlist2,null,true);
			title0=(TextView)v.findViewById(R.id.title);
			title0.setText(name0.get(position).toString());
			nm=(TextView)v.findViewById(R.id.num1);
			nm.setText(num0.get(position).toString());
			//img.setImageURI(Uri.parse(num0.get(position).toString()));
			
			return v;
		}

	}

}
