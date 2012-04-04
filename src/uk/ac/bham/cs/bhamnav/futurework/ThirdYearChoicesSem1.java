package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.id;
import org.me.nav.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ThirdYearChoicesSem1 extends Activity implements OnClickListener {

	private CheckBox commprog;	
	private CheckBox evocomp;	
	private CheckBox hci;
	private CheckBox individstud;	
	private CheckBox irobots	;
	private CheckBox networks;	
	private CheckBox neural;
	private CheckBox os;
	private Intent intent;
	
	Intent prevIntent;
	Button submit;
	int creditcount = 0;
	AlertDialog alert;
	AlertDialog invalid;
	String degree;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thirdyearchoicessem1);
		
		Intent prevIntent = getIntent();
		degree = prevIntent.getStringExtra("degree");
		
		intent = new Intent(ThirdYearChoicesSem1.this, ThirdYearChoicesSem2.class);
		submit = (Button) findViewById(R.id.submitchoice1);
		
		commprog = (CheckBox) findViewById(R.id.commprogbox);
		evocomp = (CheckBox) findViewById(R.id.evocompbox);
		hci = (CheckBox) findViewById(R.id.hcibox);
		individstud = (CheckBox) findViewById(R.id.indstudbox);
		irobots = (CheckBox) findViewById(R.id.irbox);
		networks = (CheckBox) findViewById(R.id.networksbox);
		neural = (CheckBox) findViewById(R.id.neuralbox);
		os = (CheckBox) findViewById(R.id.osbox);
		
		commprog.setOnClickListener(this);
		evocomp.setOnClickListener(this);
		hci.setOnClickListener(this);
		individstud.setOnClickListener(this);
		irobots.setOnClickListener(this);
		networks.setOnClickListener(this);
		neural.setOnClickListener(this);
		os.setOnClickListener(this);
		
		alert = new AlertDialog.Builder(this).create();		
		alert.setCancelable(false);
		alert.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
				intent.putExtra("degree", degree);
				ThirdYearChoicesSem1.this.startActivity(intent);
			}
		});

		invalid = new AlertDialog.Builder(this).create();		
		invalid.setCancelable(false);
		invalid.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				invalid.dismiss();
			}
		});

		
		submit.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				
				if(degree.equals("CSBM") && creditcount==20){
					intent.putExtra("degree", degree);
					ThirdYearChoicesSem1.this.startActivity(intent);
				}
				
				else if(creditcount>=30 && !degree.equals("CSBM")){
					
					if(creditcount>50){
						invalid.setTitle("Invalid selection");
						invalid.setMessage("You have selected too many credits. Please choose "+(creditcount-50)+" less");
						invalid.show();
						
					}
					else {
						if(creditcount==50){
							alert.setTitle("Warning");
							alert.setMessage("50 credits in Semester 1 is allowed, but at your own risk.");
							alert.show();
							intent.putExtra("numberofcredits", creditcount);
						}
						else if(creditcount==30){
									alert.setTitle("Warning");
									alert.setMessage("30 credits in Semester 1 is allowed, but you must choose 50 credits in Semester 2.");
									alert.show();
									intent.putExtra("numberofcredits", creditcount);
						}
					
						if(creditcount==40){
							intent.putExtra("degree", degree);
							ThirdYearChoicesSem1.this.startActivity(intent);
							intent.putExtra("numberofcredits", creditcount);
						}

					}
				}
			}
		});
					
		TextView t = (TextView) findViewById(R.id.thirdyeartextsem1);
		
		
		submit.setEnabled(false);
		if(degree.equals("CSSE")){
			commprog.setEnabled(false);
			commprog.setChecked(true);
			t.setText("Choose between 20 credits and 40 credits");
		}
	}

	@Override
	public void onClick(View v) {
		

		creditcount = 0;


		if(commprog.isChecked()){
			creditcount+=10;
		}
		if(evocomp.isChecked()){
			creditcount+=10;
		}
		if(hci.isChecked()){
			creditcount+=10;
		}
		if(individstud.isChecked()){
			creditcount+=10;
		}
		if(irobots.isChecked()){
			creditcount+=20;
		}
		if(networks.isChecked()){
			creditcount+=20;			
		}
		if(neural.isChecked()){
			creditcount+=10;
		}
		if(os.isChecked()){
			creditcount+=20;			
		}
		
		if(degree.equals("CSBM")){
			if(creditcount==20){
				submit.setEnabled(true);
			}
			else {
				submit.setEnabled(false);
			}
		}
		else {
			// Enable button if minimum credits entered
			if(creditcount>=30 && creditcount<=50){
				submit.setEnabled(true);
			}
			else {
				submit.setEnabled(false);
			}			
		}
	}	
}