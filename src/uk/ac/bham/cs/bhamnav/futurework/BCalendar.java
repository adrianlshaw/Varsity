package uk.ac.bham.cs.bhamnav.futurework;

import java.util.ArrayList;

import org.me.nav.R;
import org.me.nav.R.id;
import org.me.nav.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Adrian Shaw
 */

public class BCalendar extends Activity implements OnClickListener{

	Time t = new Time();

	private String month = "";
	private static int year;
	private static int monthno;
	private static TextView tv;
	private static int monthDisplayed;
	@SuppressWarnings("unused")
	private static int yearDisplayed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.calendar);

		t.setToNow();

		monthno = t.month;
		year = t.year;
		month = getMonthName(monthno);
		

		tv = (TextView) this.findViewById(R.id.text);
		tv.setText(month + " " + t.year);

		monthDisplayed = monthno;
		yearDisplayed = year;
		
		Button nextMonth = (Button) this.findViewById(R.id.nextmonth);
		nextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BCalendar.monthDisplayed++;
				refresh();
			}

		});

		Button prevMonth = (Button) this.findViewById(R.id.prevmonth);
		prevMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BCalendar.monthDisplayed--;
				refresh();
			}

		});

		for(int i=0; i<buttonIDs.length;i++){
	       findViewById(buttonIDs[i]).setOnClickListener(this);	        
		}
		
		hideExtraButtons();
		refresh();
	}

	final static int[] buttonIDs = { R.id.b01, R.id.b02, R.id.b03, R.id.b04,
			R.id.b05, R.id.b06, R.id.b07, R.id.b08, R.id.b09, R.id.b10,
			R.id.b11, R.id.b12, R.id.b13, R.id.b14, R.id.b15, R.id.b16,
			R.id.b17, R.id.b18, R.id.b19, R.id.b20, R.id.b21, R.id.b22,
			R.id.b23, R.id.b24, R.id.b25, R.id.b26, R.id.b27, R.id.b28,
			R.id.b29, R.id.b30, R.id.b31 };

	public void refresh() {

		if (monthDisplayed < t.month) {
			monthDisplayed++;
			return;
		}
		
		if (monthDisplayed>11){
			monthDisplayed--;
			return;
		}

		Button thirtyfirst = (Button) findViewById(R.id.b31);
		Button thirtieth = (Button) findViewById(R.id.b30);
		Button twentyninth = (Button) findViewById(R.id.b29);

		twentyninth.setVisibility(Button.VISIBLE);
		
		switch (monthDisplayed) {

		case 0:
			thirtyfirst.setVisibility(Button.VISIBLE);
			thirtieth.setVisibility(Button.VISIBLE);
			break;
		case 1:
			thirtyfirst.setVisibility(Button.INVISIBLE);
			thirtieth.setVisibility(Button.INVISIBLE);
			
			// Leap year if divisble by 4
				// But if divisible by 100 then it is not
					// But if divisible by 100 AND 400 then it is
						
			if((t.year % 400)==0){
				twentyninth.setVisibility(Button.VISIBLE);
			}
			else if((t.year % 100)==0){
				twentyninth.setVisibility(Button.INVISIBLE);				
			}
			else if((t.year % 4)==0){
				twentyninth.setVisibility(Button.VISIBLE);				
			}
			else {
				twentyninth.setVisibility(Button.INVISIBLE);				
			}
			
			break;
		case 2:
			thirtyfirst.setVisibility(Button.VISIBLE);
			thirtieth.setVisibility(Button.VISIBLE);		
			break;
		case 3:
			thirtieth.setVisibility(Button.VISIBLE);		

			thirtyfirst.setVisibility(Button.INVISIBLE);
			break;
		case 4:
			thirtieth.setVisibility(Button.VISIBLE);		

			thirtyfirst.setVisibility(Button.VISIBLE);
			break;
		case 5:
			thirtieth.setVisibility(Button.VISIBLE);		

			thirtyfirst.setVisibility(Button.INVISIBLE);
			break;
		case 6:
			thirtieth.setVisibility(Button.VISIBLE);		

			thirtyfirst.setVisibility(Button.VISIBLE);
			break;
		case 7:
			thirtieth.setVisibility(Button.VISIBLE);

			thirtyfirst.setVisibility(Button.VISIBLE);
			break;
		case 8:
			thirtieth.setVisibility(Button.VISIBLE);	

			thirtyfirst.setVisibility(Button.INVISIBLE);
			break;
		case 9:
			thirtieth.setVisibility(Button.VISIBLE);	

			thirtyfirst.setVisibility(Button.VISIBLE);
			break;
		case 10:
			thirtieth.setVisibility(Button.VISIBLE);

			thirtyfirst.setVisibility(Button.INVISIBLE);
			break;
		case 11:
			thirtieth.setVisibility(Button.VISIBLE);

			thirtyfirst.setVisibility(Button.VISIBLE);
			break;

		}

		t.setToNow();

		Log.i("NavMap", "Year:" + t.year + " Month: " + t.month
				+ " DayofMonth:" + t.monthDay + " Hour:" + t.hour + " Minute:"
				+ t.minute + " Second:" + t.second);

		tv.setText(getMonthName(monthDisplayed));
		if (monthDisplayed == monthno) {
			Button b = (Button) findViewById(buttonIDs[t.monthDay - 1]);
			b.getBackground().setColorFilter(0xFFFF0000,
					PorterDuff.Mode.MULTIPLY);
		} else {
			Button b = (Button) findViewById(buttonIDs[t.monthDay - 1]);
			b.getBackground().setColorFilter(null);
		}
	}

	public static String getMonthName(int m) {
		switch (m) {

		case 0:
			return "January";
		case 1:
			return "Febuary";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";
		default:
			return "Next Year";
		}
	}

	public void hideExtraButtons() {
		Button hide;
		hide = (Button) this.findViewById(R.id.extra1);
		hide.setVisibility(Button.INVISIBLE);
		hide = (Button) this.findViewById(R.id.extra2);
		hide.setVisibility(Button.INVISIBLE);
		hide = (Button) this.findViewById(R.id.extra3);
		hide.setVisibility(Button.INVISIBLE);
		hide = (Button) this.findViewById(R.id.extra4);
		hide.setVisibility(Button.INVISIBLE);
	}

	public void addLecture() {
		// is there a class already called Event? Need to store Date/Time/Room
		// etc
	}
	
	/**
	 * @param id
	 * @return	Returns the index of the ID in the array if there is a match.
	 * 			Returns -1 if the id is not in the array.
	 */
	public int checkForID(int id){
		Log.i("NavMap", "Checking for ID "+id);
		
		for(int i=0; i<buttonIDs.length; i++){
			if(id == buttonIDs[i]){
				return i;
			}
		}
		Log.i("NavMap", "Didn't make it");
		
		return -1;
	}

	ArrayList<Event> lectures;
	
	public void addEventsTest(){
		 lectures = new ArrayList<Event>();

		 Event e = new Event("Compilers", 12, 0, 30, 1, 2011, 50, "LT2", "Mechanical Engineering");

		 lectures.add(e);
		 
		 e = new Event("Planning", 14, 0, 30, 1, 2011, 50, "LT2", "Sports Ex Sciences");
		 lectures.add(e);
	}
	
	@Override
	public void onClick(View arg0) {

		addEventsTest();
		
		Log.i("NavMap", "view clicked");
		
		int index = checkForID(arg0.getId());
		
		Log.i("Navmap", "Index = "+ index);
		
		if(index>=0){

			Intent intent = new Intent(BCalendar.this, EventView.class);							
			
			ArrayList<Event> arrayToSend = new ArrayList<Event>();
			
			for(int i = 0; i<lectures.size();i++){
				
				int temp = lectures.get(i).dayofmonth;

				if(temp-1==index){

					arrayToSend.add(lectures.get(i));
				}
			}
			
			Log.i("NavMap","BeforeSending: has "+arrayToSend.size()+" lectures");
			
			intent.putExtra("events", arrayToSend);
			this.startActivity(intent);
		}	
	}
}