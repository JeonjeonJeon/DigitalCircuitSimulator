package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Voltage;
import wires.GateIO;

public class XorGate extends Element {
	private static final long serialVersionUID = -5962861935648209191L;
	
	GateIO output;
	GateIO input1;
	GateIO input2;
	
	public XorGate(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 5, coordy + 2, 1);
		input1 = new GateIO(coordx, coordy+1, 0);
		input2 = new GateIO(coordx, coordy+3, 0);
		
		data.addGateIO(output);
		data.addGateIO(input1);
		data.addGateIO(input2);
	}

	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw or gate--*/
		g.setColor(Color.BLACK);
		drawLine(g, 0, 1, 0.78, 1); 
		drawLine(g, 0, 3, 0.78, 3); 
		drawLine(g, 4, 2, 5, 2); 
		drawLine(g, 0.5, 0.5, 1.5, 0.5); 
		drawLine(g, 0.5, 3.5, 1.5, 3.5); 
		drawArc(g, -4, -0.5, 5, 5, -36.86989765, 73.73979529); 
		drawArc(g, -4.3, -0.5, 5, 5, -36.86989765, 73.73979529); 
		drawArc(g, -2.009601203, .490398797, 6.519202406, 6.519202406, 32.46673212, 53.04436811); 
		drawArc(g, -2.009601203, -3.009601203, 6.519202406, 6.519202406, -85.51110023, 53.04436811); 
		
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
	}
	
	public void sim1() {
		if(input1.getState() == Voltage.HIZ || input2.getState() == Voltage.HIZ) {
			internalState = Voltage.HIZ;
			return;
		}
		else if(input1.getState() == input2.getState()) {
			internalState = Voltage.LOW;
		}
		else internalState = Voltage.HIGH;
	}
	
	public void sim2(){
		output.setState(internalState);
	}
	
	public Element copy() {
		XorGate a = new XorGate(coordx, coordy);
		return a;
	}
}
