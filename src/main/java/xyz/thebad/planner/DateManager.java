package xyz.thebad.planner;

import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map.Entry;

public class DateManager {
	
	public static HashMap<Integer, Time> hours;
	public static HashMap<Pai, String> locs = new HashMap<Pai, String>();
	
	public static void start() throws SQLException {
		
		hours = DB.getHours();
		
		locs.put(new Pai(9, 10), "Amphi A4");
		locs.put(new Pai(9, 14), "Amphi A10");
		locs.put(new Pai(9, 20), "Amphi A3");
		locs.put(new Pai(9, 26), "Amphi A3");
		locs.put(new Pai(10, 2), "Amphi A3");
		locs.put(new Pai(10, 8), "Amphi A9");
		locs.put(new Pai(10, 12), "Amphi A9");
		locs.put(new Pai(10, 18), "Amphi A4");
		locs.put(new Pai(10, 24), "Amphi A10");
		locs.put(new Pai(10, 30), "Amphi A3");
		locs.put(new Pai(11, 7), "Amphi A3");
		locs.put(new Pai(11, 13), "Amphi A3");
		locs.put(new Pai(11, 19), "Amphi A9");
		locs.put(new Pai(11, 23), "Amphi A9");
		
	}
	
	public static Time getHour(int i) {
		return hours.get((i > hours.size()) ? hours.size() : i);
	}
	
	public static String getLocation(int month, int day) {
		Pai asked = new Pai(month, day);
		
		for(Pai p : locs.keySet()) {
			if(!p.equals(asked))
				continue;
			System.out.println(locs.get(asked));
			return locs.get(asked);
		}
		return "";
	}
}
