package com.example.win.mp3app;
import android.content.*;
import android.database.sqlite.*;

public class DbsHandler extends SQLiteOpenHelper
{

	SQLiteDatabase db;
	public DbsHandler(Context ctx)
	{
		super(ctx,"mplayer",null,1);
		table();
	}
	@Override
	public void onCreate(SQLiteDatabase p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int p2, int p3)
	{
		// TODO: Implement this method
	}
	public void table()
	{
		db=this.getWritableDatabase();
		//db.execSQL("DROP TABLE IF EXISTS mlist");
		//db.execSQL("DROP TABLE IF EXISTS mlist2");
		db.execSQL("CREATE TABLE IF NOT EXISTS mlist (id INTEGER PRIMARY KEY,title varchar,path varchar,artist varchar,date varchar);");
		db.execSQL("CREATE TABLE IF NOT EXISTS mlist2 (id INTEGER,title varchar,path varchar,artist varchar,date varchar);");
		db.execSQL("CREATE TABLE IF NOT EXISTS mlist3 (id INTEGER,title varchar,path varchar,artist varchar,date varchar);");
	}
}
