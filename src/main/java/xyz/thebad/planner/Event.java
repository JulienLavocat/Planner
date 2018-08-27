package xyz.thebad.planner;

import java.sql.Date;
import java.sql.Timestamp;

public class Event {

	public Timestamp start;
	public Timestamp end;
	public String name;
	public String location;
	
	public Event(Timestamp start, Timestamp end, String name,String location) {
		this.start = start;
		this.end = end;
		this.name = name;
		this.location = location;
	}
	
	public Event() {
		
	}
	
	@Override
	public String toString() {
		return start + ": " + name + " | In: " + location;
	}
	
}
