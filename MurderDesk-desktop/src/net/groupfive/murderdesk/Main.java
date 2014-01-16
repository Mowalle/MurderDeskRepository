package net.groupfive.murderdesk;

import javax.swing.SwingUtilities;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Main class of the MurderDesk game. The most important parts are:<br>
 * [1] Controller. This part of the application collects all user/AI input and will notify all observers of changes<br>
 * [x] Observers. All classes that implement the Observer class will be linked to the controller and notified accordingly<br>
 * [x] Models. These provide the game logic. Pass the Controller (in the constructor or aftwerwards) and use any of the functions available.
 */
public class Main {
	
	static GUI gui;
	static Model m, n;
	static Controller c;
	public static DataController d;
	public static MurderDesk murderDesk;
	protected static InterfaceLink il;
	
	/**
	 * Some booleans to change the behavior
	 */
	public final static boolean GUI = true;
	public final static boolean INTERFACE = false;
	public final static boolean FULLSCREEN = false;
	public final static boolean BOOTSCREEN = false;
		
	public static void main (String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","lcd");
		System.setProperty("swing.aatext", "true");
		c = new Controller();
		
		d = new DataController();
		d.log();
		
		murderDesk = new MurderDesk();
		murderDesk.setController(c);
		
		il = new InterfaceLink();
		il.initialize();
		
		ShutdownHook shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		
		if(GUI){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run () {
					gui = new GUI();
					gui.init();
					gui.show();
					if(!INTERFACE){
						boot();
					}
					//c.addObserver(gui);
				}
			});
		} else{
			runAsSingleWindow();
		}
	}
	
	public static void runAsSingleWindow(){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = MurderDesk.TITLE + " " + MurderDesk.VERSION;
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 400;
		
		new LwjglApplication(new MurderDesk(), cfg);
	}
	
	public static void boot(){
		gui.boot(BOOTSCREEN);
	}
	
}

class ShutdownHook extends Thread {
    public void run() {
        System.out.println("Shutting down");
        try{
        	Main.il.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
    }
}
