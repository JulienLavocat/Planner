package xyz.thebad.planner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Agenda {

	public static void start() throws IOException {


	}

	public static void updateAgenda() throws IOException {
		ArrayList<Event> events = new ArrayList<Event>();

		int count = 0;
		Iterable<CSVRecord> records = CSVParser.parse(Paths.get("EDT-Corrected.csv"), Charset.forName("UTF-8"), CSVFormat.EXCEL);
		for (CSVRecord record : records) {
			count++;
			if(count == 1)
				continue;

			HashMap<Integer, String> entries = new HashMap<Integer, String>();
			entries.put(7, record.get(2));
			entries.put(8, record.get(3));
			entries.put(9, record.get(4));
			entries.put(10, record.get(5));
			for(int i = 7; i < 11; i++) {
				entries.remove(i, "");
			}
//			//System.out.println(entries);
//			for(Entry<Integer, String> entry : entries.entrySet()) {
//				Event e = new Event();
//				e.day = Integer.parseInt(record.get(0).trim());
//				e.month = 9;
//				e.start = (entry.getKey() >= 9) ? entry.getKey()+1 : entry.getKey();
//				e.end = (entries.get(entry.getKey() + 1) == null) ? e.start + 2 : e.start + 1;
//				e.name = entry.getValue();
//
//				System.out.println(e);
//				System.out.println("---------------------------------");
//				events.add(e);
//			}

		}

		DB.insertEvents(events);
	}

}
