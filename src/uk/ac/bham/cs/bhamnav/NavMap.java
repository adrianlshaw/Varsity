package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.me.nav.R;

import uk.ac.bham.cs.bhamnav.futurework.SelectDegree;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View; 
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
//	Icons used in the Menu are from http://www.darshancomputing.com/android/1.5-drawables.html 
//	Mobiforge (http://mobiforge.com/) was a good resource for helping me to 
//	understand how to put a MapView within a TabHost.
public class NavMap extends MapActivity implements LocationListener, Listener {

/*******************************IMPORTANT GLOBAL VARIABLES**************************************/
	
	private static boolean DEBUG = false;
	private TabHost tabHost;
	private ListView listView;
	private MapView mapView;
	private MapController mapController;
	private String provider = LocationManager.GPS_PROVIDER; // String of the current GPS provider
	private LocationManager lm;
	private Location lastloc; // variable storing the last known location of the device
	private Criteria criteria;
	private Canvas c = new Canvas();

	private RouteOverlay r;
	private LocationOverlay locationOverlay;
	private BuildingsOverlay buildingsOverlay;
    	int defaultstart = 0;
    	int currentdestination = -1;
	private ArrayList<Node> nodes;
	private Location CAMPUS = new Location("");
	private int CAMPUS_DIAMETER = 650;
    
	/* Extra Variables */
	private int currentSort = 0;
	private long WAIT = 0l;
	private float MIN_DISTANCE = 0f;
	private final int ZOOM_LEVEL = 20;
	private int POWER_SETTING = Criteria.POWER_HIGH;
	private boolean ANTI_ALIASING = false;
	
	/* Dialogs */
	private ProgressDialog dialog;
	private AlertDialog myAlert;
	
	/*Constants */
	private final static int MAP_TAB = 1;		
	private Location destinationCoods = null;
	private boolean reached = false;
	private int RANGE = 20;

	
/*******************************METHODS BEGIN HERE************************************************/

	private void addProximityAlert(Location destination, int range){
		destinationCoods = destination;
		RANGE = range;
		reached = false;
	}
	
