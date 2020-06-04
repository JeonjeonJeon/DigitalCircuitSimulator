package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Voltage;

public class NandGate extends Element {
	private static final long serialVersionUID = -7977837069261648184L;
	
	GateIO output;
	GateIO input1;
	GateIO input2;
	
	public NandGate(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 5, coordy + 2, 1);
		input1 = new GateIO(coordx, coordy+1, 0);
		input2 = new GateIO(coordx, coordy+3, 0);
		data.addGateIO(output);
		data.addGateIO(input1);
		data.addGateIO(input2);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw AND gate*/
		g.setColor(Color.BLACK);
		drawLine(g, 0, 1, 0.5, 1); 
		drawLine(g, 0, 3, .5, 3); 
		drawLine(g, 4.5, 2, 5, 2); 
		drawLine(g, 0.5, 0.5, 2.5, 0.5); 
		drawLine(g, 0.5, 3.5, 2.5, 3.5); 
		drawLine(g, 0.5, 0.5, 0.5, 3.5);
		drawArc(g, 1, 0.5, 3, 3, -90, 180);
		drawArc(g, 4, 1.75, 0.5, 0.5, 0, 360);
		
		bound.setRect(coordx + 1, coordy, 3, 4);
		
		output.paint(g); 
		input1.paint(g); 
		input2.paint(g); 
	}

	@Override
	public void removeData() {
		data.removeGateIO(output);
		data.removeGateIO(input1);
		data.removeGateIO(input2);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 3, 4);
		output.setPos(coordx + 5, coordy + 2);
		input1.setPos(coordx, coordy + 1);
		input2.setPos(coordx, coordy + 3);
		
		//do something with node
	}
	
	public void sim1() {
		if(input1.getState() == Voltage.HIGH && input2.getState() == Voltage.HIGH) {
			internalState = Voltage.LOW;
		}
		else if(input1.getState() == Voltage.HIZ || input2.getState() == Voltage.HIZ) {
			internalState = Voltage.HIZ;
		}
		else internalState = Voltage.HIGH;
	}
	
	public void sim2(){
		output.setState(internalState);
	}
	
	public Element copy() {
		NandGate a = new NandGate(coordx, coordy);
		return a;
	}
}
