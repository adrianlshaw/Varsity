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

public class SecondYearChoicesSem2 extends Activity {

	private Button submit;
	RadioButton nlp;
	RadioButton compvis;
	
	Intent intent; 
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondyearchoicessem2);
		
		compvis = (RadioButton) findViewById(R.id.radio_compvis);
		nlp = (RadioButton) findViewById(R.id.radio_nlp);
		
		compvis.setOnClickListener(buttonlistener);
		nlp.setOnClickListener(buttonlistener);
		
		intent = new Intent(SecondYearChoicesSem2.this, NavMap.class);
		
		submit = (Button) findViewById(R.id.submityear2sem2choice);
		submit.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				SecondYearChoicesSem2.this.startActivity(intent);
				finish();
			}
			
		});
		submit.setEnabled(false);
	}
	private OnClickListener buttonlistener = new OnClickListener(){
		public void onClick(View v){
			
			RadioButton btn = (RadioButton) v;

			if(btn.getId()==R.id.radio_compvis){
				intent.putExtra("choice3", "compvision");
			}
			else {
				intent.putExtra("choice3", "nlp");
			}
			
			submit.setEnabled(true);
		}
	};	
}
