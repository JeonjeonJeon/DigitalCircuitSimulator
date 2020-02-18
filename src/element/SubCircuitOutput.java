package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;

public class SubCircuitOutput extends Element {
	private static final long serialVersionUID = -307844545053060804L;

	GateIO input;
	
	public static int num = 1;
	private int individualNum;
	private String id = " ";
	
	public SubCircuitOutput(double coorx, double coory) {
		super(coorx, coory);
		input = new GateIO(coordx, coordy + 1, 3);
		data.addGateIO(input);
		individualNum = num;
		num++;
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		g.setColor(Color.BLACK);
		
		drawLine(g, 5-0.2, 0.5, 5-3, 0.5);
		drawLine(g, 5-0.2, 1.5, 5-3, 1.5);
		drawLine(g, 5-3, 0.5, 5-4, 1);
		drawLine(g, 5-3, 1.5, 5-4, 1);
		drawLine(g, 5-0.2, 0.5, 5-0.2, 1.5);
		drawLine(g, 5-4, 1, 5-5, 1);
		if(id.equals(" ")) drawString(g, 2.1, 1.2, "output: " + individualNum);
		else drawString(g, 2.1, 1.2, id);
		
		
		bound.setRect(coordx + 1, coordy, 4, 2);
		input.paint(g); 
	}

	@Override
	public void removeData() {
		data.removeGateIO(input);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 4, 2);
		input.setPos(coordx, coordy + 1);
		
		//do something with node
	}
	
	public GateIO getIO() {
		return input;
	}
	public int getNum() {
		return individualNum;
	}
	
	
	public void sim1() {
		
	}
	
	public void sim2() {
		
	}
	public void setID(String s) {
		id = s;
	}
	public String getID() {
		return id;
	}
	
	public Element copy() {
		SubCircuitOutput a = new SubCircuitOutput(coordx, coordy);
		return a;
	}
}
