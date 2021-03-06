package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;

import android.location.Location;

/** lols
 * This implements a very basic adaptation of the Kalman filter.
 * It remembers the last several locations that you were in
 * and the time at each location.
 * It then determines your average speed and estimates how long
 * it will take you to reach your location.
 * @author Adrian Shaw
 */
public class KalmanFilter {

	public static final int MEMORY = 5;
	public static final int NOT_ENOUGH_POINTS = -1;
	
	ArrayList<Location> points;
	ArrayList<Long> times;
	
	public KalmanFilter(){
		points = new ArrayList<Location>();
		times = new ArrayList<Long>();
	}
	
	public void addPoint(Location point){
		
		Long t = System.currentTimeMillis();
		
		if(points.size()>MEMORY)
		{
			points.remove(0);
			times.remove(0);
			points.add(point);
			times.add(t);
			
		}
		else {
			points.add(point);
			times.add(t);
		}	
	}
	
	/**	Estimates how long it will take to reach a location.
	 * @param distanceInMeters	the total distance to destination in meters
	 * @return	the estimated time in seconds
	 */
	public float estimateTimeOfArrival(long distanceInMeters)
	{
		float averageSpeed = calculateAverageSpeed();
		float time = distanceInMeters / averageSpeed;		
		return time;
	}
	
	/** Calculates average speed of user, based on contents of array.
	 * @return	The average speed (m/sec)
	 */
	public float calculateAverageSpeed(){
		
		if(points.size()>MEMORY && (times.size()==points.size())){
			
			float distance = 0;
			for(int i=0;i<points.size()-1;i++)
			{
				Location a = new Location("");
				Location b = new Location("");
				
				a.setLatitude(points.get(i).getLatitude());
				a.setLongitude(points.get(i).getLongitude());
				b.setLatitude(points.get(i+1).getLatitude());
				b.setLongitude(points.get(i+1).getLongitude());
	
				distance = distance + a.distanceTo(b);
			}
			
			long startTime = times.get(0);
			long endTime = times.get(times.size()-1);			
			long totalTime = endTime - startTime;
			
			return (distance / (totalTime/1000));
			
		}
		else {
			return NOT_ENOUGH_POINTS;
		}
	}
}