package xyz.thebad.planner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import io.sentry.Sentry;

public class DB {

	private static Connection con;
	private static MysqlDataSource src;

	public static PreparedStatement INSERT_EVENT;
	public static PreparedStatement INSERT_TIME;
	public static PreparedStatement SELECT_DAY_EVENTS;
	public static PreparedStatement SELECT_WEEK_EVENTS;
	public static PreparedStatement SELECT_MONTH_EVENTS;
	public static PreparedStatement SELECT_ALL_EVENTS;

	public static void start() throws SQLException {

		src = new MysqlDataSource();
		src = new MysqlDataSource();
		src.setServerName("thebad.xyz");
		src.setUser("planner");
		src.setPassword("220100jlplanner");
		src.setDatabaseName("planner");
		src.setAutoReconnect(true);

		con = src.getConnection();

		SELECT_DAY_EVENTS = getConnection().prepareStatement("SELECT start, end, name, location FROM events WHERE DATE(start) = ?");
		SELECT_WEEK_EVENTS = getConnection().prepareStatement("SELECT start, end, name, location FROM events WHERE YEARWEEK(start, 1) = YEARWEEK(?, 1) AND YEAR(start) = YEAR(DATE(?))");
		SELECT_MONTH_EVENTS = getConnection().prepareStatement("SELECT start, end, name, location FROM events WHERE MONTH(start) = MONTH(DATE(?)) AND YEAR(start) = YEAR(DATE(?))");
		SELECT_ALL_EVENTS = getConnection().prepareStatement("SELECT start, end, name, location FROM events");
		
		 Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				keepAlive();
			}
			
		 }, 0, 1, TimeUnit.HOURS);
		
	}
	
	public static synchronized Connection getConnection() {
		return con;
	}

	public static HashMap<Integer, Time> getHours() {

		checkConnection();

		HashMap<Integer, Time> hours = new HashMap<Integer, Time>();

		try {

			ResultSet rs = getConnection().createStatement().executeQuery("SELECT * FROM day");

			while(rs.next()) {
				hours.put(rs.getInt(1), rs.getTime(2));
			}

		} catch (SQLException e) {
			Sentry.capture(e);
		}

		return hours;
	}

	public static ArrayList<Event> getDayEvents(String date) {

		checkConnection();
		try {
			SELECT_DAY_EVENTS.setString(1, date);
			ResultSet rs = SELECT_DAY_EVENTS.executeQuery();
			return toEventList(rs);
		} catch (SQLException e) {
			Sentry.capture(e);
			e.printStackTrace();
			return new ArrayList<Event>();
		}
	}

	public static ArrayList<Event> getWeekEvents(String cd) {

		checkConnection();
		try {
			SELECT_WEEK_EVENTS.setString(1, cd);
			SELECT_WEEK_EVENTS.setString(2, cd);
			ResultSet rs = SELECT_WEEK_EVENTS.executeQuery();
			return toEventList(rs);
		} catch (SQLException e) {
			Sentry.capture(e);
			e.printStackTrace();
			return new ArrayList<Event>();
		}

	}

	public static ArrayList<Event> getMonthEvents(String cd) {
		try {		
			SELECT_MONTH_EVENTS.setString(1, cd);
			SELECT_MONTH_EVENTS.setString(2, cd);
			System.out.println(cd);
			ResultSet rs = SELECT_MONTH_EVENTS.executeQuery();
			return toEventList(rs);
		} catch (SQLException e) {
			Sentry.capture(e);
			e.printStackTrace();
			return new ArrayList<Event>();
		}
	}
	
	public static ArrayList<Event> getAllEvents() {
		try {		
			checkConnection();
			ResultSet rs = SELECT_ALL_EVENTS.executeQuery();
			return toEventList(rs);
		} catch (SQLException e) {
			Sentry.capture(e);
			e.printStackTrace();
			return new ArrayList<Event>();
		}
	}

	public static ArrayList<Event> toEventList(ResultSet rs) {
		ArrayList<Event> events = new ArrayList<Event>();
		try {
			while(rs.next()) {
				Event e = new Event();
				e.start = rs.getTimestamp(1);
				e.end = rs.getTimestamp(2);
				e.name = rs.getString(3);
				e.location = rs.getString(4);
				events.add(e);	
			}
		} catch (SQLException e) {
			Sentry.capture(e);
			e.printStackTrace();
		}
		return events;
	}

	public static void updateEvents() {

		//		try {
		//			PreparedStatement ps = getConnection().prepareStatement("INSERT INTO events2 (start, end, name, location) VALUES (?, ?, ?, ?)");
		//			ResultSet rs = getConnection().createStatement().executeQuery("SELECT day, month, start, end, name, location FROM events");
		//			ArrayList<Event> ev = new ArrayList<Event>(); 
		//			while(rs.next()) {
		//				Event e = new Event(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6));
		//				ev.add(e);
		//			}
		//			
		//			int year = 2018-1900;
		//			for(Event e : ev) {
		//				if(e.end == 13)
		//					e.end = 12;
		//				
		//				Time start = DateManager.getHour(e.start);
		//				Time end = DateManager.getHour(e.end);
		//				
		//				Timestamp startTs 	= new Timestamp(year, e.month - 1, e.day, start.getHours(), start.getMinutes(), 0, 0);
		//				Timestamp endTs 	= new Timestamp(year, e.month - 1, e.day, end.getHours(), end.getMinutes(), 0, 0);;
		//				
		//				ps.setTimestamp(1, startTs);
		//				ps.setTimestamp(2, endTs);
		//				ps.setString(3, e.name);
		//				if(!e.name.toLowerCase().startsWith("ed") || !e.name.startsWith("Tut'Rentrée:")) {
		//					ps.setString(4, DateManager.getLocation(e.month, e.day));
		//				}
		//				ps.execute();
		//			}
		//			
		//		} catch (SQLException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}

	}

	public static void checkConnection() {

		//System.out.println(Thread.currentThread());
		try {
			if(!getConnection().isValid(300));
				con = src.getConnection();
			
			getConnection().createStatement().executeQuery("SELECT 1 FROM day");
			
		} catch (SQLException e) {
			System.out.println("Exception thrown when validating connection !");
			Sentry.capture(e);
			e.printStackTrace();
			try {
				con = src.getConnection();
			} catch (SQLException e1) {
				System.out.println("Exception thrown when oppening a new connection !");
				e1.printStackTrace();
				Sentry.capture(e);
			}
		}

	}
	
	private static void keepAlive() {
		try {
			getConnection().createStatement().executeQuery("SELECT 1 FROM day");
		} catch (SQLException e) {
			e.printStackTrace();
			Sentry.capture(e);
		}
	}
	 

}
