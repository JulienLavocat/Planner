package xyz.thebad.planner.http;

import xyz.thebad.planner.Event;

public class EventView {

	public int id;
	public String start_date;
	public String end_date;
	public String text;
	
	public EventView(int id, Event e) {
		
		this.id = id;
		this.start_date = e.start.toString().replace("-", "/");
		this.end_date = e.end.toString().replace("-", "/");;
		this.text = e.name;
	}
	
}
