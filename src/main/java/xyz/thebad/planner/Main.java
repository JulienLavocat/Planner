package xyz.thebad.planner;

import io.sentry.Sentry;
import xyz.thebad.planner.http.Router;

public class Main {
	
	public static void main(String[] args) throws Exception {
		try {
			
			Sentry.init("https://1af88e22973841f6b6064c3c1026feb0@sentry.io/1266987");
			DB.start();
			
			new Router();
		} catch(Exception e) {
			System.out.println(e);
			Sentry.capture(e);
			System.exit(-1);
		}
	}
	
}
