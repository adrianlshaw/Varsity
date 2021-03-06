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

public class SecondYearChoicesSem1 extends Activity {

	private Button submit;
	RadioButton prolog;
	RadioButton haskell;
	RadioButton nat;
	RadioButton mach;
	RadioButton soft;
	RadioButton L2;
	
	boolean b1 = false;
	boolean b2 = false;
	Intent intent; 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondyearchoicessem1);

		Intent prevIntent = getIntent();
		String degree = prevIntent.getStringExtra("degree");
		
		
		
		prolog = (RadioButton) findViewById(R.id.radio_prolog);
		 haskell = (RadioButton) findViewById(R.id.radio_haskell);
		 nat = (RadioButton) findViewById(R.id.radio_natcomp);
		 mach = (RadioButton) findViewById(R.id.radio_machlearn);
		 soft = (RadioButton) findViewById(R.id.radio_softeng);
		 L2 = (RadioButton) findViewById(R.id.radio_l2);
		
		prolog.setOnClickListener(buttonlistener);
		haskell.setOnClickListener(buttonlistener);
		nat.setOnClickListener(buttonlistener);
		mach.setOnClickListener(buttonlistener);
		soft.setOnClickListener(buttonlistener);
		L2.setOnClickListener(buttonlistener);
		
		intent = new Intent(SecondYearChoicesSem1.this, NavMap.class);
		
		submit = (Button) findViewById(R.id.submityear2sem1choice);
		submit.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
				if(soft.isChecked() || L2.isChecked()){
					intent = new Intent(SecondYearChoicesSem1.this, NavMap.class);
					SecondYearChoicesSem1.this.startActivity(intent);
				}
				else {
					intent = new Intent(SecondYearChoicesSem1.this, SecondYearChoicesSem2.class);
					SecondYearChoicesSem1.this.startActivity(intent);
				}
				
			}
			
		});
		submit.setEnabled(false);
		

		if(degree.equals("CSSE")){
			nat.setEnabled(false);
			L2.setEnabled(false);
			mach.setEnabled(false);
			soft.setChecked(true);
			soft.setEnabled(false);
		}	
		if(degree.equals("AI")){
			nat.setEnabled(true);
			nat.setChecked(true);
			L2.setEnabled(false);
			mach.setEnabled(true);
			mach.setChecked(true);
			soft.setEnabled(false);
			prolog.setChecked(true);
			prolog.setEnabled(true);
			haskell.setEnabled(false);
		}	
	}
	private OnClickListener buttonlistener = new OnClickListener(){
		public void onClick(View v){

			// Check if two items have been selected

			if(prolog.isChecked()){
				b1 = true;
				intent.putExtra("choice1", "prolog");
			}
			else if(haskell.isChecked()){
				intent.putExtra("choice1", "haskell");
				b1 = true;
			}
			
			if(nat.isChecked()){
				b2 = true;
				intent.putExtra("choice2", "natcomp");				
			}
			else if(mach.isChecked()){
				b2 = true;
				intent.putExtra("choice2", "machinelearning");				
			}
			else if(soft.isChecked()){
				b2 = true;
				intent.putExtra("choice2", "softwareengineering");				
			}
			else if(L2.isChecked()){
				b2 = true;
				intent.putExtra("", "L2");
			}
			if(b1==true & b2==true){
				submit.setEnabled(true);
			}			
		}
	};	
}
