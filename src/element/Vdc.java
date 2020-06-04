package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Voltage;

public class Vdc extends Element {
	private static final long serialVersionUID = -7605075773535244103L;
	
	GateIO output;
	private boolean high = false;
	
	public Vdc(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 1, coordy, 2);
		data.addGateIO(output);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		g.setColor(Color.BLACK);
		drawArc(g, 0, 1, 2, 2, 0, 360);
		drawLine(g, 1, 0, 1, 1);
		drawLine(g, 1, 3, 1, 3.8);
		// GND
		drawLine(g, 0.2, 3.8, 1.8, 3.8);
		drawLine(g, 0.2, 3.8, 1, 4.6);
		drawLine(g, 1.8, 3.8, 1, 4.6);
		// High Low sign
		if(high == true) {
			drawLine(g, 0.7, 1.5, 0.7, 2.5);
			drawLine(g, 1.3, 1.5, 1.3, 2.5);
			drawLine(g, 0.7, 2, 1.3, 2);
		}
		else {
			drawLine(g, 0.7, 1.5, 0.7, 2.5);
			drawLine(g, 0.7, 2.5, 1.3, 2.5);
		}
		
		bound.setRect(coordx, coordy + 1, 2, 4);
		output.paint(g); 
	}

	@Override
	public void removeData() {
		data.removeGateIO(output);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx, coordy + 1, 2, 4);
		output.setPos(coordx + 1, coordy);
		
		//do something with node
	}
	
	public void changeStatus() {
		System.out.println("change status call");
		high = !high;
	}
	
	public void sim1() {
		if(high == true) internalState = Voltage.HIGH;
		else internalState = Voltage.LOW;
	}
	
	public void sim2() {
		output.setState(internalState);
	}

	public boolean update() {
		
		if(high == true) output.setState(Voltage.HIGH);
		else output.setState(Voltage.LOW);
		
		return true;
	}
	
	public Element copy() {
		Vdc a = new Vdc(coordx, coordy);
		return a;
	}
}
