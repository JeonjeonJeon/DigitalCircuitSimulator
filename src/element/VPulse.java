package element;

import java.awt.Color;
import java.awt.Graphics2D;

import graphicComponent.Calc;
import main.DigitalCircuitSimulator;
import main.GateIO;
import main.Voltage;
import window.WorkSpace;

public class VPulse extends Element{
	private static final long serialVersionUID = -2265231577538159525L;
	
	
	GateIO output;
	private double pulseWidth = 1;
	private double pulseCount = DigitalCircuitSimulator.FPS;
	private boolean outputHigh = true;
	
	
	public VPulse(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 1, coordy, 2);
		WorkSpace.ios.add(output);
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
		
		drawString(g, 2, 1, pulseWidth+"");
		
		bound.setRect(coordx, coordy + 1, 2, 4);
		
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
		
		bound.setRect(coordx, coordy + 1, 2, 4);
		output.setPos(coordx + 1, coordy);
		
		//do something with node
	}
	
	public void sim1() {
		if(outputHigh == true) internalState = Voltage.HIGH;
		else internalState = Voltage.LOW;
	}
	public void sim2() {
		output.setState(internalState);
		pulseCount--;
		if(pulseCount <= 0) {
			pulseCount = DigitalCircuitSimulator.FPS * pulseWidth;
			outputHigh = !outputHigh;
		}
	}
	
	public void setPulseWidth(double p) {
		pulseWidth = Calc.max(p, 0.05);
	}
	public double getPulseWidth() {
		return pulseWidth;
	}
	
	public Element copy() {
		VPulse a = new VPulse(coordx, coordy);
		return a;
	}
}
