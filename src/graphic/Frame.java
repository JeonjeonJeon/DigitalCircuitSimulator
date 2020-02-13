
// import javax.swing.JFrame;

public class Frame {
	
	private static String osName;
	public Frame(String name) {
		
		osName = System.getProperty("os.name");
		if(osName.matches("Linux")) {
		}

		else {
			javax.swing.JFrame frame = new javax.swing.JFrame(name);
			frame.setBounds(100, 100, WIDTH, HEIGHT);
			

		}
	}
}
