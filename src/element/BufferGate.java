package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Voltage;

public class BufferGate extends Element {
	private static final long serialVersionUID = 3348466536787374449L;
	
	GateIO output;
	GateIO input;
	
	public BufferGate(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 4, coordy + 2, 1);
		input = new GateIO(coordx, coordy+2, 0);
		data.addGateIO(output);
		data.addGateIO(input);
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
		drawLine(g, 3, 2, 4, 2);
		
		bound.setRect(coordx + 1, coordy, 2, 4);
		
		output.paint(g); 
		input.paint(g); 
	}

	@Override
	public void removeData() {
		data.removeGateIO(output);
		data.removeGateIO(input);
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 2, 4);
		output.setPos(coordx + 4, coordy + 2);
		input.setPos(coordx, coordy + 2);
		
		//do something with node
	}

	public void sim1() {
		if(input.getState() == Voltage.HIZ) {
			//internalState = Voltage.HIZ;
		}
		else if(input.getState() == Voltage.HIGH) {
			internalState = Voltage.HIGH;
		}
		else if(input.getState() == Voltage.LOW) {
			internalState = Voltage.LOW;
		}	
	}
	
	public void sim2(){
		output.setState(internalState);
	}
	
	public Element copy() {
		BufferGate a = new BufferGate(coordx, coordy);
		return a;
	}
}
