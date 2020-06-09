package element;

import java.awt.Color;
import java.awt.Graphics2D;

import graphicComponent.Calc;
import main.GateIO;
import main.Voltage;

public class VPulse extends Element{
	private static final long serialVersionUID = -2265231577538159525L;
	
	
	GateIO output;

	private int halfOfPeriod = 10;
	private int pulseCount = 10;
	private boolean outputHigh = true;
	
	
	public VPulse(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 1, coordy, 2);
		data.addGateIO(output);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw AND gate*/
		g.setColor(Color.BLACK);
		drawArc(g, 0, 1, 2, 2, 0, 360);
		drawLine(g, 1, 0, 1, 1);
		drawLine(g, 1, 3, 1, 3.8);
		// + sign
		drawLine(g, 1, 1.25, 1, 1.55); // |
		drawLine(g, 0.85, 1.4, 1.15, 1.4); // -
		// - sign
		drawLine(g, 0.85, 2.6, 1.15, 2.6);
		// GND
		drawLine(g, 0.2, 3.8, 1.8, 3.8);
		drawLine(g, 0.2, 3.8, 1, 4.6);
		drawLine(g, 1.8, 3.8, 1, 4.6);
		// pulse sign
		drawLine(g, 0.7, 1.7, 1.3, 1.7);
		drawLine(g, 0.3, 2.3, 0.6, 2.3);
		drawLine(g, 1.4, 2.3, 1.7, 2.3);
		drawLine(g, 0.6, 2.3, 0.7, 1.7);
		drawLine(g, 1.3, 1.7, 1.4, 2.3);
		
		drawString(g, 2, 1, pulseCount+"/"+halfOfPeriod);
		
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
	
	public void sim1() {
		pulseCount--;
		if(pulseCount <= 0) {
			if(outputHigh == true) internalState = Voltage.LOW;
			else internalState = Voltage.HIGH;
		}
	}
	
	public void sim2() {
		output.setState(internalState);
		if(pulseCount <= 0) {
			pulseCount = halfOfPeriod;
			outputHigh = !outputHigh;
		}
	}
	
	public void setPulseWidth(int p) {
		halfOfPeriod = Calc.max(p, 0);
	}
	public int getPulseWidth() {
		return halfOfPeriod;
	}
	
	public Element copy() {
		VPulse a = new VPulse(coordx, coordy);
		return a;
	}
}
