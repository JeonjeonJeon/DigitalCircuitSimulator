package main;

import graphic.Frame;
import graphic.Graphic;
import graphic.WindowsFrame;

public class Main {
	/* variables related to frame counter */
	
	
	public static boolean FPS_LOCK = true;
	
	Frame f;
	
	public Main() {
		if(Property.isWindows() == false) {
			System.out.println("only support windows yet");
		}
		f = (Frame)new WindowsFrame("asdf");
	}
	
	public static void main(String[] args) {
		Main dcs = new Main(); // digital circuit simulator
		
		long now;
		int FPS = 0; //real FPS
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		int framesCounter = 0;
		
		while(true) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta>= 1) {
				delta--;
			}
			
			
			if(FPS_LOCK == true) {
				try {
					
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//////////////////////////
			
			dcs.f.render(FPS);
			
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
