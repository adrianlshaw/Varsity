package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;
import java.util.Stack;

import android.location.Location;
import com.google.android.maps.GeoPoint;

/**
 * @author Adrian Shaw
 *
 */

public class FindShortestPath {

	public static final int NOTEXIST = -1;
	public static final int IF = Integer.MAX_VALUE;
	public static int NUM_VERTICES; // was 19,24,26,28,
	public static final int first = 0;
	public static int last;
	private String[] strings;
	private Node[] nodes = new Node[NUM_VERTICES];

	private long overallDistance = 0;
	
	public long getOverallDistance(){
		return overallDistance;
	}
	
	
	public Node[] getNodesArray(){
		return nodes;
	}
	

	public FindShortestPath(ArrayList<Node> newNodes)
	{
				
		int numberofnodes = newNodes.size();
		NUM_VERTICES = numberofnodes;
		last = NUM_VERTICES-1;
		distance = new int[NUM_VERTICES];
		tight = new boolean[NUM_VERTICES];
		predecessor = new int[NUM_VERTICES];		
		
		Node[] temp = new Node[NUM_VERTICES];
		String[] names = new String[NUM_VERTICES];
		
		/*
		 * Convert to regular array of Nodes
		 */
		for(int i=0; i<newNodes.size();i++)
		{
			temp[i] = newNodes.get(i);
			names[i] = newNodes.get(i).getName();	
		}
		
		this.nodes = temp;
		this.strings = names;
		
		createBlankGraph();

		// go through all the nodes
		for(int i=0; i<this.nodes.length; i++)
		{
			Node currentNode = this.nodes[i]; // get the current node in the array
			String[] connections = this.nodes[i].getConnectedTo(); // and get its connections
			
			// for each of the connections it has
			
			if(connections!=null){
				for(int j=0; j<connections.length; j++){
					Node connectedNode = lookup(connections[j]); // get the connection 
					if(connectedNode!=null && currentNode!=null){
						addConnection(currentNode, connectedNode); 	// add connection between 
																	// current node and its connection
		//				//Log.i("NavMap", "Connection added");
					}
					else {
		//				//Log.i("NavMap", "No connection added. ConnectedNode="+connectedNode+" CurrentNode="+currentNode);
					}
				}				
			}			
		}		
	}
	
	private Node lookup(String nodeName){
		
		for(int i=0; i<nodes.length; i++)
		{
			//Log.i("NavMap", "Comparing "+nodeName + " with "+strings[i]);
			
			if(strings[i].equalsIgnoreCase(nodeName)){
				return nodes[i];
			}
		}
		return null;
	}
		
	public boolean addConnection(Node a, Node b){
		
		if(a.getNodeID().equals(b.getNodeID())){
			return false;
		}
		if(a.equals(b)){
			return false;
		}

		int positiona = -1;
		int positionb = -1;
		for(int i=0; i<strings.length; i++){
			if(strings[i].equals(a.getNodeID())){
				positiona = i;
			}
			if(strings[i].equals(b.getNodeID())){
				positionb = i;
			}
		}
		
		if(positiona==-1 || positionb ==-1){
			return false;
		}
		
		Location loca = new Location("");
		float lata = a.getGeoPoint().getLatitudeE6();
		float longa = a.getGeoPoint().getLongitudeE6();
		loca.setLatitude(lata);
		loca.setLongitude(longa);
		
		Location locb = new Location("");
		float latb = b.getGeoPoint().getLatitudeE6()/1000000;
		float longb = b.getGeoPoint().getLongitudeE6()/1000000;
		locb.setLatitude(latb);
		locb.setLongitude(longb);
		
		int distance = (int) loca.distanceTo(locb);
		
		weight[positiona][positionb] = distance;
		weight[positionb][positiona] = distance;
		
		return true;
	}	
	
	public int findNearestNode(Location l){
						
		int numberofnearest = 0;
		
		if(nodes.length>2){
			
			Location temp;
			
			float shortestdistancesofar = Float.MAX_VALUE;

			int iter = 0;
			try {
					
			for(int i=0; i<nodes.length; i++){
						
				iter = i;
				temp = new Location("");
								
				temp.setLatitude(nodes[i].getGeoPoint().getLatitudeE6()/1E6);
				temp.setLongitude(nodes[i].getGeoPoint().getLongitudeE6()/1E6);

				
				if(l.distanceTo(temp)<shortestdistancesofar){
					shortestdistancesofar = l.distanceTo(temp);
					numberofnearest = i;
				}
				else {
	//				//Log.i("NavMap", "More than "+i+" "+l.distanceTo(temp));
				}
			}
			}
			catch(NullPointerException er){
				//Log.i("NavMap","Caught null in findnearestnode at iteration #"+iter);
				return -1;
			}
		}
		
		//Log.i("NavMap", "Found nearest node"+numberofnearest);
		return numberofnearest;		
	}
	
	private int weight[][];
	
	private void createBlankGraph(){
		weight = new int[NUM_VERTICES][NUM_VERTICES];
		
		for(int i=0; i<weight.length; i++){
			for(int j=0; j<weight.length;j++){
				weight[i][j] = IF;
				if(i==j){
					weight[i][j]=0;
				}
			}
		}
		
	}
	
