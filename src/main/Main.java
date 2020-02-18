package main;

import graphic.Graphic;
import graphic.LinuxFrame;
import graphic.WindowsFrame;

public class Main {
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
	public Graphic graphic;
	
	public DigitalCircuitSimulator() {
		Class c = Property.isLinux() ? Class.forName("LinuxFrame") : Class.forName("WindowFrame");
		graphic = (Graphic)c.newInstance();
	}
	
	public static void main(String[] args) {
		Main dcs = new Main(); // digital circuit simulator
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
			
			dcs.graphic.render(FPS);
			
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
