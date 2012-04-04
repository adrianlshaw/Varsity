package uk.ac.bham.cs.bhamnav.futurework;

import java.util.ArrayList;

import org.me.nav.R;
import org.me.nav.R.layout;

import uk.ac.bham.cs.bhamnav.NavMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
   
public class EventView extends ListActivity {

	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		
        Intent intent = getIntent();		
		@SuppressWarnings("unchecked")
		final ArrayList<Event> receipt = (ArrayList<Event>) intent.getSerializableExtra("events");
		Log.i("NavMap", "There are "+receipt.size()+" lectures");
		
		if(receipt.size()>0){
	        ListView listview = getListView();

	        String[] lectures = new String[receipt.size()];
			
			for(int i=0; i<receipt.size();i++)
			{
				Event e = receipt.get(i);
				String s ="";
				s = e.hour+":"+e.minutes+" - "+e.name;
				lectures[i] = s;
			}
			
			this.setListAdapter(new ArrayAdapter<Object>(this, R.layout.eventview, lectures));

			listview.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id){
															
					String place = receipt.get(position).building;
					Intent intent = new Intent(EventView.this, NavMap.class);
					intent.putExtra("place", place);
					EventView.this.startActivity(intent);
					finish();
				}
			});
		}
		else {
			
			Toast.makeText(getApplicationContext(), "No lectures", Toast.LENGTH_SHORT).show();
		}	
	}
}