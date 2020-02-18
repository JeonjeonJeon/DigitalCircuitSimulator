package graphic;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

import main.Property;

public class WindowFrame extends JFrame implements Frame {
	public WindowFrame(String name){
		super(name);
		SpringLayout sl = new SpringLayout();

		jf.setBounds(100, 100, Property.WIDTH, Property.HEIGHT);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(sl);
		sl.putConstraint(SpringLayout.NORTH, ws, 0, SpringLayout.NORTH, getContentPane());
		sl.putConstraint(SpringLayout.WEST, ws, 0, SpringLayout.WEST, getContentPane());
		sl.putConstraint(SpringLayout.SOUTH, ws, 0, SpringLayout.SOUTH, getContentPane());
		sl.putConstraint(SpringLayout.EAST, ws, 0, SpringLayout.EAST, getContentPane());
		jf.getContentPane().add(ws);

		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}

	public void render(int fps){
		ws.render(fps);
	}

}
