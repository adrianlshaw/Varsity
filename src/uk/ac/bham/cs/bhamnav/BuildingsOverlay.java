package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class BuildingsOverlay extends Overlay{

	private ArrayList<Building> buildings = new ArrayList<Building>();
	
	public void addBuilding(Building name)
	{ 
		buildings.add(name);
    	Log.i("NavMap", "Building added in Overlay with"+name.getGeoPoints().size());

	}
	
	public void removeBuilding(String name){
		for(int i=0; i<buildings.size();i++)
		{
			if(buildings.get(i).getName().equalsIgnoreCase(name)){
				buildings.remove(i);
			}
		}
	}
	
	public void removeAll(){
		buildings.clear();
	}
	
	public void emptyList(){
		for(int i=0;i<buildings.size();i++)
		{
			buildings.remove(i);
		}
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(false);

		if(buildings.size()==0){
        	Log.i("NavMap", "No buildings to draw");
		}
		
		for(int i=0;i<buildings.size();i++)
		{

			Building building = buildings.get(i);
			ArrayList<GeoPoint> points = building.getGeoPoints();

			Path path = new Path();

			if(points.size()>1){
				Point pA = new Point();
				Point pB = new Point();
				Projection projection = mapView.getProjection();
				for(int k=0;k<points.size()-1;k++)
				{
					projection.toPixels(points.get(k), pA);
					path.moveTo(pA.x, pA.y);
					projection.toPixels(points.get(k+1), pB);
					path.lineTo(pB.x, pB.y);					
				}
				
				Point lastPoint = new Point();
				Point firstPoint = new Point();
				projection.toPixels(points.get(points.size()-1), lastPoint);
				projection.toPixels(points.get(0), firstPoint);
				path.moveTo(lastPoint.x, lastPoint.y);
				path.lineTo(firstPoint.x, firstPoint.y);

				path.close();
				
				path.setFillType(Path.FillType.EVEN_ODD);
				
				canvas.drawPath(path, paint);
	        	Log.i("NavMap", "Drawn");

			}
			else {
	        	Log.i("NavMap", "Not enough points to draw building");

			}
		}
	}	
}