package uk.ac.bham.cs.bhamnav.futurework;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class CalView extends TableLayout{

	public CalView(Context context) {
		super(context);

		Button button = new Button(context);
		button.setText("BTN1");
		Button button2 = new Button(context);
		button2.setText("BTN2");
		
//		this.addView(button);
//		this.addView(button2);
		
//		TableView table = new TableView(context);
		
		for(int k=0; k<4; k++){
			TableRow row = new TableRow(context);
		
			for(int i=1;i<8;i++)
			{
				Button b = new Button(context);
				b.setText(String.valueOf(i));
				row.addView(b);
			}
		
			this.addView(row);
		
		}
		this.setVisibility(View.VISIBLE);
		
	}
}
