package datahandler;

import java.util.ArrayList;

import element.Element;
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
}
