package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

public class Building {

	private ArrayList<GeoPoint> points;
	private String name;
		
	public Building(String buildingName, ArrayList<GeoPoint> p, GeoPoint entrance){
		
		name = buildingName;
		
		if (p.size()>1){
			points = p;			
		}
		else {
			points = null;
		}
	
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<GeoPoint> getGeoPoints(){
		
		if(points!=null){
			return points;			
		}
		else {
			return new ArrayList<GeoPoint>();
		}

	}
}
