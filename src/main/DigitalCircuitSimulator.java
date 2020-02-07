package main;

import window.Window;

public class DigitalCircuitSimulator {
	/* variables related to frame counter */
	public static int FPS = 0; //real FPS
	private static long lastTime = System.nanoTime();
	private static double amountOfTicks = 60.0;
	private static double ns = 1000000000 / amountOfTicks;
	private static double delta = 0;
	private static long timer = System.currentTimeMillis();
	private static int frames = 0;
	private static int framesCounter;
	
	public static boolean FPS_LOCK = true;
	public Window window;
	
	public DigitalCircuitSimulator() {
		window = new window.Window();
	}
	
	public static void main(String[] args) {
		DigitalCircuitSimulator dcs = new DigitalCircuitSimulator();
		long now;
		while(true) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta>= 1) {
				delta--;
			}
			
			
			if(FPS_LOCK == true) {
				try {
					
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//////////////////////////
			
			dcs.window.render(FPS);
			
			/////////////////////////////
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				framesCounter = frames;
				frames = 0;
			}
			FPS = framesCounter;
		}
	}
}
