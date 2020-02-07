package element;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.GateIO;
import main.Node;
import main.Voltage;
import util.Calc;
import window.WorkSpace;

public class SubCircuit extends Element {
	private static final long serialVersionUID = 4038549874770807873L;
	
	public String circuitName;
	public int inputNum;
	public int outputNum;
	
	GateIO[] input;
	GateIO[] output;
	GateIO[] inputInner;
	GateIO[] outputInner;
	int[] inputBuffer;
	int[] outputBuffer;
	String[] inputID;
	String[] outputID;
	
	public ArrayList<Element> element = new ArrayList<Element>();
	public ArrayList<GateIO> ios = new ArrayList<GateIO>();
	public ArrayList<Node> node = new ArrayList<Node>();
	
	public SubCircuit(double coordx, double coordy) { // get input number as coordinate
		super(coordx, coordy);
	}
	
	
	public void init(int in, int out, ArrayList<Element> el, ArrayList<GateIO> gio, ArrayList<Node> no, String name) {
		inputNum = in;
		outputNum = out;
		
		element = el;
		ios = gio;
		node = no;
		circuitName = name;
		
		input = new GateIO[in];
		output = new GateIO[out];
		inputInner = new GateIO[in];
		outputInner = new GateIO[out];
		inputBuffer = new int[in];
		outputBuffer = new int[out];
		inputID = new String[in];
		outputID = new String[out];
		
		for(int i = 0 ; i < in ; i++) {
			input[i] = new GateIO(coordx, Math.round(coordy + 2 + 2*i), 0);
			inputInner[i] = new GateIO(coordx, Math.round(coordy + 2 + 2*i), 0);
			WorkSpace.ios.add(input[i]);
		}
		for(int i = 0 ; i < out ; i++) {
			output[i] = new GateIO(coordx + 6, Math.round(coordy + 2 + 2*i), 0);
			outputInner[i] = new GateIO(coordx + 6, Math.round(coordy + 2 + 2*i), 0);
			WorkSpace.ios.add(output[i]);
		}
		
		
		
		for(int i = 0 ; i < el.size(); i++) {
			if(el.get(i) instanceof SubCircuitInput) {
				SubCircuitInput sci = (SubCircuitInput) el.get(i);
				inputInner[sci.getNum() - 1].setNode(sci.getIO().getNode());
				inputID[sci.getNum() - 1] = sci.getID();
				el.remove(i);
				i--;
			}
			else if(el.get(i) instanceof SubCircuitOutput) {
				SubCircuitOutput sco = (SubCircuitOutput) el.get(i);
				outputInner[sco.getNum() - 1].setNode(sco.getIO().getNode());
				outputID[sco.getNum() - 1] = sco.getID();
				el.remove(i);
				i--;
			}
		}
		
		
		//WorkSpace.ios.addAll(ios);
		//WorkSpace.node.addAll(node);
	}
	
	
	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw */
		try {
			g.setColor(Color.BLACK);
			drawString(g, 1.1, 0.6, circuitName);
			drawLine(g, 1, 0, 1, 2*Calc.max(inputNum, outputNum) + 1); // left |
			drawLine(g, 5, 0, 5, 2*Calc.max(inputNum, outputNum) + 1); // right |
			drawLine(g, 1, 0, 5, 0); // upper -
			drawLine(g, 1, 2*Calc.max(inputNum, outputNum) + 1, 5, 2*Calc.max(inputNum, outputNum) + 1); // lower -
			
			bound.setRect(coordx + 1, coordy, 3, 1 + 2*Calc.max(inputNum, outputNum));
			
			for(int i = 0 ; i < inputNum ; i++) {
				input[i].paint(g);
				g.setColor(Color.BLACK);
				drawLine(g, 0, 2 + 2*i, 1, 2 + 2*i);
				drawString(g, 1, 2 + 2*i, inputID[i]);
			}
			for(int i = 0 ; i < outputNum ; i++) {
				output[i].paint(g);
				g.setColor(Color.BLACK);
				drawLine(g, 5, 2 + 2*i, 6, 2 + 2*i);
				drawString(g, 4.5-outputID[i].length()/3, 2 + 2*i, outputID[i]);
			}
		}catch(NullPointerException ee) {
			System.out.println("sub circiut not initialized!");
		}
		
		
	}

	@Override
	public void removeData() {
		
		WorkSpace.ios.removeAll(ios);
		for(int i = 0 ; i < inputNum ; i++) {
			WorkSpace.ios.remove(input[i]);
		}
		for(int i = 0 ; i < outputNum ; i++) {
			WorkSpace.ios.remove(output[i]);
		}
		for(int i = 0; i < WorkSpace.node.size(); i++) {
			for(Node n : node) {
				if(WorkSpace.node.get(i).equals(n)) {
					WorkSpace.node.remove(i);
					i--;
				}
			}
		}
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 3, 1 + 2*Calc.max(inputNum, outputNum));
		
		for(int i = 0 ; i < inputNum ; i++) {
			input[i].setPos(coordx, coordy + 2 + 2*i);
		}
		for(int i = 0 ; i < outputNum ; i++) {
			output[i].setPos(coordx + 6, coordy + 2 + 2*i);
		}
	}

	
	@Override
	public void sim1() {
		for(Element ee : element) {
			ee.sim1();
		}
	}

	@Override
	public void sim2() {
		for(Element ee : element) {
			ee.sim2();
		}
		for(int i = 0 ; i < inputNum ; i++) {
			//inputBuffer[i] inputInner[i].getState(), input[i].getState()
			if(inputInner[i].getState() != input[i].getState()) {
				if(inputBuffer[i] == inputInner[i].getState()) {
					inputBuffer[i] = input[i].getState();
					inputInner[i].setState(input[i].getState());
				}
				else if(inputBuffer[i] == input[i].getState()) {
					inputBuffer[i] = inputInner[i].getState();
					input[i].setState(inputInner[i].getState());
				}
			}
		}
		for(int i = 0 ; i < outputNum ; i++) {
			if(outputInner[i].getState() != output[i].getState()) {
				if(outputBuffer[i] == outputInner[i].getState()) {
					outputBuffer[i] = output[i].getState();
					outputInner[i].setState(output[i].getState());
				}
				else if(outputBuffer[i] == output[i].getState()) {
					outputBuffer[i] = outputInner[i].getState();
					output[i].setState(outputInner[i].getState());
				}
			}
		}
	}
	
	public void simInit(int s) {
		for(Element ee : element) {
			if(ee instanceof SubCircuit) {
				SubCircuit sc = (SubCircuit) ee;
				sc.simInit(s);
			}
		}
		for(Node nn : node) {
			nn.setState(s);
		}
		for(int i = 0; i < inputNum; i++) {
			inputBuffer[i] = Voltage.LOW;
		}
		for(int i = 0; i < outputNum; i++) {
			outputBuffer[i] = Voltage.LOW;
		}
	}

	public String toString() {
		return "input: " + inputNum + " / output: " + outputNum + "\nelement: " + element.size();
	}
	
	public Element copy() {
		SubCircuit a = new SubCircuit(coordx, coordy);
		return a;
	}
}
