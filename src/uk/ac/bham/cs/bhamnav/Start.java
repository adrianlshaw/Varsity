package uk.ac.bham.cs.bhamnav;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.me.nav.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * @author Adrian Shaw
 *
 */
public class Start extends Activity{
	
	private static int shinyNewAPIsSupported = android.os.Build.VERSION.SDK_INT;
	
	private static boolean fragmentsSupported = false;
	
	private static void checkFragmentsSupported() throws NoClassDefFoundError{

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.splash);
		
		int thebg = 1 + (new Random().nextInt(3));
				
		ImageView image = (ImageView) findViewById(R.id.splashimage);
		
		/**
		 * Image gets chosen at random.
		 */
		if(thebg<=1){
			image.setImageResource(R.drawable.bg);			
		}
		else if(thebg==2){
			image.setImageResource(R.drawable.bg2);			
		}
		else if(thebg==3){
			image.setImageResource(R.drawable.bg3);			
		}
		else if(thebg>=4){
			image.setImageResource(R.drawable.bg4);
		}

		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{

			@Override
			public void run() {
				Intent intent = new Intent(Start.this, NavMap.class);
				Start.this.startActivity(intent);
				this.cancel();
				Start.this.finish();
			}
			
		};
				
		timer.schedule(task, 1000, 1000);
		
	}
}