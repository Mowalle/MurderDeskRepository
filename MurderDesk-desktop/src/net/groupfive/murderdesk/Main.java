package net.groupfive.murderdesk;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.StyledDocument;

import net.groupfive.murderdesk.gui.GamePanel;
import net.groupfive.murderdesk.gui.ContentPanel;
import net.groupfive.murderdesk.gui.MurderDeskScreen;
import net.groupfive.murderdesk.gui.NormalText;
import net.groupfive.murderdesk.gui.NormalTextScroll;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

public class Main {
	
	static GUI gui;
	static Model m, n;
	static Controller c;
	
	public final static boolean GUI = true;
		
	/**
	 * Creates and shows the game or user interface.
	 * Use the GUI boolean to enable or disable the three-screen UI.
	 */
	public static void main(String[] args) {
		// set up the logic part (atm it's just a random number generator that signals the controller)
		// we want a shared controller (that takes care of ALL events) for the whole application
		c = new Controller();
		// set up as much Models or other stuff taking care of the logic as wanted.
		m = new Model(c);
		n = new Model(c);
		
		if(GUI){
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				@Override
	            public void run() {
	                gui = new GUI();
	                c.addObserver(gui);
	                //m.addObserver(gui);
	                //n.addObserver(gui);
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
		cfg.width = 800;
		cfg.height = 600;
		
		new LwjglApplication(new MurderDesk(), cfg);
	}
	
}
