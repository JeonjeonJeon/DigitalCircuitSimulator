package graphic;

// this is android(linux) based frame 
// nothing special to do 

public class LinuxFrame {
	
	public LinuxFrame(){
		ws = new WorkSpace();
	}

	public void render(int fps){
		ws.render(fps);
	}
}
