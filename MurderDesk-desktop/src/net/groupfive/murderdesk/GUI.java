package net.groupfive.murderdesk;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.groupfive.murderdesk.gui.ContentPanel;
import net.groupfive.murderdesk.gui.GamePanel;
import net.groupfive.murderdesk.gui.MurderDeskScreen;
import net.groupfive.murderdesk.gui.NormalText;
import net.groupfive.murderdesk.gui.NormalTextScroll;

public class GUI implements Observer {
	
	public static Font ftCamcorder, ftMinecraftia, ftTitle1, ftTitle2, ftRegular, ftSmall;

	private NormalText txtObjectives;
	
	public GUI(){
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
	
	private void initScreen1(MurderDeskScreen s){
	}
	
	private void initScreen2(final MurderDeskScreen s){
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

	private void initScreen3(MurderDeskScreen s){
		ContentPanel pSubject = new ContentPanel("Subject");
		ContentPanel pObjectives = new ContentPanel("Objectives");
		ContentPanel pBalance = new ContentPanel("Balance");
		
		pSubject.setBounds(0, 0, 500, 556);
		pObjectives.setBounds(500, 0, 280, 400);
		pBalance.setBounds(500, 400, 280, 156);
		
		//initialize & add the individual components
		txtObjectives = new NormalText();
		pObjectives.add(new NormalTextScroll(txtObjectives), BorderLayout.CENTER);
		
		s.content.add(pSubject);
		s.content.add(pObjectives);
		s.content.add(pBalance);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(arg);
		try{
			txtObjectives.append(((Integer) arg).toString() + "\n"); 
			System.out.println(SwingUtilities.isEventDispatchThread());
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
