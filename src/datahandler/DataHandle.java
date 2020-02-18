package datahandler;

import java.util.ArrayList;

import element.Element;
import main.GateIO;
import main.Node;

public class DataHandle {
	
	public static DataHandle data;
	
	private ArrayList<Element> element;
	private ArrayList<GateIO> ios;
	private ArrayList<Node> node;
	
	private DataHandle() {
		element = new ArrayList<Element>();
		ios = new ArrayList<GateIO>();
		node = new ArrayList<Node>();
	}
	
	public static DataHandle createInstance() {
		if(data == null) data = new DataHandle();
		return data;
	}
	
//////////////////////////////////////////////////////////
	
	public Element getElement(int i) {
		return element.get(i);
	}
	public GateIO getIos(int i) {
		return ios.get(i);
	}
	public Node getNode(int i) {
		return node.get(i);
	}
	
//////////////////////////////////////////////////////////
	
	public ArrayList<Element> getElement(){
		return element;
	}
	
	public ArrayList<GateIO> getIos() {
		return ios;
	}
	
	public ArrayList<Node> getNode() {
		return node;
	}
	
//////////////////////////////////////////////////////////
	
	public void setElement(ArrayList<Element> e) {
		element = e;
	}
	
	public void setIos(ArrayList<GateIO> gio) {
		ios = gio;
	}
	
	public void setNode(ArrayList<Node> n) {
		node = n;
	}
	
//////////////////////////////////////////////////////////
	
	public int elementSize() {
		return element.size();
	}
	
	public int iosSize() {
		return ios.size();
	}
	
	public int nodeSize() {
		return node.size();
	}

//////////////////////////////////////////////////////////

	public void addElement(Element e) {
		element.add(e);
	}
	
	public void addGateIO(GateIO gio) {
		ios.add(gio);
	}
	
	public void addNode(Node n) {
		node.add(n);
	}
	
///////////////////////////////////////////////////////////
	
	public void removeElement(Element e) {
		element.remove(e);
	}
	public void removeGateIO(GateIO gio) {
		ios.remove(gio);
	}
	public void removeNode(Node nodeTemp) {
		node.remove(nodeTemp);
	}
}
