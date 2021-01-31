package com.example.win.mp3app;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
public class MainActivity extends AppCompatActivity 
{
	DbsHandler dbs;
	TabLayout tabs;
	ViewPager vw;
	FloatingActionButton b1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		tabs=(TabLayout)findViewById(R.id.tab);
		dbs=new DbsHandler(this);
		b1=(FloatingActionButton)findViewById(R.id.b1);
		//b1.setBackgroundColor(Color.parseColor("#fd0000"));
		vw=(ViewPager)findViewById(R.id.vw);
		tabs.setTabTextColors(Color.BLACK,Color.GRAY);
		//tabs.setSelectedTabIndicatorColor(Color.parseColor("#fd000"));
		TabLayout.Tab tbl=tabs.newTab();
		tbl.setText("Login");

		tabs.addTab(tbl);
		
		TabLayout.Tab tbl2=tabs.newTab();
		tbl2.setText("Sign Up");
		tabs.addTab(tbl2);
		
		TabLayout.Tab tbl3=tabs.newTab();
		tbl3.setText("Home");
		tabs.addTab(tbl3);
		Pager fp=new Pager(getSupportFragmentManager());
		vw.setAdapter(fp);
		tabs.setupWithViewPager(vw);
		b1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i=new Intent(MainActivity.this,Player.class);
					startActivity(i);
				}
				
			
		});
    }
	private class Pager extends FragmentPagerAdapter
	{

		@Override
		public Fragment getItem(int p1)
		{
			Fragment frm=null;
			if(p1==0)
			{
				frm=new Frm1();
			}
			else if(p1==1)
			{
				frm=new Frm2();
			}
			else if(p1==2)
			{
				frm=new Frm3();
			}
			return frm;
		}

		@Override
		public int getCount()
		{
			// TODO: Implement this method
			return 3;
		}
		
		public Pager(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			String title="";
			if(position==0)
			{
				title="Songs";
			}
			else if(position==1)
			{
				title="Recently";
			}
			else if(position==2)
			{
				title="PlayList";
			}
			// TODO: Implement this method
			return title;
		}
		
	}
}
