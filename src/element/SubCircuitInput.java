package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import window.WorkSpace;

public class SubCircuitInput extends Element {	
	private static final long serialVersionUID = 5748706230043633658L;
	
	public static int num = 1;
	
	private int individualNum;
	private String id = " ";

	GateIO output;
	
	public SubCircuitInput(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 5, coordy + 1, 3);
		WorkSpace.ios.add(output);
		individualNum = num;
		num++;
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		g.setColor(Color.BLACK);
		
		drawLine(g, 0.2, 0.5, 3, 0.5);
		drawLine(g, 0.2, 1.5, 3, 1.5);
		drawLine(g, 3, 0.5, 4, 1);
		drawLine(g, 3, 1.5, 4, 1);
		drawLine(g, 0.2, 0.5, 0.2, 1.5);
		drawLine(g, 4, 1, 5, 1);
		if(id.equals(" ")) drawString(g, 0.3, 1.2, "input: " + individualNum);
		else drawString(g, 0.3, 1.2, id);
		
		
		bound.setRect(coordx, coordy, 4, 2);
		output.paint(g); 
	}

	@Override
	public void removeData() {
		WorkSpace.ios.remove(output);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx, coordy, 4, 2);
		output.setPos(coordx + 5, coordy + 1);
		
		//do something with node
	}
	
	public GateIO getIO() {
		return output;
	}
	
	public int getNum() {
		return individualNum;
	}
	
	
	public void sim1() {
		
	}
	
	public void sim2() {
		
	}
	
	public String getID() {
		return id;
	}
	public void setID(String s) {
		id = s;
	}
	
	public Element copy() {
		SubCircuitInput a = new SubCircuitInput(coordx, coordy);
		return a;
	}
}
