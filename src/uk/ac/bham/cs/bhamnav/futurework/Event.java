package uk.ac.bham.cs.bhamnav.futurework;

import java.io.Serializable;

import android.text.format.Time;

public class Event implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6271364378718527172L; // generated ID for the Serializable
	String name;
	String room;
	String building;
	Time startTime;
	int duration;
	
	int hour;
	int minutes;
	int dayofmonth;
	int month;
	int year;
	
	
	/*
	 * Time class is not Serializable, so will have to do with
	 * alternate means
	 */
	
	public Event(String lecturename, int h, int min, int dayofM, int mont, int yr, int durationinminutes, String lectureroom, String lecturebuilding){
		
		// do some data verification/validation		
		name = lecturename;
		hour = h;
		minutes = min;
		dayofmonth = dayofM;
		month = mont;
		year = yr;
		duration = durationinminutes;
		room = lectureroom;
		building = lecturebuilding;
	}


}