package main;

import framework.Windows;

public class Main implements Runnable{
	/* variables related to frame counter */
	
	
	Windows window;

	public Main() {
		window = new Windows("Digital Circuit Simultor");
	}
	
	public static void main(String[] args) {
		Main dcs = new Main(); // digital circuit simulator
		//Simulation sim = Simulation.getInstance();

		Thread mainThread = new Thread(dcs);
		//Thread simThread = new Thread(sim);

		mainThread.start();
		//simThread.start();

	}
	
	@Override
	public void run() {
		int targetFPS = 60;
		int targetPeriod = 1000/targetFPS + 10; // unit: milli sec
		
		int FPS = 0;
		long period = targetPeriod;
		
		System.out.println("target FPS: " + targetFPS);
		System.out.println("target period: " + targetPeriod);
		
		long savedTime = System.currentTimeMillis();
		int count = 0;
		while(true) {
			
			window.render(FPS);
			count++;
			try {
				Thread.sleep(period);
			}
			catch(Exception e) {
				System.out.println("exception at therad main");
				e.printStackTrace();
			}
			
			
			if(System.currentTimeMillis() - savedTime > 1000) {
				savedTime = System.currentTimeMillis();
				FPS = count;
				count = 0;
			}
		}
	}
}



//
//	private void renderAlgorithm(Windows win) {
//		long now;
//		int FPS = 0; //real FPS
//		long lastTime = System.nanoTime();
//		double amountOfTicks = 60.0;
//		double ns = 1000000000 / amountOfTicks;
//		double delta = 0;
//		long timer = System.currentTimeMillis();
//		int frames = 0;
//		int framesCounter = 0;
//		boolean FPS_LOCK = true;
//		
//		while(true) {
//			now = System.nanoTime();
//			delta += (now - lastTime) / ns;
//			lastTime = now;
//			while(delta>= 1) {
//				delta--;
//			}
//			
//			
//			if(FPS_LOCK == true) {
//				try {
//					
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			//////////////////////////
//			
//			win.render(FPS);
//			
//			/////////////////////////////
//			frames++;
//			if(System.currentTimeMillis() - timer > 1000) {
//				timer += 1000;
//				framesCounter = frames;
//				frames = 0;
//			}
//			FPS = framesCounter;
//		}
//	}
//
