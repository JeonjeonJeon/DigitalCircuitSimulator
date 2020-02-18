package eventhandler;

import java.awt.event.KeyEvent;

import element.Element;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import element.Switch;
import element.TextBlock;
import element.VPulse;
import element.Vdc;
import main.GateIO;
import main.Node;

// keyboard event handler

public class Keyboard implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		
		for(Element ee : element) {
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
			ratio *= 1.1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			ratio *= 0.9;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			for(Element ee : element) {
				if(ee.contains(mx, my)) {
					if(ee instanceof VPulse) {
						VPulse vp = (VPulse) ee;
						vp.setPulseWidth(vp.getPulseWidth() + 0.5);
					}
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			for(Element ee : element) {
				if(ee.contains(mx, my)) {
					if(ee instanceof VPulse) {
						VPulse vp = (VPulse) ee;
						vp.setPulseWidth(vp.getPulseWidth() - 0.5);
					}
				}
			}
		}
		else if(e.isControlDown() == true) {
			if(e.getKeyCode() == KeyEvent.VK_C) {
				for(Element ee : element) {
					if(ee.isSelected() == true) {
						ctrlCV = ee;
						return;
					}
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_V) {
				if(ctrlCV != null) {
					Element copied = ctrlCV.copy();
					copied.move(ctrlCV.getX() + 2, ctrlCV.getY() + 4);
					ctrlCV = copied;
					element.add(copied);
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_H) {
			statusHide = !statusHide;
		}
		else if(e.getKeyCode() == KeyEvent.VK_L) {
			DigitalCircuitSimulator.FPS_LOCK = !DigitalCircuitSimulator.FPS_LOCK;
		}
		else if(e.getKeyCode() == KeyEvent.VK_W) {
			nodeExtension();
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			sim();
		}
		else if(e.getKeyCode() == KeyEvent.VK_P) {
			for(Element ee : element) {
				if(ee.contains(mx, my)) {
					System.out.println(ee.toString());
				}
			}
			for(Node nn : node) {
				if(nn.contains(mx, my)) {
					System.out.println(nn.toString());
				}
			}
			for(GateIO io : ios) {
				if(io.contains(mx, my)) {
					System.out.println(io.toString());
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_C) {
			for(Element ee : element) {
				if(ee instanceof Switch) {
					Switch ss = (Switch)ee;
					if(ss.contains(mx, my)) ss.changeStatus();
				}
				else if(ee instanceof Vdc) {
					Vdc vdc = (Vdc)ee;
					if(vdc.contains(mx, my)) vdc.changeStatus();
				}
				else if(ee instanceof VPulse) {
					VPulse vp = (VPulse) ee;
					if(vp.contains(mx, my)) vp.setPulseWidth(1);
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			for(int i = 0 ; i < element.size() ; i++) {
				if(element.get(i).isSelected()) {
					element.get(i).removeData();
					element.remove(i);
					i--;
				}
			}
			for(int i = 0 ; i < node.size(); i++) {
				if(node.get(i).isSelected()) {
					for(GateIO io : ios) {
						if(io.isLinked() && io.getNode().equals(node.get(i))) {
							io.setNode(null);
						}
					} 
					node.remove(i);
					i--;
				}
			}
		}
		
		nb.keyPressed(e.getKeyCode());
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {	
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

}
