package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.layout;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


/**
 * THIS IS THE OLD MAIN MENU - DEPRECATED BY MAINMENU
 * @author Ade
 *
 */
public class TabMenu extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabmenu);
				
		TabHost host = getTabHost();
		TabHost.TabSpec tabspec; // Reusable Tabspec for each tab
		
		Intent intent; // Reusable/shared Intent object
		
		
		//TAB1 - null is where thumb image should be
		intent = new Intent().setClass(this, SelectYear.class);
		tabspec = host.newTabSpec("selectDeg").setIndicator("T1",null).setContent(intent);
		host.addTab(tabspec);
		
	    //TAB2
		intent = new Intent().setClass(this, SelectYear.class);
		tabspec = host.newTabSpec("navmap").setIndicator("T2",null).setContent(intent);	    
		host.addTab(tabspec);
		
		//TAB3
		intent = new Intent().setClass(this, SelectYear.class);
		tabspec = host.newTabSpec("navmap").setIndicator("T3",null).setContent(intent);	    
		host.addTab(tabspec);

		//TAB4
		intent = new Intent().setClass(this, SelectYear.class);
		tabspec = host.newTabSpec("navmap").setIndicator("T4",null).setContent(intent);	    
		host.addTab(tabspec);
		
	   // host.setCurrentTab(1);
		
		//TAB2
//		intent = new Intent().setClass(this, SelectYear.class);
	//	tabspec = host.newTabSpec("selectYear").setIndicator("Year",res.getDrawable(R.drawable.tab_grey)).setContent(intent);
		//host.addTab(tabspec);
		
		
	}
	
}
