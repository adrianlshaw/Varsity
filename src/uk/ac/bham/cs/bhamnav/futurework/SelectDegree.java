package uk.ac.bham.cs.bhamnav.futurework;


import org.me.nav.R;
import org.me.nav.R.layout;

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

public class SelectDegree extends ListActivity implements OnClickListener{

	/*
	 * Until the whole school can be done I can only do single honour students
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		final String[] DEGREE_PROGRAMMES = new String[]{
				"Computer Science", 
				"Computer Science with AI", 
				"Computer Science with SE [BEng]",
				"Computer Science with BM ",
				"Computer Science with SE [MEng]",
				"Computer Science MSc",
				"Adanced Computer Science MSc",
				"Computer Security MSc",
				"Natural Computation MSc",
				"Intelligent Systems Engineering MSc",
				"Internet Software Systems",
				"Other (Joint Honours)"
			};
			
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.selectdegree, DEGREE_PROGRAMMES));

		ListView listview = getListView();
		listview.setTextFilterEnabled(true);

		listview.setOnItemClickListener(new OnItemClickListener(){
			
			/**
			 * The Toast class has static methods that you can use
			 * a bit like JOptionPane in Java SE
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				
				
				Intent intent = new Intent(SelectDegree.this, SelectYear.class);
				
				if(position==0){
					intent.putExtra("degree", "CS");
				}
				else if(position==1){
					intent.putExtra("degree", "AI");
				}
				else if(position==2){
					intent.putExtra("degree", "CSSE");
				}
				else if(position==3){
					intent.putExtra("degree", "CSBM");
				}
				if(position==4){
					intent.putExtra("degree", "MCSSE");
				}
				if(position==5){
					intent.putExtra("degree", "MSE");
				}
				if(position==6){
					intent.putExtra("degree", "MACS");
				}
				if(position==7){
					intent.putExtra("degree", "MCS");
				}
				if(position==8){
					intent.putExtra("degree", "MNC");
				}
				if(position==9){
					intent.putExtra("degree", "OTHER");
				}

				if (position>=0 && position<4){
					SelectDegree.this.startActivity(intent);
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