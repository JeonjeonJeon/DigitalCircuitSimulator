package datahandler;

import element.Element;
import element.Monitor;
import element.SubCircuit;
import element.Vdc;
import main.Node;
import main.Voltage;

public class Simulation implements Runnable{


	private DataHandle data;
	private boolean isSim;
	public boolean simConti = false;
	
	public static Simulation instance;
	public static Simulation getInstance() {
		if(instance == null) instance = new Simulation();
		return instance;
	}
	private Simulation() {
		data = DataHandle.getInstance();
		isSim = false;
	}

	public void sim() {
		if(isSim == false) {
			System.out.println("press STARTSIM first");
			return;
		}
		
		for(int i = 0; i < data.elementSize(); i++) {
			data.getElement(i).sim1();
		}
		for(int i = 0; i < data.elementSize(); i++) {
			data.getElement(i).sim2();
		}
	}	
	public void terminateSim() {
		if(isSim == false) {
			System.out.println("press STARTSIM first");
			return;
		}
		isSim = false;
		simConti = false;
		for(int i = 0; i < data.nodeSize(); i++) {
			data.getNode(i).setState(Voltage.LOW);
		}
	}
	
	public void startSim() {
		isSim = true;
		for(int i = 0; i < data.nodeSize(); i++) {
			Node nn = data.getNode(i);
			if(nn.startSim() == false) {
				isSim = false;
				System.out.println("Error found at some nodes. Simulation failed!!");
				return;
			}
		}
		for(int i = 0; i < data.nodeSize(); i++) {
			Node nn = data.getNode(i);
			nn.setState(Voltage.LOW);
		}
		
		for(int i = 0; i < data.elementSize(); i++) {
			Element ee = data.getElement(i);
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
	
	public void simContinuous() {
		if(isSim == false) {
				System.out.println("press SIM_START first");
				return;
			}
		simConti = true;	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
