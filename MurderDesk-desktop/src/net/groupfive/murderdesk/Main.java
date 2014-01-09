package net.groupfive.murderdesk;

import javax.swing.SwingUtilities;

import net.groupfive.murderdesk.gui.GamePanel;

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
	
	/**
	 * Use the GUI boolean to enable or disable the three-screen UI.
	 */
	public final static boolean GUI = false;
		
	public static void mains (String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				new GamePanel();
			}
		});
	}
	
	public static void runAsSingleWindow(){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = MurderDesk.TITLE + " " + MurderDesk.VERSION;
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 600;
		
		new LwjglApplication(new MurderDesk(), cfg);
	}
	
}
