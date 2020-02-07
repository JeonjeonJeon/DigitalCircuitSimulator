package window;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

public class Window extends JFrame{
	private static final long serialVersionUID = -2644283529852531924L;
	public static int WIDTH = 1000;
	public static int HEIGHT = 800;
	
	private WorkSpace ws = new WorkSpace();
	
	public Window() {
		super("asdfasdf");
		
		setBounds(100, 100, WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		springLayout.putConstraint(SpringLayout.NORTH, ws, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ws, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, ws, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, ws, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(ws);
		
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void render(int fps) {
		ws.render(fps);
	}
}
