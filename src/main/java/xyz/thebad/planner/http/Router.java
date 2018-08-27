package xyz.thebad.planner.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import io.javalin.Javalin;
import io.javalin.embeddedserver.Location;
import io.sentry.Sentry;
import xyz.thebad.planner.DB;
import xyz.thebad.planner.Event;

public class Router {

	public Router() throws IOException {
		
		Javalin app = Javalin.create()
				.enableStaticFiles("public", Location.EXTERNAL)
				.port(80)
				.start();
		
		app.get("/", ctx -> {
			String index = new String(Files.readAllBytes(Paths.get("html/index.html")));
			ctx.html(index);
		});
		
		app.get("/getEvents", ctx -> {
			try {
				
				if(ctx.anyQueryParamNull("mode", "date")) {
					ctx.json(toView(DB.getAllEvents()));
					return;
				}
				
				String date = ctx.queryParam("date");
				String mode = ctx.queryParam("mode");
				
				if(mode.equalsIgnoreCase("day"))
					ctx.json(toView(DB.getDayEvents(date)));
				if(mode.equalsIgnoreCase("week"))
					ctx.json(toView(DB.getWeekEvents(date)));
				if(mode.equalsIgnoreCase("month"))
					ctx.json(toView(DB.getMonthEvents(date)));
					
			} catch (Exception e) {
				Sentry.capture(e);
				e.printStackTrace();
				
			}
			
		});
		
	}

	private ArrayList<EventView> toView(ArrayList<Event> events) {
		ArrayList<EventView> views = new ArrayList<EventView>();
		for(int i = 0; i < events.size(); i++) {
			views.add(new EventView(i+1, events.get(i)));
		}
		return views;
	}
	
}

