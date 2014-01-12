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
	
	/**
	 * Use the GUI boolean to enable or disable the three-screen UI.
	 */
	public final static boolean GUI = true;
		
	public static void main (String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","lcd");
		System.setProperty("swing.aatext", "true");
		c = new Controller();
		
		d = new DataController();
		d.log();
		
		if(GUI){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run () {
					//m = new Model(c);
					c.addObserver(new GUI());
				}
			});
		} else{
			//runAsSingleWindow();
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
	
}
