package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.layout;

import uk.ac.bham.cs.bhamnav.NavMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Year class
 * 
 * @author axs91
 *
 */
public class SelectYear extends ListActivity{
	
	String degree;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		Intent prevIntent = getIntent();
		
		degree = prevIntent.getStringExtra("degree");
		String[] YEAR;
		
		if(degree.equals("CSS")){
			YEAR = new String[]{"Year 1", "Year 2","Year 3","Year 4"};
		}
		else {
			YEAR = new String[]{"Year 1","Year 2","Year 3"};
		}
		
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.selectyear, YEAR));
		
		ListView listview = getListView();
		listview.setTextFilterEnabled(true);		

		listview.setOnItemClickListener(new OnItemClickListener(){
			
			/**
			 * The Toast class has static methods that you can use
			 * a bit like JOptionPane in Java SE
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Intent intent = null;
				
				if(degree.equals("CS")){
					if (position==0){
						intent = new Intent(SelectYear.this, FirstYearChoices.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==1){
						 intent = new Intent(SelectYear.this, SecondYearChoicesSem1.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==2){
						 intent = new Intent(SelectYear.this, ThirdYearChoicesSem1.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}					
				}

				else if(degree.equals("AI")){
					if (position==0){
						 intent = new Intent(SelectYear.this, FirstYearChoices.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==1){
						 intent = new Intent(SelectYear.this, NavMap.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
						finish();
					}
					if (position==2){
						 intent = new Intent(SelectYear.this, ThirdYearChoicesSem1.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}					
				}
				
				else if(degree.equals("CSSE")){
					if (position==0){
						 intent = new Intent(SelectYear.this, FirstYearChoices.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==1){
						 intent = new Intent(SelectYear.this, NavMap.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==2){
						 intent = new Intent(SelectYear.this, ThirdYearChoicesSem1.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}					
				}
				
				else if(degree.equals("MCSSE")){
					if (position==0){
						intent = new Intent(SelectYear.this, FirstYearChoices.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==1){
						intent = new Intent(SelectYear.this, NavMap.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}
					if (position==2){
						intent = new Intent(SelectYear.this, ThirdYearChoicesSem1.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}					
				}
				
				else if(degree.equals("CSBM")){
					if (position==0){
						intent = new Intent(SelectYear.this, NavMap.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
						finish();
					}
					if (position==1){
						intent = new Intent(SelectYear.this, NavMap.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
						finish();
					}
					if (position==2){
						intent = new Intent(SelectYear.this, ThirdYearChoicesSem1.class);
						intent.putExtra("degree", degree);
						SelectYear.this.startActivity(intent);
					}					
				}
				
				intent.putExtra("year", position+1);
				SelectYear.this.startActivity(intent);				
			}
		});
	}	
}