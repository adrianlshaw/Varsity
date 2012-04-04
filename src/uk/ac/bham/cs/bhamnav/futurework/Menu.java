package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.layout;

import uk.ac.bham.cs.bhamnav.NavMap;
import uk.ac.bham.cs.bhamnav.Start;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends ListActivity implements OnClickListener{

	/*
	 * Until the whole school can be done I can only do single honour students
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		final String[] SELECTION = new String[]{
				"1. NavMap", 
				"2. Select Degree", 
				"3. TabMenu",
				"4. TestNav",
				"5. TestNav2",
				"6. Calendar",
				"7. Wizard",
				"8. Start"
				,"9. Mininav"
				
			};
			
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.mainmenu, SELECTION));
		
//		ImageView imageview = R.layout.menu.getViewById(splash);
		
		ListView listview = getListView();
		
		listview.setTextFilterEnabled(true);

		listview.setOnItemClickListener(new OnItemClickListener(){
			
			/**
			 * The Toast class has static methods that you can use
			 * a bit like JOptionPane in Java SE
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				
				if (position==0){
//					Intent intent2 = new Intent(Menu.this, NavMap.class);
//					Menu.this.startActivity(intent2);
				}
				
				else if (position==1){
					Intent intent = new Intent(Menu.this, SelectDegree.class);					
					Menu.this.startActivity(intent);
				}
				
				else if (position==2){
					Intent intent3 = new Intent(Menu.this,TabMenu.class);					
					Menu.this.startActivity(intent3);
				}
				
				else if (position==3){
		//			Intent intent4 = new Intent(Menu.this,TestNav.class);					
		//			Menu.this.startActivity(intent4);
				}
				else if (position == 4){
					Intent intent5 = new Intent(Menu.this, NavMap.class);					
					Menu.this.startActivity(intent5);
				}
				else if(position == 5)
				{
					Intent intent6 = new Intent(Menu.this,BCalendar.class);
					Menu.this.startActivity(intent6);
				}
				else if(position == 6)
				{
					Intent intent7 = new Intent(Menu.this, SelectDegree.class);
					Menu.this.startActivity(intent7);
				}
				else if(position==7){
					Intent intent7 = new Intent(Menu.this, Start.class);
					Menu.this.startActivity(intent7);					
				}
				
				else {
					Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		
	}
}
