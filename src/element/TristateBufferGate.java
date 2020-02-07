package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Voltage;
import window.WorkSpace;

public class TristateBufferGate extends Element {
	private static final long serialVersionUID = -5290386491035121345L;
	
	GateIO output;
	GateIO input;
	GateIO enable;
	
	public TristateBufferGate(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 4, coordy + 2, 0);
		input = new GateIO(coordx, coordy+2, 0);
		enable = new GateIO(coordx+2, coordy, 0);
		WorkSpace.ios.add(output);
		WorkSpace.ios.add(input);
		WorkSpace.ios.add(enable);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw NOT gate*/
		g.setColor(Color.BLACK);
		drawLine(g, 0, 2, 0.748, 2);
		drawLine(g, 0.748, 0.7, 0.748, 3.3);
		drawLine(g, 0.748, 0.7, 3, 2);
		drawLine(g, 0.748, 3.3, 3, 2);
		drawLine(g, 2, 0, 2, 1.42);
		drawLine(g, 3, 2, 4, 2);
		
		bound.setRect(coordx + 1, coordy, 2, 4);
		
		output.paint(g); 
		input.paint(g); 
		enable.paint(g);
	}

	@Override
	public void removeData() {
		WorkSpace.ios.remove(output);
		WorkSpace.ios.remove(input);
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 2, 4);
		output.setPos(coordx + 4, coordy + 2);
		input.setPos(coordx, coordy + 2);
		enable.setPos(coordx+2, coordy);
		
		//do something with node
	}

	public void sim1() {
		if(enable.getState() == Voltage.HIGH) {
			if(input.getState() == Voltage.HIGH) {
				internalState = Voltage.HIGH;
			}
			else if(input.getState() == Voltage.LOW) {
				internalState = Voltage.LOW;
			}
		}
	}
	
	public void sim2(){
		if(enable.getState() == Voltage.HIGH) {
			output.setState(internalState);
		}
	}
	
	public Element copy() {
		TristateBufferGate a = new TristateBufferGate(coordx, coordy);
		return a;
	}
}