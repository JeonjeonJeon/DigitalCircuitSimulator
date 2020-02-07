package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Voltage;
import window.WorkSpace;

public class OrGate extends Element {
	private static final long serialVersionUID = -4664030039931093477L;
	
	
	GateIO output;
	GateIO input1;
	GateIO input2;
	
	public OrGate(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 5, coordy + 2, 1);
		input1 = new GateIO(coordx, coordy+1, 0);
		input2 = new GateIO(coordx, coordy+3, 0);
		
		WorkSpace.ios.add(output);
		WorkSpace.ios.add(input1);
		WorkSpace.ios.add(input2);
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
		drawArc(g, -2.009601203, .490398797, 6.519202406, 6.519202406, 32.46673212, 53.04436811); 
		drawArc(g, -2.009601203, -3.009601203, 6.519202406, 6.519202406, -85.51110023, 53.04436811); 
		
		bound.setRect(coordx + 1, coordy, 3, 4);
		output.paint(g); 
		input1.paint(g); 
		input2.paint(g); 
	}

	@Override
	public void removeData() {
		WorkSpace.ios.remove(output);
		WorkSpace.ios.remove(input1);
		WorkSpace.ios.remove(input2);
		
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
		else if(input1.getState() == Voltage.HIGH || input2.getState() == Voltage.HIGH) {
			internalState = Voltage.HIGH;
		}
		else internalState = Voltage.LOW;
	}
	
	public void sim2(){
		output.setState(internalState);
	}
	
	public Element copy() {
		OrGate a = new OrGate(coordx, coordy);
		return a;
	}
}
