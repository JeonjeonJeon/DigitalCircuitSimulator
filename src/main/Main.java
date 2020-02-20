package main;

import graphic.Frame;
import graphic.WindowsFrame;

public class Main {
	/* variables related to frame counter */
	
	
	Frame f;
	
	public Main() {
		if(Property.isWindows() == false) {
			System.out.println("only support windows yet");
		}
		f = (Frame)new WindowsFrame("asdf");
	}
	
	public static void main(String[] args) {
		Main dcs = new Main(); // digital circuit simulator
		
		try {
			dcs.renderAlgorithm2(dcs.f);
		} catch(InterruptedException ie) {
			ie.printStackTrace();
			System.exit(0);
		}
	}
	
	private void renderAlgorithm2(Frame f) throws InterruptedException{
		int targetFPS = 60;
		int targetPeriod = 1000/targetFPS; // unit: milli sec
		
		int FPS = 0;
		long period = targetPeriod;
		
		System.out.println("target FPS: " + targetFPS);
		System.out.println("target period: " + targetPeriod);
		
		long savedTime = System.currentTimeMillis();
		int count = 0;
		while(true) {
			
			f.render(FPS);
			count++;
			Thread.sleep(period);
			
			
			if(System.currentTimeMillis() - savedTime > 1000) {
				savedTime = System.currentTimeMillis();
				FPS = count;
				count = 0;
			}
		}
		
	}
	
	
	private void renderAlgorithm(Frame f) {
		long now;
		int FPS = 0; //real FPS
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		int framesCounter = 0;
		boolean FPS_LOCK = true;
		
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
			
			f.render(FPS);
			
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
