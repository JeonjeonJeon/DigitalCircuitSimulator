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
	private static Thread simulThread;

	public static Simulation getInstance() {
		if(instance == null) {
			instance = new Simulation();
			simulThread = new Thread(instance);
		}
		return instance;
	}
	private Simulation() {
		System.out.println("simulation instance has been made");
		data = DataHandle.getInstance();
		isSim = false;
		simConti = false;
	}

	public void sim() {
		if(isSim == false) {
			System.out.println("fail");
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
		if(simConti == true) {
			System.out.println("");
			simulThread.interrupt();
			try {
				simulThread.join();
			} catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("Thread stop: "+simulThread.isAlive());
		}
		isSim = false;
		simConti = false;
		for(int i = 0; i < data.nodeSize(); i++) {
			data.getNode(i).setState(Voltage.LOW);
		}
	}
	
	public boolean startSim() {
		System.out.println("Start Sim");
		isSim = true;
		simConti = false;
		for(int i = 0; i < data.nodeSize(); i++) {
			Node nn = data.getNode(i);
			if(nn.startSim() == false) {
				isSim = false;
				System.out.println("Error found at some nodes. Simulation failed!!");
				return false;
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
		
		return true;
	}
	
	public void simContinuous() {
		System.out.println("continuous sim pressed");
		if(simConti == true){
			System.out.println("Simulation already continuous");
			return;
			
		}
		else if(isSim == false && startSim() == false) {
			System.out.println("unable to start simulation");
			return;
		}
		
		if(Simulation.simulThread.isAlive() == true) {
			System.out.println("simulation is in progress, press end sim first");
		}
		else {
			Simulation.simulThread.start();
		}
		simConti = true;	
	}

	@Override
	public void run() {
		System.out.println("thread run");
		while(Thread.currentThread().isInterrupted() == false) {
		/* do something */	
			sim();
			
			
			
			
//			try {
//				Thread.sleep(100);
//			} catch(InterruptedException ie) {
//				System.out.println("sleep exception");
//				Thread.currentThread().interrupt();
//			}
//			System.out.print(".");
		}
	}
}
