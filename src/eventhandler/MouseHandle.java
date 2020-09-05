package eventhandler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import datahandler.DataHandle;
import datahandler.Simulation;
import element.And;
import element.Buffer;
import element.Element;
import element.Ground;
import element.Monitor;
import element.Nand;
import element.Nor;
import element.NotGate;
import element.Or;
import element.SevenSegment;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import element.Switch;
import element.TextBlock;
import element.TristateBufferGate;
import element.VPulse;
import element.Vdc;
import element.Xor;
import framework.WorkSpace;
import graphicComponent.Calc;
import graphicComponent.Rectangle;
import main.Property;
import wires.GateIO;
import wires.Node;

// mouse event handler

public class MouseHandle implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	DataHandle data;
	WorkSpace ws;
	Simulation sim;
	
	public double mPositionX = 0, mPositionY = 0; // coordinate in window
	public static double mx = 0, my = 0; // mouse position
	public double dx = 0, dy = 0;
	
	private String message;
	
	public MouseHandle(WorkSpace workspace) {
		data = DataHandle.getInstance();
		ws = workspace;
		sim = Simulation.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent e) {
		dx = mx;
		dy = my;
		if(message == "OR") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Or(mx, my));
		}
		else if(message == "AND") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new And(mx, my));
		}
		else if(message == "VPULSE") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new VPulse(mx, my));
		}
		else if(message == "GND") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Ground(mx, my));
		}
		else if(message == "SWITCH") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Switch(mx, my));
		}
		else if(message == "VDC") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Vdc(mx, my));
		}
		else if(message == "MONITOR") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Monitor(mx, my));
		}
		else if(message == "NOT") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new NotGate(mx, my));
		}
		else if(message == "INPUT") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new SubCircuitInput(mx, my));
		}
		else if(message == "OUTPUT") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new SubCircuitOutput(mx, my));
		}
		else if(message == "NAND") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Nand(mx, my));
		}
		else if(message == "NOR") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Nor(mx, my));
		}
		else if(message == "BUFFER") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Buffer(mx, my));
		}
		else if(message == "XOR") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new Xor(mx, my));
		}
		else if(message == "TRISTATE") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new TristateBufferGate(mx, my));
		}
		else if(message == "TEXT") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new TextBlock(mx, my));
		}
		else if(message == "SEVEN") {
			//if(ws.isSim == true) ws.terminateSim();
			data.addElement(new SevenSegment(mx, my));
		}
		else if(message != null && message.endsWith("NULL")) {
			
		}
		else {
			try {
				ws.getSCFile(message, mx, my);
			}
			catch(Exception exc) {
			}
			
		}
		
		
		message = ws.nb.mouseClicked(e.getX(), e.getY());
		
		
		if(message == "SIM_START") {
			sim.startSim();
		}
		
		else if(message == "SIM_STEP_INTO") {
			sim.sim();
		}
		else if(message == "SIM_10_STEP") {
			for(int i = 0; i < 10; i++) {
				sim.sim();
			}
			
		}
		else if(message == "SIM_CONTINUOUS") {
			sim.simContinuous();
		}
		else if(message == "SIM_END"){
			sim.terminateSim();
		}
		else if(message == "FILE_SAVE") {
			//ws.terminateSim();
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
			jfc.setFileFilter(filter);
			jfc.addChoosableFileFilter(filter2);
			jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			int val = jfc.showSaveDialog(null);
			if(val == JFileChooser.APPROVE_OPTION) {
				try {
					File f = jfc.getSelectedFile();
					FileOutputStream filestream = new FileOutputStream(f);
					ObjectOutputStream os = new ObjectOutputStream(filestream);
					os.writeObject(new Point2D.Double(ws.offsetX, ws.offsetY));
					os.writeObject(data.getIos());
					os.writeObject(data.getElement());
					os.writeObject(data.getNode());
					os.close();
					filestream.close();
					
					if(jfc.getFileFilter().getDescription().startsWith("Digital" )&& f.getName().endsWith(".dcs") == false) {
						f.renameTo(new File(f.getAbsolutePath() + ".dcs"));
					}
					else if(jfc.getFileFilter().getDescription().startsWith("Sub") && f.getName().endsWith(".sc") == false){
						f.renameTo(new File(f.getAbsolutePath() + ".sc"));
					}
				}catch(Exception eee) {
					eee.printStackTrace();
				}
			}
		}
		else if(message == "TXT_FILE_SAVE") {
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
			FileNameExtensionFilter filter3 = new FileNameExtensionFilter("Txt Sub Circuit", "txt");
			jfc.setFileFilter(filter);
			jfc.addChoosableFileFilter(filter3);
			jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			int val = jfc.showSaveDialog(null);
			if(val == JFileChooser.APPROVE_OPTION) {
				try {
					File f = jfc.getSelectedFile();
					PrintWriter printer = new PrintWriter(f);
					
					printer.println("* Build Date : " + Property.currentDate);
					printer.println("* Author : " + System.getProperty("user.name") + "\n");
					for(Element element : data.getElement()) {
						if(element instanceof And) {
							printer.println(element.toNetlist());
						}
						else if(element instanceof Nand) {
							printer.println(element.toNetlist());
						}
						else if(element instanceof Nor) {
							printer.println(element.toNetlist());
						}
						else if(element instanceof Or) {
							printer.println(element.toNetlist());
						}
						else if(element instanceof Xor) {
							printer.println(element.toNetlist());
						}
						printer.println("");
					}
					
					printer.println("");
					
					for(Node node : data.getNode()) {
						printer.println(node.toSaveFile());
						printer.println("");
					}
					
					printer.close();
					
					
					if(jfc.getFileFilter().getDescription().startsWith("Digital" )&& f.getName().endsWith(".dcs") == false) {
						f.renameTo(new File(f.getAbsolutePath() + ".dcs"));
					}
					else if(jfc.getFileFilter().getDescription().startsWith("Sub") && f.getName().endsWith(".sc") == false){
						f.renameTo(new File(f.getAbsolutePath() + ".sc"));
					}
					else if(jfc.getFileFilter().getDescription().startsWith("Txt") && f.getName().endsWith(".txt") == false) {
						f.renameTo(new File(f.getAbsolutePath() + ".txt"));
					}
				}catch(Exception eee) {
					eee.printStackTrace();
				}
			}
		}
		else if(message == "FILE_OPEN") {
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
			jfc.setFileFilter(filter);
			jfc.addChoosableFileFilter(filter2);
			jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			int val = jfc.showOpenDialog(null);
			if(val == JFileChooser.APPROVE_OPTION) {

			try {
				File f = jfc.getSelectedFile();
				FileInputStream in = new FileInputStream(f);
				ObjectInputStream oin = new ObjectInputStream(in);
				
				if(f.getAbsolutePath().endsWith(".dcs")) {
					Point2D.Double t = (Point2D.Double)(oin.readObject());
					WorkSpace.offsetX = t.x;
					WorkSpace.offsetY = t.y;
					data.setIos((ArrayList<GateIO>)(oin.readObject()));
					data.setElement((ArrayList<Element>)(oin.readObject()));
					data.setNode((ArrayList<Node>)(oin.readObject()));
					int inputnum = 1;
					int outputnum = 1;
					for(int i = 0; i < data.elementSize(); i++) {
						Element el = data.getElement(i);
						if(el instanceof SubCircuitInput) {
							inputnum++;
						}
						else if(el instanceof SubCircuitOutput) {
							outputnum++;
						}
					}
					SubCircuitInput.num = inputnum;
					SubCircuitOutput.num = outputnum;
				}
				else if(f.getAbsolutePath().endsWith(".sc")) {
					message = f.getAbsolutePath();
				}
				oin.close();
				}catch(Exception eeee) {
					eeee.printStackTrace();
				}
			}
		}
		else if(message == "TXT_FILE_OPEN") {
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
			FileNameExtensionFilter filter3 = new FileNameExtensionFilter("Txt Sub Circuit", "txt");

			jfc.setFileFilter(filter);
			jfc.addChoosableFileFilter(filter3);
			jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			int val = jfc.showOpenDialog(null);
			if(val == JFileChooser.APPROVE_OPTION) {

			try {
				File f = jfc.getSelectedFile();
				FileReader filereader = new FileReader(f);
				BufferedReader reader = new BufferedReader(filereader);
				
				FileInputStream in = new FileInputStream(f);
				ObjectInputStream oin = new ObjectInputStream(in);


				if(f.getAbsolutePath().endsWith(".txt")) {
					String line = "";
					
					ArrayList<GateIO> gateios = new ArrayList<GateIO>();
					ArrayList<Node> nodes = new ArrayList<Node>();
					ArrayList<Element> elements = new ArrayList<Element>();
					
					Node nodeTemp;
					Element elementTemp;

					int whatsReading = 0; // 0: nothing, 1: element, 2: node
					while((line = reader.readLine()) != null) {
						if(line.startsWith("*")) continue;
						else if(line.startsWith(".NODE")) {
							whatsReading = 2;
							nodeTemp = new Node();
							nodeTemp.name = line.split(" ")[1];
						}
						else if(whatsReading == 2) {
							if(line == "") {
								whatsReading = 0;
							}
							else {
								
								
							}
						}
						
					}
					
					reader.close();
					
					
					
					
					Point2D.Double t = (Point2D.Double)(oin.readObject());
					WorkSpace.offsetX = t.x;
					WorkSpace.offsetY = t.y;
					data.setIos((ArrayList<GateIO>)(oin.readObject()));
					data.setElement((ArrayList<Element>)(oin.readObject()));
					data.setNode((ArrayList<Node>)(oin.readObject()));
					int inputnum = 1;
					int outputnum = 1;
					for(int i = 0; i < data.elementSize(); i++) {
						Element el = data.getElement(i);
						if(el instanceof SubCircuitInput) {
							inputnum++;
						}
						else if(el instanceof SubCircuitOutput) {
							outputnum++;
						}
					}
					SubCircuitInput.num = inputnum;
					SubCircuitOutput.num = outputnum;
				}
				else {
					System.out.println("unable to open txt file");

				}
				oin.close();
				}catch(Exception eeee) {
					eeee.printStackTrace();
				}
			}
		}
		ws.dragBox = new Rectangle(ws.getX(mx), ws.getY(my), ws.getX(mx)-ws.getX(dx), ws.getY(my)-ws.getY(dy));
		for(int i = 0; i < data.elementSize(); i++) {
			Element el = data.getElement(i);
			if(el.contains(mx, my)) {
				ws.movingOne = true;
			}
			else if(el.isSelected()) {
				
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int n = e.getWheelRotation();
		if(n < 0) {
			ws.ratio *= 1.1;
			ws.ratio = Calc.min(ws.ratio, 2);
		}
		else {
			ws.ratio *= 0.9;
			ws.ratio = Calc.max(ws.ratio, 0.1);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		ws.movingOne = false;
		if(ws.dragBox != null) {
			if(ws.dragBox.isEmpty()) {
				ws.dragBox = null;
				return;
			}
			for(int i = 0; i < data.elementSize(); i++) {
				Element el = data.getElement(i);
				el.select(ws.dragBox);//select elements which intersects with dragbox
			}
			for(int i = 0; i < data.nodeSize(); i++) {
				Node n = data.getNode(i);
				n.select(ws.dragBox);
			}
			ws.dragBox = null;//erase dragbox
		}
		
		mx = e.getX();
		my = e.getY();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("(" + e.getX() + ", " + e.getY() + ")");
		//bug: call node and merge itself - ConcurrentModificationException
		//bug: double clicking one gate io - NegativeArraySizeException
		ws.nodeExtension();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mPositionX = e.getX();
		mPositionY = e.getY();
		mx = ws.getX(mPositionX);
		my = ws.getY(mPositionY);
		
		if(ws.nodeMaking == false) {
			for(int i = 0; i < data.elementSize(); i++) {
				Element el = data.getElement(i);
				if(el.contains(mx, my) == true) break;
			}
			for(int i = 0; i < data.iosSize(); i++) {
				GateIO gio = data.getIos(i);
				gio.contains(mx, my);
			}
			for(int i = 0; i < data.nodeSize(); i++) {
				Node n = data.getNode(i);
				n.contains(mx, my);
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) { // little bit awkward when control key is down
		double diffx = mPositionX - e.getX();
		double diffy = mPositionY - e.getY();
		mPositionX = e.getX();
		mPositionY = e.getY();
		dx = ws.getX(mPositionX);
		dy = ws.getY(mPositionY);
		
		if(e.isControlDown()) {
			ws.offsetX += diffx;
			ws.offsetY += diffy;
		}
		else if(ws.movingOne) {
			for(int i = 0; i < data.elementSize(); i++) {
				Element el = data.getElement(i);
				if(el.containing) {
					el.move(dx, dy);
					break;
				}
			}
		}
		else {
			if(ws.dragBox != null) {
				ws.dragBox.setRect(Calc.min(mx, dx), 
						Calc.min(my, dy), 
						Calc.abs(mx - dx), 
						Calc.abs(my - dy));
			}
		}
	}
	
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	public static double getMouseX() {
		return mx;
	}
	public static double getMouseY() {
		return my;
	}

}
