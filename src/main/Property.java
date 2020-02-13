
public class Property {
	private static Property p;

	private static String osName;
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;

	private Property(){
		osName = System.getProperty("os.name");
	}

	public static getOsName(){
		if(p == null) p = new Property();
		return p.osName();
	}


}
