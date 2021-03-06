package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
/**
 * 
 * @author Adrian Shaw
 */
public class RouteOverlay extends Overlay {

	private boolean ANTI_ALIASING = false;
		
	public RouteOverlay(){

	}
	
	private final static int CIRCLE = 1;
	private final static int MIN_ZOOM_LEVEL = 16;
	private int shape = 0;
	
	private GeoPoint defaultpos = new GeoPoint(52450792,-1936877);
	
	private Path p;
	
	private GeoPoint current; // GeoPoint representing the last known location of the user
	
	private ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();	// path 
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		super.draw(canvas, mapView, shadow);

		if(mapView.getZoomLevel()<10){
			mapView.getController().setZoom(MIN_ZOOM_LEVEL);
		}
		
		p = new Path();
				
		Paint paint = new Paint();
		paint.setARGB(200, 0, 191, 255); // alpha compositing
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(ANTI_ALIASING);
		
		if(shape==CIRCLE)
		{
			Point screenPoint = new Point();
			Projection p = mapView.getProjection();
			p.toPixels(current, screenPoint);
			canvas.drawCircle(screenPoint.x, screenPoint.y, 30f, paint);
			shape = 0;
		}
				
		/**
		 * If there is more than one point in the ArrayList points draw the route.
		 * But if there is only one point then do not draw (this happens when the 
		 * person's destination is the same as their current position). 
		 */
		if(points.size()>1){
			Point pA = new Point();
			Point pB = new Point();
			Projection projection = mapView.getProjection();
			
			/**
			 * Prepare co-ordinates to be projected onto screen
			 */
			for(int i=0;i<points.size()-1;i++)
			{
				projection.toPixels(points.get(i), pA);
				p.moveTo(pA.x, pA.y);
				projection.toPixels(points.get(i+1), pB);
				p.lineTo(pB.x, pB.y);
			}
									
			canvas.drawPath(p, paint);	
		}
		else {
			//Log.i("NavMap", "Not enough points");
		}				
	}

	public void drawCircle(Canvas canvas, MapView mapView, boolean shadow, GeoPoint geopoint) {
		current = geopoint;
		shape = 1;
		this.draw(canvas, mapView, false);
	}
	
	public void setPath(ArrayList<GeoPoint> points){
		this.points = points;
	}
	
	public void setCurrentLocation(GeoPoint g){
		current = g;
	}
	
	public void setAntiAliasing(boolean on){
		ANTI_ALIASING = on;
	}
}