	/**	Checks the 
	 * 
	 * @param l		The Location object of the place you want to setup an alert at
	 */
	private void checkProximity(Location l){
		if(reached!=true){
			if(destinationCoods!=null){
				if(l.distanceTo(destinationCoods)<RANGE){
					reached = true;
					showInfo("Success","You have arrived at your destination");
					((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(300); 
					// SOURCE: http://www.clingmarks.com/?p=105 29/03/11
				}
			}			
		}
	}	
	
	
	/**
	 * This is the first method that gets called when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.testnav2);
		CAMPUS.setLatitude(52.450547);
		CAMPUS.setLongitude(-1.930541);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(); // needs to be done because it is not a regular tabactivity
		listView = (ListView) findViewById(R.id.list);
		listView.setEmptyView((TextView) findViewById(R.id.empty));
		setupDefaultCriteria();
		dialog = ProgressDialog.show(NavMap.this, "","Loading...Please Wait...");
		r = new RouteOverlay();
		setupPreferences();
		r.setAntiAliasing(ANTI_ALIASING);
		//Log.i("NavMap", "Anti-aliasing is "+ANTI_ALIASING);
		
		/* Load graph*/
		Resources resources = getResources();
		LoadXML load = new LoadXML(resources.getXml(R.xml.graph)); 
		// Loads XML graph from Resource ID
		nodes = load.convertXML();

		
		/* Setup MapView */
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(ZOOM_LEVEL);
		mapController.setCenter(
			new GeoPoint(
				(int) (CAMPUS.getLatitude()*1e6),
				(int) (CAMPUS.getLongitude()*1e6))
			);
		createTouchableOverlays();
		mapView.setSatellite(true);
		mapView.postInvalidate();
    		buildingsOverlay = new BuildingsOverlay();
    	
    		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    		// Source: http://stackoverflow.com/
		//questions/1016896/android-how-to-get-screen-dimensions
    	
		locationOverlay = new LocationOverlay(screenWidth);
		
		mapView.getOverlays().add(locationOverlay);
		
		loadBuildings();
		
		// Source: www.stackoverflow.com/questions/
		//1560788/how-to-check-internet-access-on-android-inetaddress-never-timeouts
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		boolean networkConnected;
		
		try {
			networkConnected = cm.getActiveNetworkInfo().isConnectedOrConnecting();
			if(!networkConnected){
				showInfo("No internet connection",
					"You are currently not connected to the internet." +
					" Please connect to a wireless or mobile network for full functionality.");
			}
		}
		catch(NullPointerException nonetwork){			
			showInfo("No internet connection","You are currently not connected to the internet." +
				" Please connect to a wireless or mobile network for full functionality.");		
		}
		
		setupPlacesTabByDepartment();
		
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        
		        final String s = (String) listView.getAdapter().getItem(position);

			int positionInNodes = -1;
				        				        
			// if the list is sorted by Code, find the index of the corresponding node
			if(currentSort==1){
				        			        	
				if(	s.startsWith("Y") || s.startsWith("R") || 
					s.startsWith("O") || s.startsWith("G") ||
				 	s.startsWith("B")){
				        
					String[] number = s.split(" ", 2);
		        		//Log.i("NavMap","Number = '"+number[0]+"'");
		        		String code = number[0];
		        		code.trim();
		        				
		        		//Log.i("NavMap", "Char at 1 "+code.charAt(1));
		        				
		        		if(code.charAt(1)=='0'){
		        			String[] temp = code.split("0", 2);
		        			code = temp[0]+temp[1];
			        		//Log.i("NavMap", "Replaced 0");
		        		}
		        				
				        for(int i=0; i<nodes.size();i++){
				        	//Log.i("NavMap","Comparing"+code+nodes.get(i).getCode());				        			
				        	if(nodes.get(i).getCode().equals(code)){
				        		positionInNodes = i;
				        	}
				        }
				        if(positionInNodes==-1){
				        	//Log.i("NavMap","Position in Nodes: "+positionInNodes);
				        }
				}
				        
			}
				        
			// if the list is sorted by Building name, find the index of the corresponding node
			else if(currentSort==2){
			        for(int i=0; i<nodes.size() ;i++){
			        	if(nodes.get(i).getBuildingName()!=null && nodes.get(i).getBuildingName().equals(s)){
				       		positionInNodes = i;
				       		break;
				       	}
				}
			}
				        
			else {
			        for(int i=0; i<nodes.size() ;i++){
			        	if(nodes.get(i).getDepartment()!=null && nodes.get(i).getDepartment().equals(s)){
			        		positionInNodes = i;
			        		break;
			        	}
			        }		        	
		        }

				        
			// If the selected action is to display the route
			if(ROUTES_ON==true){
			        try {
			        	if(lastloc!=null){		        		
			        		if(currentGpsStatus == GpsStatus.GPS_EVENT_STOPPED 
							|| lm.isProviderEnabled(provider)==false){
						        		setCurrentLocation();
					        }
					}
					else {
					        setCurrentLocation();
					}
				}
				catch(NullPointerException e){		        	
				       	setCurrentLocation();		        	
				}
					        
					        
				if(positionInNodes>=0 && positionInNodes<nodes.size())
				{

				       	drawRoute(defaultstart, positionInNodes);
				        	
				       	if(currentGpsStatus!=GpsStatus.GPS_EVENT_STOPPED || currentGpsStatus!=-1){

				        	Location l = new Location("");
				       		GeoPoint meh = nodes.get(positionInNodes).getGeoPoint();
				  	   	float x = (float) (meh.getLatitudeE6()/1E6);
					        	float y = (float) (meh.getLongitudeE6()/1E6);
					       	l.setLatitude(x);
					       	l.setLongitude(y);
					   	addProximityAlert(l,20);
					}

					mapView.getOverlays().remove(buildingsOverlay);
					buildingsOverlay.removeAll();
					        	
					Log.i("NavMap", "Clicked on "+positionInNodes);
					        	
					if(nodes.get(positionInNodes).getBuilding()!=null){
					      	buildingsOverlay.addBuilding(nodes.get(positionInNodes).getBuilding());
					       	Log.i("NavMap", "Number of geopoints at building '"+
							positionInNodes+"' is "+
							nodes.get(positionInNodes).getBuilding().
								getGeoPoints().size());
						        	
					        Log.i("NavMap", "#1 Added building at "+positionInNodes);
					        //Log.i("NavMap", "Building added asdsd" );
					    	mapView.getOverlays().add(buildingsOverlay);			        		
					        	drawRoute(defaultstart, positionInNodes);
					}
				}

			}
			else {
				mapView.getOverlays().remove(r);
			       	r = new RouteOverlay();
				mapView.getOverlays().remove(buildingsOverlay);
				mapView.getOverlays().add(r);
			       	buildingsOverlay.removeAll();
			
			       	if(positionInNodes>=0 && positionInNodes<nodes.size()){
			        	if(nodes.get(positionInNodes).getBuilding()!=null){
				        	Log.i("NavMap", "#2 Added building at "+positionInNodes);
			        		buildingsOverlay.addBuilding(nodes.get(positionInNodes).getBuilding());
			    			mapView.getOverlays().add(buildingsOverlay);			        		
			        	}
					GeoPoint geopoint = nodes.get(positionInNodes).getGeoPoint();
			        	mapController.animateTo(geopoint);
				}
			}
			tabHost.setCurrentTab(MAP_TAB);
			mapController.setZoom(19);        
		    }
		});
		
		/**
		 * Setup tabs
		 */		
		Resources res = getResources(); // obtains icons as Drawable objects
				
		tabHost.addTab(tabHost.newTabSpec("List")
			.setIndicator("Places", res.getDrawable(R.drawable.noun_project_460_1))
			.setContent(new TabContentFactory() {
		    		public View createTabContent(String arg0) {
		        		return listView;
		    		}
		})); 

		tabHost.addTab(tabHost.newTabSpec("Map")
			.setIndicator("Map", res.getDrawable(R.drawable.noun_project_96))
			.setContent(new TabContentFactory() {
		    		public View createTabContent(String arg0) {
		        		return mapView;
		    		}
	    	}));
		
		
		this.getApplicationContext();
	
		tabHost.setCurrentTab(1);		
		getProvider();

		Toast.makeText(this, "To get started, please stand outside for optimal results.", Toast.LENGTH_LONG);
	}

	
	private void setupPreferences()
	{
		SharedPreferences preferences = this.getSharedPreferences("bhamnavpreferences", MODE_PRIVATE);
		boolean antialiasing = preferences.getBoolean("antialiasing", false);
		boolean routeson = preferences.getBoolean("routeson", true);
		
		int powerSetting = preferences.getInt("power", Settings.HIGH_POWER);
		
		switch(powerSetting){
			case 1: POWER_SETTING = Criteria.POWER_HIGH;
			case 2: POWER_SETTING = Criteria.POWER_MEDIUM;
			case 3: POWER_SETTING = Criteria.POWER_MEDIUM;
		}
		
		if(routeson==true){
			ROUTES_ON = true;
		}
		else {
			ROUTES_ON = false;
		}

		if(antialiasing==true){
			ANTI_ALIASING = true;
		}
		else {
			ANTI_ALIASING = false;
		}
		
		int numberOfTimesUsed = preferences.getInt("used", 0);
		numberOfTimesUsed = numberOfTimesUsed + 1;
		
		Editor edit = preferences.edit();
		edit.putInt("used", numberOfTimesUsed);
		
		if((numberOfTimesUsed%2)==0){
			showInfo("What do you think?","Would really like some feedback");
		}
		
	}
	
	private void setupDefaultCriteria(){
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(POWER_SETTING);
		criteria.setCostAllowed(false);
		criteria.setAltitudeRequired(false);
	}
	
	
	/**
	 * Orders Places tablist by Department Name
	 */
	private void setupPlacesTabByDepartment()
	{
		
		//Log.i("NavMap", "Loading places tab by Dept. Size of graph is "+nodes.size());
		
		currentSort = 0;
		List<String> pointsList = new ArrayList<String>();
		for(int i=0;i<nodes.size();i++)
		{
			if(nodes.get(i).isVisible()){
				if(nodes.get(i).getDepartment()!=null){
					String placeName = nodes.get(i).getDepartment();
					pointsList.add(placeName);					
				}
			}
		}
		
		Collections.sort(pointsList); 
		// I learnt about the Collections class at JavaMEX website.
		// Source: http://www.javamex.com/tutorials/collections/sorting_simple_cases.shtml 26/02/11
		
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pointsList));
	}
	
	/**
	 * Orders by Places tablist by R code
	 */
	private void setupPlacesTabByRCode(){
		//Log.i("NavMap", "Loading places tab by code. Size of graph is "+nodes.size());
		
		currentSort = 1;
		List<String> pointsList = new ArrayList<String>();

		for(int i=0;i<nodes.size();i++)
		{
			if(nodes.get(i).isVisible()){
				if(nodes.get(i).getBuildingName()!=null){
					
					if(nodes.get(i).getCode()!=null || nodes.get(i).getCode().equals("")){

						String placeName = "";
						
						if(nodes.get(i).getCode().startsWith("Y")){
							String thecode = nodes.get(i).getCode();
							String removeY = thecode.substring(1);
							
							if(Integer.valueOf(removeY)<10){
								removeY = "0" + removeY;
							}
													
							placeName = "Y"+removeY + " " + nodes.get(i).getBuildingName();
							pointsList.add(placeName);

						}
						else if(nodes.get(i).getCode().startsWith("R")){
							String thecode = nodes.get(i).getCode();
							String removeR = thecode.substring(1);
							if(Integer.valueOf(removeR)<10){
								removeR = "0" + removeR;
							}							
													
							placeName = "R"+removeR + " " + nodes.get(i).getBuildingName();							
							pointsList.add(placeName);

						}
						else if(nodes.get(i).getCode().startsWith("O"))
						{
							String thecode = nodes.get(i).getCode();
							String removeO = thecode.substring(1);
							if(Integer.valueOf(removeO)<10){
								removeO = "0" + removeO;
							}							
													
							placeName = "O" + removeO + " " + nodes.get(i).getBuildingName();							
							pointsList.add(placeName);
						}
						else if(nodes.get(i).getCode().startsWith("G"))
						{
							String thecode = nodes.get(i).getCode();
							String removeG = thecode.substring(1);
							if(Integer.valueOf(removeG)<10){
								removeG = "0" + removeG;
							}							
													
							placeName = "G" + removeG + " " + nodes.get(i).getBuildingName();							
							pointsList.add(placeName);
						}
						else if(nodes.get(i).getCode().startsWith("B")){
							String thecode = nodes.get(i).getCode();
							String removeB = thecode.substring(1);
							if(Integer.valueOf(removeB)<10){
								removeB = "0" + removeB;
							}							
													
							placeName = "B" + removeB + " " + nodes.get(i).getBuildingName();							
							pointsList.add(placeName);
							
						}
												
					}
				}
			}
		}
						
		// Source: http://www.javamex.com/tutorials/collections/sorting_simple_cases.shtml 26/02/11
		Collections.sort(pointsList);
				
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pointsList));
	}
	
	/**
	 * Orders Places tab/listview by Building name
	 */
	private void setupPlacesTabByBuildingName(){
		if(DEBUG){
			Log.i("NavMap", "Loading Places tab by Building. Size of graph is "+nodes.size());			
		}
		currentSort = 2;
		List<String> pointsList = new ArrayList<String>();
		for(int i=0;i<nodes.size();i++){
			if(nodes.get(i).isVisible()){
				if(nodes.get(i).getBuildingName()!=null){
					String placeName = nodes.get(i).getBuildingName();
					pointsList.add(placeName);
				}
			}
		}		
		Collections.sort(pointsList); 
		// Source: http://www.javamex.com/tutorials/collections/sorting_simple_cases.shtml 26/02/11
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pointsList));
	}
	
	private void setupPictures(){
		try {
//		nodes.get(78).setDrawable(R.drawable.munrow);
			
		int index = getNodeIndex("Great Hall (Aston Webb)");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.great_hall);			
		}
		 index = getNodeIndex("Grounds and Gardens");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.grounds_n_gardens);
		}
		 index = getNodeIndex("Main Library");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.mainlib);
		}
		 index = getNodeIndex("Watson");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.watson);
		}
		 index = getNodeIndex("Haworth");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.haworth);
		}
		 index = getNodeIndex("Ashley");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.ashley);
		}
		 index = getNodeIndex("Poynting");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.poynting);
		}
		index = getNodeIndex("Old Gym");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.oldgym);
		}
		index = getNodeIndex("Strathcona");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.strathcona);
		}
		index = getNodeIndex("Barber Institute of Fine Arts");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.barber);
		}
		
		index = getNodeIndex("Hills");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.hills);
		}
		
		index = getNodeIndex("Biosciences");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.biosciences);
		}
		index = getNodeIndex("Frankland");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.frankland);
		}

		index = getNodeIndex("Frankland");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.frankland);
		}
		
		index = getNodeIndex("University Centre");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.unicentre);
		}
		index = getNodeIndex("Education");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.education);
		}
		index = getNodeIndex("Law");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.law);
		}
		index = getNodeIndex("Guild of Students");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.guild);
		}
		index = getNodeIndex("Chemical Engineering");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.chem_west);
		}
		index = getNodeIndex("Sports and Exercise Sciences");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.sports);
		}
		index = getNodeIndex("Maintenance");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.maintenance_building);		
		}
		index = getNodeIndex("Mechanical Engineering");
		if(index>=0){ 
			nodes.get(index).setDrawable(R.drawable.mech_eng);
		}
		index = getNodeIndex("Computer Science");
		if(index>=0){
			nodes.get(0).setDrawable(R.drawable.compscience);
		}
		index = getNodeIndex("Learning Centre");
		if(index>=0){
			nodes.get(index).setDrawable(R.drawable.learningcentre);
		}
			
		}
		catch(IndexOutOfBoundsException e){
			// in case another graph is loaded, other than the default one
		}
	}
	
	
	private void loadBuildings(){
		LoadBuildings lb = new LoadBuildings(nodes, buildingsOverlay, this);
		lb.load();
	}
	
	/**
	 * Replaces createTouchableOverlays
	 */ 
	private void createTouchableOverlays()
	{
		setupPictures();
		Drawable redDrawable = this.getResources().getDrawable(R.drawable.red_node);
		Drawable yelDrawable = this.getResources().getDrawable(R.drawable.yellow_node);
		Drawable greDrawable = this.getResources().getDrawable(R.drawable.green_node);
		Drawable bluDrawable = this.getResources().getDrawable(R.drawable.blue_node);
		Drawable oraDrawable = this.getResources().getDrawable(R.drawable.orange_node);
		NodeOverlay nodeOverlay = new NodeOverlay(redDrawable, this);
		NodeOverlay YnodeOverlay = new NodeOverlay(yelDrawable, this);
		NodeOverlay GnodeOverlay = new NodeOverlay(greDrawable, this);
		NodeOverlay BnodeOverlay = new NodeOverlay(bluDrawable, this);
		NodeOverlay OnodeOverlay = new NodeOverlay(oraDrawable, this);
		int numberOfReds = 0;
		int numberOfYellows = 0;
		int numberOfGreens = 0;
		for(int i=0; i<nodes.size(); i++)
		{
			Node n = nodes.get(i);
			if(n.isVisible()){
				String desc = n.getDescription();
				GeoPoint g = n.getGeoPoint();
				if(desc==null){
					desc = "No information currently available.";
					n.setDescription(desc);
				}

				OverlayItem overlayitem = new OverlayItem(g, n.getBuildingName(), desc);				
				
				if(n.getCode().startsWith("Y")){
					YnodeOverlay.addNode(overlayitem, n.getDrawable());
					// second parameter passes in R.drawable.bg resource
					numberOfYellows++;
				}
				
				else if(n.getCode().startsWith("G")){
					GnodeOverlay.addNode(overlayitem, n.getDrawable());
					numberOfGreens++;
				}
				
				else if(n.getCode().startsWith("B")){
					BnodeOverlay.addNode(overlayitem, n.getDrawable());
				}
				
				else if(n.getCode().startsWith("O")){
					OnodeOverlay.addNode(overlayitem,n.getDrawable());
				}
				
				else {
					nodeOverlay.addNode(overlayitem, n.getDrawable());
					numberOfReds++;
				}
				
			}
		}

		mapView.getOverlays().add(nodeOverlay);	
		mapView.getOverlays().add(YnodeOverlay);
		mapView.getOverlays().add(GnodeOverlay);
		mapView.getOverlays().add(BnodeOverlay);
		mapView.getOverlays().add(OnodeOverlay);

	}
	
	public void createTouchableOverlays2(){
		Drawable redDrawable = this.getResources().getDrawable(R.drawable.red_node);
		NodeOverlay nodeOverlay = new NodeOverlay(redDrawable, this);
		for(int i=0; i<nodes.size(); i++)
		{

		Node n = nodes.get(i);
		if(n.isVisible()){
			String desc = n.getDescription();
			GeoPoint g = n.getGeoPoint();
			if(desc==null){
				desc = "No information currently available.";
				n.setDescription(desc);
			}
			OverlayItem overlayitem = new OverlayItem(g, n.getBuildingName(), desc);
			nodeOverlay.addNode(overlayitem);
		}

		}
		mapView.getOverlays().add(nodeOverlay);

	}

	public void getProvider(){
		if(DEBUG){
			Log.i("NavMap", "Attempting to retrieve provider");			
		}
		dialog.show();
			
		try {
			provider = LocationManager.GPS_PROVIDER;
			lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			if(provider!=null){
				lm.requestLocationUpdates(provider, WAIT,MIN_DISTANCE, this);
				if(DEBUG){
					Log.i("NavMap", "Initial location requested");
				}
				lastloc = lm.getLastKnownLocation(provider);

			}

			dialog.dismiss();
		}

		catch(NullPointerException er){
			dialog.dismiss();
			provider = LocationManager.GPS_PROVIDER;
		}
	}
		
	public void verifyAndSetupGraph(){
		@SuppressWarnings("unchecked")
		ArrayList<Node> nodes = (ArrayList<Node>) this.getIntent().getSerializableExtra("graph");
		
		if(nodes.size()>0){
			
		}
	}
		
	public void showToast(int defaultstart){
    	Toast.makeText(this, "Nearest node is "+defaultstart, Toast.LENGTH_LONG);
	}
		
	public void forceUpdateGPS(){
		dialog.show();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(provider!=null){
			lm.requestLocationUpdates(provider, WAIT,MIN_DISTANCE, this);
			Toast.makeText(this, "Waiting for location", Toast.LENGTH_SHORT).show();
			lastloc = lm.getLastKnownLocation(provider);			
		}
		dialog.dismiss();
	}

	@Override 
	protected boolean isRouteDisplayed() {
		return false;
	}

	
	private boolean shown = false;
	@Override
	public void onLocationChanged(Location location) {
		
		try {			
			if(location.distanceTo(CAMPUS)>CAMPUS_DIAMETER){
				if(shown==false){
					showInfo("Not on Campus",
							"You are currently not on " +
							"campus. " + "To benefit from " +
							"the full experience of BhamNav, " +
							"you must " +"be in range. You " +
							"may continue to use this app " +
							"with limited functionality.");
					shown = true;
				}
				return ;
			}
			
			GeoPoint temp = new GeoPoint(
					(int)(location.getLatitude()*1E6),
					(int) (location.getLongitude()*1E6));
			if(currentGpsStatus!=GpsStatus.GPS_EVENT_STOPPED){
				mapController.animateTo(temp);	
			}			
			locationOverlay.updateLocation(temp);
			lastloc = location;
			findNearestNode();
			checkProximity(location);						
		}		
		catch(NullPointerException e){
			if(DEBUG){
				showInfo("NullPointerException","Message: "+e.getMessage());				
			}
		}
	}

	private boolean GPS_ON = true;
	
	@Override
	public void onProviderDisabled(String arg0) {
		GPS_ON = false;
		AlertDialog.Builder buildDialog = new AlertDialog.Builder(this);
		buildDialog.setMessage("Your GPS device is currently turned off. Turn on?");
		buildDialog.setCancelable(false);
		buildDialog.setPositiveButton("Yes", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent checksettings = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);				
				startActivity(checksettings);
				GPS_ON = true;
			}});
		
		buildDialog.setNegativeButton("No", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				lm.removeUpdates(NavMap.this);
				GPS_ON = false;
			}});
		buildDialog.create().show();

	}

	@Override
	public void onProviderEnabled(String arg0) {
		GPS_ON = true;
		Toast.makeText(this, "Provider enabled.", Toast.LENGTH_SHORT).show();	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
		switch(status){
			
		case 1:
			Toast.makeText(this, "GPS Started", Toast.LENGTH_SHORT).show();
			GPS_ON = true;
			mapView.getOverlays().add(locationOverlay);
			
			break;
		case 2:
			Toast.makeText(this, "GPS Stopped", Toast.LENGTH_SHORT).show();
			mapView.getOverlays().remove(locationOverlay);
			GPS_ON = false;

			break;
		case 3:
			Toast.makeText(this, "GPS has located you.", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{	
			return super.onCreateOptionsMenu(menu);
	}
		
	
	private boolean ROUTES_ON = true;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		if(tabHost.getCurrentTab()==0){
			menu.clear();
			menu.add("Sort by Dept").setIcon(R.drawable.department);
			menu.add("Sort by Code").setIcon(R.drawable.rcode);
			menu.add("Sort by Building").setIcon(R.drawable.ic_menu_building);
		}   
		else if (tabHost.getCurrentTab()==2){
			menu.clear();
			menu.add("Add Event").setIcon(R.drawable.event);
			menu.add("Set Degree").setIcon(R.drawable.degree);
		}
		else {
			menu.clear();
			
			boolean providerEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			if(GPS_ON==false){
				menu.add("Find My Location").setIcon(R.drawable.ic_menu_mylocation);				
			}
			if(GPS_ON==true){
				menu.add("Stop GPS").setIcon(R.drawable.noun_project_42);
			}
			

			menu.add("Select Current Location").setIcon(R.drawable.selectloc); 
			
			if(DEBUG==true){
				menu.add("Show all").setIcon(R.drawable.enduser);
			}
			
			if(ROUTES_ON){
				menu.add("Routes OFF").setIcon(R.drawable.ic_menu_mapmode);
			}
			else {
				menu.add("Routes ON").setIcon(R.drawable.ic_menu_mapmode);
			}
			
			menu.add("About").setIcon(R.drawable.testicon2);			
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Button for Force Update GPS		
		if (item.getTitle().equals("Find My Location")) {
			forceUpdateGPS();
			GPS_ON = true;
			tabHost.setCurrentTab(1);
		}
		
		if(item.getTitle().equals("Set Degree")){
			Intent selectdegree = new Intent(NavMap.this, SelectDegree.class);
			this.startActivity(selectdegree);
		}
		
		if(item.getTitle().equals("Sort by Dept")){
			setupPlacesTabByDepartment();
		}
		
		if(item.getTitle().equals("Show all")){
			mapView.getOverlays().remove(r);
			FindShortestPath path = new FindShortestPath(nodes);
			ArrayList<ArrayList<GeoPoint>> graph = path.getAllConnections();
			for(ArrayList<GeoPoint> list: graph){
				RouteOverlay a = new RouteOverlay();
				a.setPath(list);
				mapView.getOverlays().add(a);
			}
			mapView.getOverlays().add(r);
		}
		
		if(item.getTitle().equals("Sort by Code")){
			setupPlacesTabByRCode();
		}
		
		if(item.getTitle().equals("Sort by Building")){
			setupPlacesTabByBuildingName();
		}
		
		if (item.getTitle().equals("Stop GPS")) {
			try {
				lm.removeUpdates(this);
				GPS_ON = false;
				Toast.makeText(this, "GPS stopped", Toast.LENGTH_SHORT).show();
			}
			catch(NullPointerException e){
				//Log.i("NavMap", "When you forced GPS to stop, there was no provider to shutdown");
			}
		}
		
		if(item.getTitle().equals("Routes OFF")){
			ROUTES_ON = false;
			SharedPreferences preferences = getSharedPreferences("bhamnavpreferences", MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putBoolean("routeson", false);
			editor.commit();

		}
		if(item.getTitle().equals("Routes ON")){
			ROUTES_ON = true;
			SharedPreferences preferences = getSharedPreferences("bhamnavpreferences", MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putBoolean("routeson", true);
			editor.commit();
		}
					
		if(item.getTitle().equals("Settings")){
			this.startActivity(new Intent(NavMap.this, Settings.class));
		}
		
		if(item.getTitle().equals("Select Current Location"))
		{
			setCurrentLocation();
		}
		
		if (item.getTitle().equals("About")) {
			Intent launchweb = new Intent(Intent.ACTION_VIEW);
			launchweb.setData(Uri.parse("http://studentweb.cs.bham.ac.uk/~axs911/android.html"));
			//http://stackoverflow.com/
			//questions/3004515/android-sending-an-intent-to-browser-to-open-specific-url
			startActivity(launchweb);
		}		
		return super.onOptionsItemSelected(item);
	}
	
	private void setCurrentLocation() {

		ArrayList<String> optionslist = new ArrayList<String>();
		for(Node n: nodes){
			if(n.isVisible()){
				optionslist.add(n.getBuildingName());				
			}
		}
		Collections.sort(optionslist);
		final String[] finaloptions = new String[optionslist.size()];
		for(int m=0;m<finaloptions.length;m++){
			finaloptions[m] = optionslist.get(m);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Where are you?");
		
		/**
		 * Source: http://developer.android.com/guide/topics/ui/dialogs.html
		 */

		builder.setSingleChoiceItems(finaloptions, -1, new DialogInterface.OnClickListener() {
			// -1 means there shouldn't be a default
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(which<finaloptions.length){
					for(int i=0; i<nodes.size();i++){
						if(finaloptions[which].equals(nodes.get(i).getBuildingName())){
							defaultstart = i;
							break;
						}
					}
				}
				dialog.dismiss();
				mapController.animateTo(nodes.get(defaultstart).getGeoPoint());
		        drawRoute(defaultstart, currentdestination);
								
			}
		});
		AlertDialog optionDialog = builder.create();
		optionDialog.show();
		
	}

	public void showInfo(String title, String message) {
		myAlert = new AlertDialog.Builder(this).create();
		myAlert.setTitle(title);
		myAlert.setMessage(message);
		myAlert.setCancelable(false);
		myAlert.setButton("Close", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				myAlert.dismiss();
			}
		});
		myAlert.show();
	}
	
	private int counter = 0;
	
	public void drawRoute(int origin, int destination){
		
		if(origin<0 || destination<0){
			return ;
		}
		
    		mapView.getOverlays().remove(r);
    		currentdestination = destination;
    		r = new RouteOverlay();
    		FindShortestPath path = new FindShortestPath(nodes);
    		ArrayList<GeoPoint> result = path.drawDijkstra(origin, destination);
    	
    		if(result.size()>0){
			//Log.i("NavMap", "Switched to tab1");
    			r.setPath(result);    		
    		}

    		mapView.getOverlays().add(r);
    	
    		counter = counter + 1;
    	    	    	
	}
		
	@Override
	protected void onResume() {
		try {
			String canihasprovider = lm.getBestProvider(criteria, true);
			if(canihasprovider==null || canihasprovider.equalsIgnoreCase("")){
				provider = null;
			}
			if(provider!=null){
				lm.requestLocationUpdates(provider, 0l, 0f, this);								
			}


		}
		catch(IllegalArgumentException ill){
			provider = null;
		}
		catch(SecurityException sec){
			Toast.makeText(this, 
			"Security Warning: The app isn't allowed to access location provider",
			Toast.LENGTH_LONG);
		}
		catch(NullPointerException e){
			//Log.i("NavMap","When you tried to resume the app, you tried to request
			// location, but it returned null");
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		try {
			lm.removeUpdates(this);
		}
		catch(NullPointerException e){
			//Log.i("NavMap", "When you tried to pause the app, you tried
			// to shutdown the GPS but there was no provider to shutdown");
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// finish();
		try {
			lm.removeUpdates(this);
		}
		catch(NullPointerException e){
			//Log.i("NavMap", "When you tried to close the app, you tried to shutdown the GPS but there was no provider to shutdown");		
		}

		super.onStop();
	}

	
	private int currentGpsStatus = -1;
	
	@Override
	public void onGpsStatusChanged(int arg0) {
		currentGpsStatus = arg0;
		
		if(arg0==GpsStatus.GPS_EVENT_FIRST_FIX){
			Toast.makeText(this, "GPS has found your current location.", Toast.LENGTH_SHORT).show();
		}
		if(arg0==GpsStatus.GPS_EVENT_STOPPED){
			Toast.makeText(this, "GPS Stopped", Toast.LENGTH_SHORT).show();
			mapView.getOverlays().remove(locationOverlay);
		}
		if(arg0==GpsStatus.GPS_EVENT_STARTED){
			Toast.makeText(this, "GPS Started.", Toast.LENGTH_SHORT).show();
			mapView.getOverlays().add(locationOverlay);
		}
	}
	
	/*
	 * Finds the closest nodes and updates the displayed route on the MapView
	 */
	public void findNearestNode(){
		FindShortestPath f = new FindShortestPath(nodes);
		if(DEBUG==true){
			Log.i("NavMap", "Trying to find nearest node");			
		}
		
		try {				
			int thenode = f.findNearestNode(lastloc);
			if(thenode>=0 && thenode<f.getNodesArray().length){
		        defaultstart = thenode;
		        drawRoute(thenode, currentdestination);
			} 
		}
		catch(NullPointerException er){
			if(DEBUG==true){
				Log.i("NavMap", "Caught null in findNearestNode: "+er.toString());
			}
		}
	}
	
	/*
	 * Returns the index of the node which has the same building name as the provided parameter
	 */
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
