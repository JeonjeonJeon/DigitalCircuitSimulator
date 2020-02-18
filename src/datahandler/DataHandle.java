package datahandler;

import java.util.ArrayList;

import element.Element;
import main.GateIO;
import main.Node;

public class DataHandle {
	
	public static DataHandle dh;
	
	public static ArrayList<Element> element;
	public static ArrayList<GateIO> ios;
	public static ArrayList<Node> node;
	
	private DataHandle() {
		element = new ArrayList<Element>();
		ios = new ArrayList<GateIO>();
		node = new ArrayList<Node>();
	}
	
	public static DataHandle createInstance() {
		if(dh == null) dh = new DataHandle();
		return dh;
	}
}
