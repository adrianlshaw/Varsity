package uk.ac.bham.cs.bhamnav.futurework;

import java.io.Serializable;
import java.util.ArrayList;


public class Day implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4642976559278562622L; // generated
	ArrayList<Event> events = new ArrayList<Event>();
	
	public void addEvent(Event e){
		events.add(e);
	}
	
	public void getEvent(int i){
		events.get(i);
	}
}