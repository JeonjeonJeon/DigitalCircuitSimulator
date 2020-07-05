package main;

public class Property {
	private static Property p;

	private static String osName;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;

	private Property(){
		osName = System.getProperty("os.name");
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
