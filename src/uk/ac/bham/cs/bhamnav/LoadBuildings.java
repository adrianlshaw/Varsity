package uk.ac.bham.cs.bhamnav;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LoadBuildings {

	private ArrayList<Node> nodes;
	private Context c;
	private BuildingsOverlay buildingsOverlay;
	private static final String EXTENSION = ".mp3";
	
	public LoadBuildings(ArrayList<Node> n, BuildingsOverlay b, Context context){
		nodes = n;
		c = context;
		buildingsOverlay = b;
	}
	
	public void load(){
		
		AssetManager mg = c.getResources().getAssets();
		
		for(Node node : nodes){
			
			try {
//				FileInputStream input = mg.openFd(node.getBuildingName().trim()).createInputStream();
				ArrayList<String> strings = new ArrayList<String>();
				
				InputStreamReader reader = new InputStreamReader(mg.openFd(node.getBuildingName()+EXTENSION).createInputStream());				
				BufferedReader buffreader = new BufferedReader(reader);
				String readLine = buffreader.readLine();
				Log.i("NavMap", "File loaded");

				while(readLine!=null){
					strings.add(readLine);
					readLine = buffreader.readLine();
				}
				
				if(strings.size()>0){
					
					ArrayList<GeoPoint> coordinates = new ArrayList<GeoPoint>();
					for(String s : strings){
						if(s.contains(",")){
							String[] splittedStrings = s.split(",");
							if(splittedStrings.length>0 && splittedStrings.length<=2){
								String latString = splittedStrings[0];
								String lonString = splittedStrings[1];
								
								try {
									int latint = Integer.valueOf(latString.trim());
									int lonint = Integer.valueOf(lonString.trim());
									GeoPoint g = new GeoPoint(latint, lonint);
									coordinates.add(g);
								}
								catch(NumberFormatException ne){
									Log.i("NavMap", "Failed to convert LatString:"+latString);
									Log.i("NavMap", "Failed to convert LonString:"+lonString);
								}
								catch(ClassCastException cast){
									Log.i("NavMap", "Failed to cast LatString:"+latString);
									Log.i("NavMap", "Failed to cast LonString:"+lonString);
								}
								
							}
						}
					}
					
					if(coordinates.size()>1){
						Building building = new Building(node.getBuildingName(),coordinates,new GeoPoint(222343,333433));
						
						int index = getNodeIndex(node.getBuildingName());
						if(index>=0){
							nodes.get(index).setBuilding(building);
						}
						else {
							Log.i("NavMap","Couldn't find index for building name "+node.getBuildingName());
						}
						
						buildingsOverlay.addBuilding(building);
						Log.i("NavMap", "Success: A building was added with "+coordinates.size()+" coordinates");

					}
					else {
						Log.i("NavMap", "Building that was loaded doesn't have enough co-ordinats");
					}
				}				
			}
			catch(FileNotFoundException e){
//				Log.i("NavMap", "Cannot find file "+e.getMessage());
			}
			catch(IOException ioe){
				Log.i("NavMap", "IOException");				
			}
			catch(NullPointerException nue){
				
			}			
		}
	}
	
	public int getNodeIndex(String buildingname){
		for(int i=0;i<nodes.size();i++)
		{
			Node s = nodes.get(i);
			
			if(s.getBuildingName()!=null && s.getBuildingName().equalsIgnoreCase(buildingname)){
				return i;
			}
		}
		return -1;
	}
}