	int[] distance = new int[NUM_VERTICES];
	boolean[] tight = new boolean[NUM_VERTICES];
	int[] predecessor = new int[NUM_VERTICES];
	
	/**
	 * Adapted from Matt Smart's implementation of Dijkstra's algorithm (March 23, 2007)
	 * (mjs@cs.bham.ac.uk)
	 * Source:http://www.cs.bham.ac.uk/~mmk/teaching/Foundations/Dijkstra.java
	 */
	private int minimalNontight(){
		int j;
		int u;
		
		for(j=first; j<last; j++){
			if(!tight[j]){
				break;
			}
		}
		
		u = j;
		
		for(j++; j<=last; j++){
			if(!tight[j] && distance[j] < distance[u] ){
				u = j;
			}
		}
		return u;		
	}
	
	private boolean successor(int u, int z){
		return ((weight[u][z]!=IF) && u!=z);
	}
	
	/**
	 * Adapted from Matt Smart's implementation of Dijkstra's algorithm (March 23, 2007)
	 * (mjs@cs.bham.ac.uk)
	 * Source:http://www.cs.bham.ac.uk/~mmk/teaching/Foundations/Dijkstra.java
	 * @param s
	 */
	public void dijkstra(int s){

		distance[s] = 0;
		
		int z = 0;
		int u = 0;
		
		for(z=first; z<=last;z++){
			if(z!=s){
				distance[z] = IF;
			}
			tight [z] = false;
			predecessor[z] = NOTEXIST; // mark as non-existent
		}
		
		for(int i = 0; i<NUM_VERTICES; i++){
			u = minimalNontight();
			tight[u] = true;
			
			for(z=first; z<=last; z++){
				if(successor(u,z) && !tight[z] && (distance[u]+weight[u][z]<distance[z])){
					distance[z] = distance[u] + weight[u][z];
					predecessor[z] = u;
				}
			}
		}
	}
	
	/**
	 * Adapted from Matt Smart's implementation of Dijkstra's algorithm (March 23, 2007)
	 * (mjs@cs.bham.ac.uk)
	 * Source:http://www.cs.bham.ac.uk/~mmk/teaching/Foundations/Dijkstra.java
	 * @param origin
	 * @param destination
	 * @return
	 */
	public ArrayList<GeoPoint> drawDijkstra(int origin, int destination){

		//Log.i("NavMap", "Finding shortest path");
		//Log.i("NavMap", "Popping things off the stack");		
		
		if(origin==NOTEXIST || destination==NOTEXIST){
			// do nothing
		}
		
		dijkstra(origin);
		Stack<Integer> st = new Stack<Integer>();
		
		for(int v = destination; v!=origin; v=predecessor[v]){
			if(v==NOTEXIST){
				// 
				return new ArrayList<GeoPoint>();
			}
			else {
				st.push(v);
			}
		}
		
		st.push(origin);
		
		ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
		overallDistance = 0;
		int start = origin;
		
		
		while(!st.empty()){
			// pop shortest path off the stack			
			int result = st.pop();
			
			//Log.i("NavMap", "R:"+result);
			list.add(nodes[result].getGeoPoint());
			overallDistance = overallDistance + this.getDistance(start, result);
			start = result;
		}
		//Log.i("NavMap", ""+this.getDistance(35, 24));
		//Log.i("NavMap", "36:"+nodes[36].getGeoPoint().getLatitudeE6()+" "+nodes[36].getGeoPoint().getLongitudeE6());

		overallDistance(list);
		//Log.i("NavMap", "Found shortest path");
		return list;
	}
	
	public double overallDistance(ArrayList<GeoPoint> list){
		
		Location a = new Location("");
		Location b = new Location("");
		
		int distanceAccumulator = 0;
		
		for(int i=0;i<list.size()-1;i++)
		{
			a.setLongitude(list.get(i).getLongitudeE6());
			a.setLatitude(list.get(i).getLatitudeE6());
			b.setLongitude(list.get(i+1).getLongitudeE6());
			b.setLatitude(list.get(i+1).getLatitudeE6());
			
			distanceAccumulator = distanceAccumulator + (int) a.distanceTo(b);	
		}
		
		//Log.i("NavMap", "Overall distance = "+distanceAccumulator);
		
		return distanceAccumulator;
	}
	
	public int getDistance(int start, int dest){
		return weight[start][dest];
	}
	
	public void removeConnection(int nodea, int nodeb){
		weight[nodea][nodeb] = 0;
		weight[nodeb][nodea] = 0;		
	}
	
	public ArrayList<ArrayList<GeoPoint>> getAllConnections(){
		ArrayList<ArrayList<GeoPoint>> points = new ArrayList<ArrayList<GeoPoint>>(); 
		for(Node n: nodes){
			String[] connections = n.getConnectedTo();
			if(connections!=null){
				for(String s: connections){
					int result = lookupName(s);
					if(result>=0){
						ArrayList<GeoPoint> temp = new ArrayList<GeoPoint>();
						temp.add(nodes[result].getGeoPoint());
						temp.add(n.getGeoPoint());
						points.add(temp);
					}
				}
				
			}
		}
		return points;
	}
	
	public int lookupName(String name){
		int counter = 0;
		for(Node n: nodes){
			if(n.getName().equalsIgnoreCase(name)){
				return counter; 
			}
			counter++;
		}
		return -1;
	}
	
}