package graphic;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

import main.Property;

public class WindowsFrame extends JFrame implements Frame {
	
	WorkSpace ws;
	
	public WindowsFrame(String name){
		super(name);
		ws = new WorkSpace();
		SpringLayout sl = new SpringLayout();

		setBounds(100, 100, Property.WIDTH, Property.HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(sl);
		sl.putConstraint(SpringLayout.NORTH, ws, 0, SpringLayout.NORTH, getContentPane());
		sl.putConstraint(SpringLayout.WEST, ws, 0, SpringLayout.WEST, getContentPane());
		sl.putConstraint(SpringLayout.SOUTH, ws, 0, SpringLayout.SOUTH, getContentPane());
		sl.putConstraint(SpringLayout.EAST, ws, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(ws);

		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void render(int fps){
		ws.render(fps);
	}
}
