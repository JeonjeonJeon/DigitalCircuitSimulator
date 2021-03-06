package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Property {
	private static Property p;

	private static String osName;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	
	public static String currentDate;

	private Property(){
		osName = System.getProperty("os.name");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();
		currentDate = dtf.format(now);
	}

	public static String getOsName(){
		if(p == null) p = new Property();
		return p.osName;
	}
	
	public static boolean isWindows() {
		String st = getOsName();
		System.out.println(st);
		if(st.startsWith("Win")) return true;
		else return false;
	}


}
