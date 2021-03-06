package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.id;
import org.me.nav.R.layout;

import uk.ac.bham.cs.bhamnav.NavMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class FirstYearChoices extends Activity {

	private Button submit;
	private Intent intent;

	private RadioButton infoandtheweb;
	private RadioButton robotprogramming;
	private RadioButton intromaths;
	private RadioButton momd;
	
	private boolean b1 = false;
	private boolean b2 = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstyearchoices);
		infoandtheweb = (RadioButton) findViewById(R.id.radio_infoweb);
		robotprogramming = (RadioButton) findViewById(R.id.radio_robots);
		intromaths = (RadioButton) findViewById(R.id.radio_maths);
		momd = (RadioButton) findViewById(R.id.radio_momd);
		momd.setOnClickListener(buttonlistener);
		intromaths.setOnClickListener(buttonlistener);
		infoandtheweb.setOnClickListener(buttonlistener);
		robotprogramming.setOnClickListener(buttonlistener);
		
		intent = new Intent(FirstYearChoices.this, NavMap.class);		
		submit = (Button) findViewById(R.id.submityear1choice);
		submit.setEnabled(false);
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				FirstYearChoices.this.startActivity(intent);				
				finish();
			}
			
		});

		infoandtheweb.setOnClickListener(buttonlistener);
		robotprogramming.setOnClickListener(buttonlistener);
	}
	private OnClickListener buttonlistener = new OnClickListener(){
		public void onClick(View v){

			if(infoandtheweb.isChecked()){
				b1 = true;
				intent.putExtra("choice1", "infoandtheweb");
			}
			if(robotprogramming.isChecked()){
				b1 = true;
				intent.putExtra("choice1", "robots");
			}			
			if(intromaths.isChecked()){
				b2 = true;
				intent.putExtra("choice2", "introtomaths");				
			}
			if(momd.isChecked()){
				b2 = true;
				intent.putExtra("choice2", "momd");				
			}
			
			if(b1==true && b2==true){
				submit.setEnabled(true);
			}
		}
	};
}
