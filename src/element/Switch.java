package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;

public class Switch extends Element{
	private static final long serialVersionUID = 25425022349291189L;
	
	
	GateIO left;
	GateIO right;
	public boolean open = true;
	
	public Switch(double coorx, double coory) {
		super(coorx, coory);
		left = new GateIO(coordx, coordy + 1, 0);
		right = new GateIO(coordx + 3, coordy + 1, 0);
		data.addGateIO(left);
		data.addGateIO(right);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw AND gate*/
		g.setColor(Color.BLACK);
		
		drawLine(g, 0, 1, 1, 1);
		drawLine(g, 2, 1, 3, 1);
		
		drawArc(g, 0.9, 0.9, 0.2, 0.2, 0, 360);
		drawArc(g, 1.9, 0.9, 0.2, 0.2, 0, 360);
		
		if(open == true) drawLine(g, 2, 1, 1.1, 0.55);
		else if(open == false) drawLine(g, 1, 1, 2, 1);
		
		bound.setRect(coordx + 1, coordy, 1, 2);
		
		right.paint(g); 
		left.paint(g);
	}

	@Override
	public void removeData() {
		data.removeGateIO(right);
		data.removeGateIO(left);
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 1, 2);
		right.setPos(coordx, coordy + 1);
		left.setPos(coordx + 3, coordy + 1);
		
		//do something with node
	}
	public void changeStatus() {
		open = !open;
	}
	
	public void sim1() {
		
	}
	public void sim2() {
		
	}
	
	public Element copy() {
		Switch a = new Switch(coordx, coordy);
		return a;
	}
}
