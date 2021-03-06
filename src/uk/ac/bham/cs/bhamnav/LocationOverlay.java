package uk.ac.bham.cs.bhamnav;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
/**
 * @author Adrian Shaw
 *
 */
public class LocationOverlay extends Overlay {

	private GeoPoint location;
	private Point screenPoints = new Point();
	private Paint p = new Paint();	
	
	private int RADIUS = 10;
	
	public LocationOverlay(int screenWidth){
		RADIUS = screenWidth / 15;
		p.setColor(Color.BLUE);
		p.setStrokeWidth(5);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		super.draw(canvas, mapView, shadow);
		if(location!=null){
			mapView.getProjection().toPixels(location, screenPoints);
			if(screenPoints!=null){
				p.setAlpha(50);				
				canvas.drawCircle(screenPoints.x, screenPoints.y, RADIUS, p);
				p.setColor(Color.BLUE);
				p.setAlpha(255);
				canvas.drawCircle(screenPoints.x, screenPoints.y, RADIUS/2, p);
				p.setColor(Color.WHITE);				
				canvas.drawCircle(screenPoints.x, screenPoints.y, (RADIUS/2)-3, p);
			}
		}
	}

	public void updateLocation(GeoPoint g){
		location = g;
	}
}