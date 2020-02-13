
public class Property {

	private static Property p;
	private static String osName;

	private Property(){
		osName = System.getProperty("os.name");
	}

	public static getOsName(){
		if(p == null) p = new Property();
		return p.osName();
	}


}
