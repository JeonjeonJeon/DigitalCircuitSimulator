package datahandler;

import java.awt.Window;
import java.util.ArrayList;

import element.Element;
import element.SubCircuit;
import main.GateIO;
import main.Node;

public class DataHandle {
	
	public static DataHandle data;
	
	public ArrayList<Element> element;
	public ArrayList<GateIO> ios;
	public ArrayList<Node> node;
	
	private DataHandle() {
		element = new ArrayList<Element>();
		ios = new ArrayList<GateIO>();
		node = new ArrayList<Node>();
	}
	
	public static DataHandle createInstance() {
		if(data == null) data = new DataHandle();
		return data;
	}

	public Element getElement(int i) {
		return element.get(i);
	}
	public GateIO getIos(int i) {
		return ios.get(i);
	}
	public Node getNode(int i) {
		return node.get(i);
	}
	
	public int elementSize() {
		return element.size();
	}
	
	public int iosSize() {
		return ios.size();
	}
	
	public int nodeSize() {
		return node.size();
	}

	public void addElement(SubCircuit sc) {
		element.add(sc);
	}
}
