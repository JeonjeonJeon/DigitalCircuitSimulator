package eventhandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import datahandler.DataHandler;
import element.Element;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import element.Switch;
import element.TextBlock;
import element.VPulse;
import element.Vdc;
import framework.WorkSpace;
import wires.GateIO;
import wires.Node;

// keyboard event handler

public class KeyboardHandle implements KeyListener {
	
	WorkSpace ws;
	DataHandler data = DataHandler.getInstance();
	
	double mx, my;
	
	public KeyboardHandle(WorkSpace workspace) {
		ws = workspace;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		mx = MouseHandle.getMouseX();
		my = MouseHandle.getMouseY();
		
		for(int i = 0; i < data.elementSize(); i++) { 
			Element ee = data.getElement(i); 
			if(ee.contains(mx, my)) {
				if(ee instanceof SubCircuitInput) {
					SubCircuitInput sci = (SubCircuitInput) ee;
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) sci.setID(sci.getID().substring(0, sci.getID().length()-1));
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) sci.setID(sci.getID() + " ");
					else sci.setID(sci.getID() + KeyEvent.getKeyText(e.getKeyCode()));
					return;
				}
				else if(ee instanceof SubCircuitOutput) {
					SubCircuitOutput sco = (SubCircuitOutput) ee;
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) sco.setID(sco.getID().substring(0, sco.getID().length()-1));
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) sco.setID(sco.getID() + " ");
					else sco.setID(sco.getID() + KeyEvent.getKeyText(e.getKeyCode()));
					return;
				}
				else if(ee instanceof TextBlock) {
					TextBlock tb = (TextBlock) ee;
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) tb.setText(tb.getText().substring(0, tb.getText().length()-1));
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) tb.setText(tb.getText() + " ");
					else tb.setText(tb.getText() + KeyEvent.getKeyText(e.getKeyCode()));
					return;
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			ws.ratio *= 1.1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			ws.ratio *= 0.9;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			for(int i = 0; i < data.elementSize(); i++) { Element ee = data.getElement(i); 
				if(ee.contains(mx, my)) {
					if(ee instanceof VPulse) {
						VPulse vp = (VPulse) ee;
						vp.setPulseWidth(vp.getPulseWidth() + 1);
					}
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			for(int i = 0; i < data.elementSize(); i++) { Element ee = data.getElement(i);
				if(ee.contains(mx, my)) {
					if(ee instanceof VPulse) {
						VPulse vp = (VPulse) ee;
						vp.setPulseWidth(vp.getPulseWidth() - 1);
					}
				}
			}
		}
		else if(e.isControlDown() == true) {
			if(e.getKeyCode() == KeyEvent.VK_C) {
				for(int i = 0; i < data.elementSize(); i++) { Element ee = data.getElement(i);
					if(ee.isSelected() == true) {
						ws.ctrlCV = ee;
						return;
					}
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_V) {
				if(ws.ctrlCV != null) {
					Element copied = ws.ctrlCV.copy();
					copied.move(ws.ctrlCV.getX() + 2, ws.ctrlCV.getY() + 4);
					ws.ctrlCV = copied;
					data.addElement(copied);
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_H) {
			ws.statusHide = !ws.statusHide;
		}
		else if(e.getKeyCode() == KeyEvent.VK_L) {
			//Main.FPS_LOCK = !Main.FPS_LOCK;
		}
		else if(e.getKeyCode() == KeyEvent.VK_W) {
			ws.nodeExtension();
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			//ws.sim();
		}
		else if(e.getKeyCode() == KeyEvent.VK_P) {
			for(int i = 0; i < data.elementSize(); i++) { Element ee = data.getElement(i);
				if(ee.contains(mx, my)) {
					System.out.println(ee.toString());
				}
			}
			for(int i = 0; i < data.nodeSize(); i++) {
			Node nn = data.getNode(i);
				if(nn.contains(mx, my)) {
					System.out.println(nn.toString());
				}
			}
			for(int i = 0; i < data.iosSize(); i++) { GateIO io = data.getIos(i); 
				if(io.contains(mx, my)) {
					System.out.println(io.toString());
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_C) {
			for(int i = 0; i < data.elementSize(); i++) { Element ee = data.getElement(i);
				if(ee instanceof Switch) {
					Switch ss = (Switch)ee;
					if(ss.contains(mx, my)) ss.changeStatus();
				}
				else if(ee instanceof Vdc) {
					Vdc vdc = (Vdc)ee;
					if(vdc.contains(mx, my)) vdc.changeStatus();
				}
//				else if(ee instanceof VPulse) {
//					VPulse vp = (VPulse) ee;
//					if(vp.contains(mx, my)) vp.setPulseWidth(1);
//				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			for(int i = 0 ; i < data.elementSize() ; i++) {
				if(data.getElement(i).isSelected()) {
					data.removeElement(i);
					i--;
				}
			}
			for(int i = 0 ; i < data.nodeSize(); i++) {
				if(data.getNode(i).isSelected()) {
					for(int j = 0; j < data.iosSize(); j++) { 
						GateIO io = data.getIos(j); 
						if(io.isLinked() && io.getNode().equals(data.getNode(i))) {
							io.setNode(null);
						}
					} 
					data.removeNode(i);
					i--;
				}
			}
		}
		
		ws.nb.keyPressed(e.getKeyCode());
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {	
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

}
