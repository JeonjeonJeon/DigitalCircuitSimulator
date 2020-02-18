package eventhandle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import element.AndGate;
import element.BufferGate;
import element.Element;
import element.Ground;
import element.Monitor;
import element.NandGate;
import element.NorGate;
import element.NotGate;
import element.OrGate;
import element.SevenSegment;
import element.SubCircuit;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import element.Switch;
import element.TextBlock;
import element.TristateBufferGate;
import element.VPulse;
import element.Vdc;
import element.XorGate;
import graphicComponent.Calc;
import graphicComponent.Rectangle;
import main.GateIO;
import main.Node;
import main.Voltage;

// mouse event handler

public class MouseHandle implements MouseListener, MouseMotioniListener, MouseWhellListener {
	@Override
	public void mousePressed(MouseEvent e) {
		dx = mx;
		dy = my;
		if(message == "OR") {
			if(isSim == true) terminateSim();
			element.add(new OrGate(mx, my));
		}
		else if(message == "AND") {
			if(isSim == true) terminateSim();
			element.add(new AndGate(mx, my));
		}
		else if(message == "VPULSE") {
			if(isSim == true) terminateSim();
			element.add(new VPulse(mx, my));
		}
		else if(message == "GND") {
			if(isSim == true) terminateSim();
			element.add(new Ground(mx, my));
		}
		else if(message == "SWITCH") {
			if(isSim == true) terminateSim();
			element.add(new Switch(mx, my));
		}
		else if(message == "VDC") {
			if(isSim == true) terminateSim();
			element.add(new Vdc(mx, my));
		}
		else if(message == "MONITOR") {
			if(isSim == true) terminateSim();
			element.add(new Monitor(mx, my));
		}
		else if(message == "NOT") {
			if(isSim == true) terminateSim();
			element.add(new NotGate(mx, my));
		}
		else if(message == "INPUT") {
			if(isSim == true) terminateSim();
			element.add(new SubCircuitInput(mx, my));
		}
		else if(message == "OUTPUT") {
			if(isSim == true) terminateSim();
			element.add(new SubCircuitOutput(mx, my));
		}
		else if(message == "NAND") {
			if(isSim == true) terminateSim();
			element.add(new NandGate(mx, my));
		}
		else if(message == "NOR") {
			if(isSim == true) terminateSim();
			element.add(new NorGate(mx, my));
		}
		else if(message == "BUFFER") {
			if(isSim == true) terminateSim();
			element.add(new BufferGate(mx, my));
		}
		else if(message == "XOR") {
			if(isSim == true) terminateSim();
			element.add(new XorGate(mx, my));
		}
		else if(message == "TRISTATE") {
			if(isSim == true) terminateSim();
			element.add(new TristateBufferGate(mx, my));
		}
		else if(message == "TEXT") {
			if(isSim == true) terminateSim();
			element.add(new TextBlock(mx, my));
		}
		else if(message == "SEVEN") {
			if(isSim == true) terminateSim();
			element.add(new SevenSegment(mx, my));
		}
		else if(message != null && message.endsWith("NULL")) {
			
		}
		else {
			try {
				getSCFile(message, mx, my);
			}
			catch(Exception exc) {
			}
			
		}
		
		
		message = nb.mouseClicked(e.getX(), e.getY());
		
		
		if(message == "SIM_START") {
			isSim = true;
			for(Node nn : node) {
				if(nn.startSim() == false) {
					isSim = false;
					System.out.println("Error found at some nodes. Simulation failed!!");
					return;
				}
			}
			for(Node nn : node) {
				nn.setState(Voltage.LOW);
			}
			for(Element ee : element) {
				if(ee instanceof Monitor) {
					Monitor m = (Monitor)ee;
					m.getLinkedNode().setState(Voltage.LOW);
				}
				else if(ee instanceof Vdc) {
					Vdc vdc = (Vdc)ee;
					vdc.sim1();
					vdc.sim2();
				}
				else if(ee instanceof SubCircuit) {
					SubCircuit sc = (SubCircuit) ee;
					sc.simInit(Voltage.LOW);
				}
			}
		}
		
		else if(message == "SIM_STEP_INTO") {
			sim();
		}
		else if(message == "SIM_10_STEP") {
			if(isSim == false) {
				System.out.println("press STARTSIM first");
				return;
			}
			for(int i = 0; i < 10; i++) {
				sim();
			}
			
		}
		else if(message == "SIM_CONTINUOUS") {
			if(isSim == false) {
				System.out.println("press SIM_START first");
				return;
			}
			simConti = true;
		}
		else if(message == "SIM_END"){
			terminateSim();
		}
		else if(message == "FILE_SAVE") {
			terminateSim();
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
					os.writeObject(new Point2D.Double(offsetX, offsetY));
					os.writeObject(ios);
					os.writeObject(element);
					os.writeObject(node);
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
					offsetX = t.x;
					offsetY = t.y;
					ios = (ArrayList<GateIO>)(oin.readObject());
					element = (ArrayList<Element>)(oin.readObject());
					node = (ArrayList<Node>)(oin.readObject());
					int inputnum = 1;
					int outputnum = 1;
					for(Element el : element) {
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
		dragBox = new Rectangle(getX(mx), getY(my), getX(mx)-getX(dx), getY(my)-getY(dy));
		for(Element el : element) {
			if(el.contains(mx, my)) {
				movingOne = true;
			}
			else if(el.isSelected()) {
				
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int n = e.getWheelRotation();
		if(n < 0) {
			ratio *= 1.1;
		}
		else {
			ratio *= 0.9;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		movingOne = false;
		if(dragBox != null) {
			if(dragBox.isEmpty()) {
				dragBox = null;
				return;
			}
			for(Element el : element) {
				el.select(dragBox);//select elements which intersects with dragbox
			}
			for(Node n : node) {
				n.select(dragBox);
			}
			dragBox = null;//erase dragbox
		}
		
		mx = e.getX();
		my = e.getY();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//bug: call node and merge itself - ConcurrentModificationException
		//bug: double clicking one gate io - NegativeArraySizeException
		nodeExtension();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mPositionX = e.getX();
		mPositionY = e.getY();
		mx = getX(mPositionX);
		my = getY(mPositionY);
		
		if(nodeMaking == false) {
			for(Element el : element) {
				if(el.contains(mx, my) == true) break;
			}
			for(GateIO gio : ios) {
				gio.contains(mx, my);
			}
			for(Node n : node) {
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
		dx = getX(mPositionX);
		dy = getY(mPositionY);
		
		if(e.isControlDown()) {
			offsetX += diffx;
			offsetY += diffy;
		}
		else if(movingOne) {
			for(Element el : element) {
				if(el.containing) {
					el.move(dx, dy);
					break;
				}
			}
		}
		else {
			if(dragBox != null) {
				dragBox.setRect(Calc.min(mx, dx), 
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

}
