package uk.ac.bham.cs.bhamnav;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class Node {
	private String nodeID;
	private String nodeName;
	private GeoPoint point;
	private String code;
	private String department;
	private String buildingName;
	private String description;
	private boolean visible = false;
	private String[] connectedTo;
	private Building building;
	
	public Node(GeoPoint point, String name, String nodeID){
	
		this.nodeName = name;
		this.point = point;
		this.nodeID = nodeID;
	}
	
	public GeoPoint getGeoPoint(){
		return point;
	}

	public String getName(){		
		return nodeName;
	}
	
	public String getNodeID(){
		return nodeID;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartment() {
		return department;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		if(code!=null){
			return code;
		}
		else {
			return "";
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setConnectedTo(String[] connectedTo) {
		this.connectedTo = connectedTo;
	}

	public String[] getConnectedTo() {
		return connectedTo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
		
	public void setName(String newName){
		this.nodeName = newName;
	}
	
	public void setImage(){
		
	}
	
	public void setBuilding(Building b){
		building = b;
	}
	
	public Building getBuilding(){
		return building;
	}
	
	public Location getLocation(){
		
		Location l = new Location("");
		
		l.setLongitude(point.getLongitudeE6()/1E6);
		l.setLatitude(point.getLatitudeE6()/1E6);
		
		return l;
	}
	
	public int getCodeAsInt(){
		if(code!=null){
			String temp = code;
			if(temp.startsWith("Y") || temp.startsWith("R") || temp.startsWith("O")){
				String withouty = temp.substring(1);
				return Integer.valueOf(withouty);
			}
		}
		return -1;
	}
	
	private int drawable = -1;
	
	public void setDrawable(int d){
		drawable = d;
	}
	
	public int getDrawable(){		
		return drawable;
	}
}