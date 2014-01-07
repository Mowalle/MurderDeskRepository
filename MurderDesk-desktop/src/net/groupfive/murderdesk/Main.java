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
	
	public final static boolean GUI = false;
	
	public static Font ftCamcorder, ftMinecraftia, ftTitle1, ftTitle2, ftRegular, ftSmall;
	
	/**
	 * Creates and shows the game or user interface.
	 * Use the GUI boolean to enable or disable the three-screen UI.
	 */
	public static void main(String[] args) {
		
		if(GUI){
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				@Override
	            public void run() {
	                createAndShowGUI();
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
	
	/**
	 * Very long and messy coded function that sets up the GUI
	 */
	public static void createAndShowGUI(){
		// add fonts globally
		try {
			InputStream s1 = Main.class.getResourceAsStream("/fonts/CAMCORDER_REG.otf");
			InputStream s2 = Main.class.getResourceAsStream("/fonts/Minecraftia.ttf");
			ftCamcorder = Font.createFont(Font.TRUETYPE_FONT, s1);
			ftMinecraftia = Font.createFont(Font.TRUETYPE_FONT,  s2);
			ftTitle1 = ftCamcorder.deriveFont(Font.PLAIN, 24);
			ftTitle2 = ftCamcorder.deriveFont(Font.PLAIN, 16);
			ftRegular = ftMinecraftia.deriveFont(Font.PLAIN, 11);
			ftSmall = ftMinecraftia.deriveFont(Font.PLAIN, 8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// create 3 windows for the game
		final MurderDeskScreen screen1 = new MurderDeskScreen("Map");
		final MurderDeskScreen screen2 = new MurderDeskScreen("Active View");
		final MurderDeskScreen screen3 = new MurderDeskScreen("Main");
		
		initScreen1(screen1);
		initScreen2(screen2);
		initScreen3(screen3);
		
		// pack
		screen1.pack();
		screen2.pack();
		screen3.pack();
		
		// show
        screen1.setVisible(true);
        screen2.setVisible(true);
        screen3.setVisible(true);
	}
	
	private static void initScreen1(MurderDeskScreen s){
		
	}
	
	private static void initScreen2(final MurderDeskScreen s){
		ContentPanel pCamera = new ContentPanel("Camera");
		ContentPanel pSound = new ContentPanel("Sound");
		ContentPanel pTraps = new ContentPanel("Traps");
		ContentPanel pConsole = new ContentPanel("Console");
		
		//pCamera.setBounds(0, 0, 640, 400);
		pCamera.setBounds(0,0,640,400);
		pSound.setBounds(640, 0, 140, 400);
		pTraps.setBounds(0, 400, 300, 156);
		pConsole.setBounds(300, 400, 480, 156);
		
		//initialize & add the individual components
		NormalText txtConsole = new NormalText();
		NormalTextScroll txtConsoleScroll = new NormalTextScroll(txtConsole);
		pConsole.add(txtConsoleScroll, BorderLayout.CENTER);
		
		s.content.add(pCamera);
		s.content.add(pSound);
		s.content.add(pTraps);
		s.content.add(pConsole);
	}

	private static void initScreen3(MurderDeskScreen s){
		ContentPanel pSubject = new ContentPanel("Subject");
		ContentPanel pObjectives = new ContentPanel("Objectives");
		ContentPanel pBalance = new ContentPanel("Balance");
		
		pSubject.setBounds(0, 0, 500, 556);
		pObjectives.setBounds(500, 0, 280, 400);
		pBalance.setBounds(500, 400, 280, 156);
		
		//initialize & add the individual components
		NormalText txtObjectives = new NormalText();
		pObjectives.add(txtObjectives, BorderLayout.CENTER);
		
		s.content.add(pSubject);
		s.content.add(pObjectives);
		s.content.add(pBalance);
	}
	
}
