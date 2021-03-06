
package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.id;
import org.me.nav.R.layout;

import uk.ac.bham.cs.bhamnav.NavMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ThirdYearChoicesSem2 extends Activity implements OnClickListener {

	private CheckBox compilers, db2, graphics, individstud2, ida, nlpa, nid, philo, principles, planning;
	private Button submit;
	private Intent prevIntent;
	private int numberofcreditssofar;
	int creditcount=0;
	AlertDialog alert;
	AlertDialog invalid;
	String degree="";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thirdyearchoicessem2);

		prevIntent = getIntent();
		numberofcreditssofar = prevIntent.getIntExtra("numberofcredits", 60); // default value is set to 60
		degree = prevIntent.getStringExtra("degree");
		
		
		compilers = (CheckBox) findViewById(R.id.compilersbox);
		db2 = (CheckBox) findViewById(R.id.db2box);
		graphics = (CheckBox) findViewById(R.id.g2box);
		individstud2 = (CheckBox) findViewById(R.id.indstud2box);
		ida =(CheckBox) findViewById(R.id.idabox);
		nlpa= (CheckBox) findViewById(R.id.nlpabox);
		nid = (CheckBox) findViewById(R.id.nidbox);
		philo = (CheckBox) findViewById(R.id.philobox);
		principles = (CheckBox) findViewById(R.id.principles);
		planning = (CheckBox) findViewById(R.id.planningbox);
		
		compilers.setOnClickListener(this);
		db2.setOnClickListener(this);
		individstud2.setOnClickListener(this);
		graphics.setOnClickListener(this);
		ida.setOnClickListener(this);
		nlpa.setOnClickListener(this);
		nid.setOnClickListener(this);
		philo.setOnClickListener(this);
		planning.setOnClickListener(this);
		principles.setOnClickListener(this);
		submit = (Button) findViewById(R.id.submitchoice2);
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
					Intent intent = new Intent(ThirdYearChoicesSem2.this, NavMap.class);
					ThirdYearChoicesSem2.this.startActivity(intent);
					
					finish();
			}
		});
		submit.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		
		creditcount = 0;
		
		
		if(compilers.isChecked()){
			creditcount+=10;
		}
		if(db2.isChecked()){
			creditcount+=10;
		}
		if(graphics.isChecked()){
			creditcount+=10;
		}
		if(individstud2.isChecked()){
			creditcount+=10;
		}
		if(ida.isChecked()){
			creditcount+=10;
		}
		if(nlpa.isChecked()){
			creditcount+=10;
		}
		if(nid.isChecked()){
			creditcount+=10;
		}
		if(philo.isChecked()){
			creditcount+=10;			
		}
		if(principles.isChecked()){
			creditcount+=10;			
		}
		if(planning.isChecked()){
			creditcount+=10;			
		}


		Log.i("NavMap", ""+numberofcreditssofar+" "+creditcount);
		
		if(degree.equals("CSBM")){
			if((creditcount == 20)){
				submit.setEnabled(true);
			}
			else {
				submit.setEnabled(false);
			}
		}
		
		else if((numberofcreditssofar + creditcount)==80){
			submit.setEnabled(true);
		}
		else if((numberofcreditssofar+creditcount)>80){
			Toast.makeText(this, "You have selected too many modules", Toast.LENGTH_LONG);
			submit.setEnabled(false);
		}
		else {
			submit.setEnabled(false);
		}
	}
}