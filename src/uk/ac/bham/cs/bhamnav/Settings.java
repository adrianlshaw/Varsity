package uk.ac.bham.cs.bhamnav;

import org.me.nav.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

/**IMPORTANT: This class is not included in the final version of BhamNav.
 * 
 * @author Adrian Shaw
 *
 */
public class Settings extends Activity {

	public static final int LOW_POWER = 1;
	public static final int MED_POWER = 2;
	public static final int HIGH_POWER = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings);

		final CheckBox antiAliasingBox = (CheckBox) findViewById(R.id.antialiasingbox);
			
		final SharedPreferences preferences = this.getSharedPreferences("bhamnavpreferences", MODE_PRIVATE);
		boolean antialiasing = preferences.getBoolean("antialiasing", false);
		int powerSetting = preferences.getInt("power", HIGH_POWER);
		
		antiAliasingBox.setChecked(antialiasing);
		
		antiAliasingBox.setOnClickListener(new OnClickListener(){
		Editor edit = preferences.edit();
	
			@Override
			public void onClick(View v) {

				if(antiAliasingBox.isChecked()){
					edit.putBoolean("antialiasing", true);
					Toast.makeText(getApplicationContext(), "Anti-aliasing turned ON", Toast.LENGTH_SHORT);
				}
				else {
					edit.putBoolean("antialiasing", false);
					Toast.makeText(getApplicationContext(), "Anti-aliasing turned OFF", Toast.LENGTH_SHORT);
				}

				edit.commit();
			}});
		
		RadioButton gpsLow = (RadioButton) findViewById(R.id.settingslowpowerradio);
		final RadioButton gpsMed = (RadioButton) findViewById(R.id.settingsmediumpowerradio);
		final RadioButton gpsHigh = (RadioButton) findViewById(R.id.settingshighpowerradio);
		
		switch(powerSetting){
			case HIGH_POWER: gpsHigh.setChecked(true);	break;
			case MED_POWER: gpsMed.setChecked(true);	break;
			case LOW_POWER: gpsLow.setChecked(true);	break;		
		}
		
		Button submit = (Button) findViewById(R.id.submitsettings);
		submit.setOnClickListener(new OnClickListener(){

			Editor edit = preferences.edit();			
			
			@Override
			public void onClick(View v) {
				
				if(gpsHigh.isChecked()==true){
					edit.putInt("power", HIGH_POWER);
				}
				else if(gpsMed.isChecked()==true){
					edit.putInt("power", MED_POWER);
				}
				else {
					edit.putInt("power", LOW_POWER);
				}
				edit.commit();				
				finish();
			}
		});	
	}	
}