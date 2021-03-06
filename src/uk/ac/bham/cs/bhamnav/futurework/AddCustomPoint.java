package uk.ac.bham.cs.bhamnav.futurework;

import org.me.nav.R;
import org.me.nav.R.id;
import org.me.nav.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Progress on this class has halted since GPS needs to be fully working first.
 */
public class AddCustomPoint extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.form);
		
		final EditText edittext = (EditText) findViewById(R.id.textbox);
		edittext.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		          Toast.makeText(AddCustomPoint.this, edittext.getText(), Toast.LENGTH_SHORT).show();
		          return true;
		        }
		        return false;
		    }
		});
		
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
					finish();
			}
		});
	}
}
