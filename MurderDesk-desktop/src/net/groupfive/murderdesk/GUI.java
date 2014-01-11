package net.groupfive.murderdesk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import net.groupfive.murderdesk.data.Objective;
import net.groupfive.murderdesk.data.Subject;
import net.groupfive.murderdesk.gui.CharacterPanel;
import net.groupfive.murderdesk.gui.ContentPanel;
import net.groupfive.murderdesk.gui.ECDPanel;
import net.groupfive.murderdesk.gui.GamePanel;
import net.groupfive.murderdesk.gui.MurderDeskScreen;
import net.groupfive.murderdesk.gui.NormalText;
import net.groupfive.murderdesk.gui.NormalTextScroll;

public class GUI implements Observer {
	
	public final static boolean FULLSCREEN = false;
	
	public static Font ftCamcorder, ftMinecraftia, ftTitle1, ftTitle2, ftRegular, ftSmall;

	private NormalText txtObjectives, txtSubject_general, txtSubject_status, txtSubject_detail, txtBalance, txtTraps, txtConsole;
	private ECDPanel pSubject_bpm;
	private CharacterPanel pSubject_img;
	
	private GamePanel game;
	
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
				//final MurderDeskScreen screen1 = new MurderDeskScreen("Map");
				final MurderDeskScreen screen2 = new MurderDeskScreen("Active View");
				final MurderDeskScreen screen3 = new MurderDeskScreen("Main");
				
				//initScreen1(screen1);
				initScreen2(screen2);
				initScreen3(screen3);
				
				// populate
				populate();
				
				game = new GamePanel();
				game.pack();
				game.setVisible(true);
				
				if(FULLSCREEN){
					
					GraphicsEnvironment env;
					GraphicsDevice s1;
					GraphicsDevice s2;
					DisplayMode s1_newMode;
					DisplayMode s2_newMode;
					
					try{
						env = GraphicsEnvironment.getLocalGraphicsEnvironment();  
						s1 = env.getScreenDevices()[0];
						s2 = env.getScreenDevices()[1];
						
						DisplayMode s1_oldMode = s1.getDisplayMode(); 
						DisplayMode s2_oldMode = s2.getDisplayMode();
						
						s1_newMode = new DisplayMode(800, 600, s1_oldMode.getBitDepth(), s1_oldMode.getRefreshRate());
						s2_newMode = new DisplayMode(800, 600, s2_oldMode.getBitDepth(), s2_oldMode.getRefreshRate());
						
						screen2.setUndecorated(true);
						screen3.setUndecorated(true);
						screen2.setResizable(true);
						screen3.setResizable(true);
						
						//s1.setFullScreenWindow(screen2);
						//s2.setFullScreenWindow(screen3);
						
						screen2.setSize(Toolkit.getDefaultToolkit().getScreenSize());
						screen3.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	
						//check low-level display changes are supported for this graphics device.
						if(s1.isDisplayChangeSupported()){
							try{
								s1.setDisplayMode(s1_newMode);
							}finally{
								s1.setDisplayMode(null);
							}
						}
						if(s1.isDisplayChangeSupported()){
							try{
								s2.setDisplayMode(s2_newMode);
							}finally{
								s2.setDisplayMode(null);
							}
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
				if(!FULLSCREEN){
					screen2.addComponentListener(new ComponentAdapter() {
					    public void componentMoved(ComponentEvent e) {
					        game.setLocation(screen2.getX()+15, screen2.getY()+63);
					    }
					});
				}
				
				// pack
				//screen1.pack();
				screen2.pack();
				screen3.pack();
				
				// show
		       	screen3.setVisible(true);
		       	screen2.setVisible(true);
		        //screen1.setVisible(true);
		       	
		       	// position
		       	screen3.setLocation(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width - 800, 0);
	}
	
	/*
	private void initScreen1(MurderDeskScreen s){
	}
	*/
	
	private void initScreen2(final MurderDeskScreen s){
		ContentPanel pCamera = new ContentPanel("Camera");
		ContentPanel pSound = new ContentPanel("Sound");
		ContentPanel pTraps = new ContentPanel("Traps");
		ContentPanel pConsole = new ContentPanel("Console");
		
		pCamera.setBounds(0,0,640,400);
		pSound.setBounds(640, 0, 140, 400);
		pTraps.setBounds(0, 400, 300, 156);
		pConsole.setBounds(300, 400, 480, 156);
		
				
		//initialize & add the individual components
		txtConsole = new NormalText();
		NormalTextScroll txtConsoleScroll = new NormalTextScroll(txtConsole);
		pConsole.add(txtConsoleScroll, BorderLayout.CENTER);
		txtTraps = new NormalText();
		Style style = txtTraps.getStyle();
		style.addAttribute(StyleConstants.SpaceAbove, 7.0f);
		style.addAttribute(StyleConstants.LineSpacing, 1.0f);
		txtTraps.setStyle(style);
		pTraps.add(txtTraps, BorderLayout.CENTER);

		s.content.add(pCamera);
		s.content.add(pSound);
		s.content.add(pTraps);
		s.content.add(pConsole);
		
		System.out.println(pCamera.getWidth() + "x" + pCamera.getHeight());
	}

	private void initScreen3(MurderDeskScreen s){
		ContentPanel pSubject = new ContentPanel("Subject");
		ContentPanel pObjectives = new ContentPanel("Objectives");
		ContentPanel pBalance = new ContentPanel("Balance");
		
		pSubject.setBounds(0, 0, 500, 556);
		pObjectives.setBounds(500, 0, 280, 400);
		pBalance.setBounds(500, 400, 280, 156);
		
		JPanel pSubject_main = new JPanel();
		pSubject_main.setOpaque(false);
		Border b = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2,3,2,3),
				BorderFactory.createLineBorder(Color.WHITE, 2));
		pSubject_main.setBorder(b);
		ContentPanel pSubject_detail = new ContentPanel("Info");
		pSubject.add(pSubject_main, BorderLayout.NORTH);
		pSubject.add(pSubject_detail, BorderLayout.CENTER);
		
		pSubject_img = new CharacterPanel();
		JPanel pSubject_info = new JPanel();
		pSubject_bpm = new ECDPanel();
		JPanel pSubject_status = new JPanel();
		pSubject_img.setOpaque(false);
		pSubject_info.setOpaque(false);
		pSubject_bpm.setOpaque(false);
		pSubject_status.setOpaque(false);
		pSubject_img.setLayout(new BorderLayout());
		pSubject_info.setLayout(new BorderLayout());
		pSubject_bpm.setLayout(new BorderLayout());
		pSubject_status.setLayout(new BorderLayout());
		pSubject_img.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
		pSubject_info.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
		//pSubject_bpm.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.WHITE));
		pSubject_status.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.WHITE));
		
		pSubject_main.setLayout(null);
		pSubject_main.setPreferredSize(new Dimension(0,250));
		pSubject_img.setBounds(3,3,147,175); // width: 3 - 478 - 3
		pSubject_info.setBounds(150,3,331,175);
		pSubject_bpm.setBounds(3,178,330,72);
		pSubject_status.setBounds(333, 178, 150,68);
		pSubject_main.add(pSubject_img);
		pSubject_main.add(pSubject_info);
		pSubject_main.add(pSubject_bpm);
		pSubject_main.add(pSubject_status);
		
		//initialize & add the individual components
		txtObjectives = new NormalText();
		pObjectives.add(new NormalTextScroll(txtObjectives), BorderLayout.CENTER);
		txtObjectives.setSpacing(0.5f);
		txtBalance = new NormalText();
		Style style = txtBalance.getStyle();
		style.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
		style.addAttribute(StyleConstants.FontFamily, ftCamcorder.getFamily());
		style.addAttribute(StyleConstants.FontSize, 48);
		style.addAttribute(StyleConstants.SpaceAbove, 30.0f);
		txtBalance.setStyle(style);
		pBalance.add(txtBalance);
		txtSubject_status = new NormalText();
		style.addAttribute(StyleConstants.Foreground, Color.BLACK);
		style.addAttribute(StyleConstants.FontSize, 32);
		style.addAttribute(StyleConstants.SpaceAbove, 15.0f);
		txtSubject_status.setStyle(style);
		pSubject_status.add(txtSubject_status, BorderLayout.CENTER);
		txtSubject_general = new NormalText();
		pSubject_info.add(txtSubject_general, BorderLayout.CENTER);
		txtSubject_detail = new NormalText();
		pSubject_detail.add(txtSubject_detail, BorderLayout.CENTER);
		
		s.content.add(pSubject);
		s.content.add(pObjectives);
		s.content.add(pBalance);
	}
	
	private void populate(){
		// objectives
		ArrayList<Objective> objectives = Main.d.getObjectives();
		for(int i=0; i<objectives.size(); i++){
			Objective o = objectives.get(i);
			txtObjectives.append(o.getDescription() + " (+$" + o.getValue() + ")\n");
		}
		
		// subject_general
		Subject s = Main.d.getSubject(Main.d.CURRENT_SUBJECT);
		TabStop[] tabs = new TabStop[1];
	    tabs[0] = new TabStop(125, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
	    TabSet tabset = new TabSet(tabs);
	    Style style = txtSubject_general.getStyle();
		style.addAttribute(StyleConstants.TabSet, tabset);
		style.addAttribute(StyleConstants.LineSpacing, 0.5f);
		txtSubject_general.setStyle(style);
		txtSubject_general.append("ID:\t" + s.getId() + "\n");
		txtSubject_general.append("First Name:\t" + s.getFirstName() + "\n");
		txtSubject_general.append("Last Name:\t" + s.getName() + "\n");
		
		// subject_status
		txtSubject_status.setOpaque(true);
		txtSubject_status.setBackground(Color.GREEN);
		txtSubject_status.append("ALIVE");
		
		// bpm
		pSubject_bpm.enableSound(false);
		
		// image
		pSubject_img.load(Main.class.getResourceAsStream("/textures/CharacterOskar.png"), 5, 3);
		pSubject_img.setPreferredSprite(11);
		
		txtSubject_detail.append(Main.d.getSubject(Main.d.CURRENT_SUBJECT).getStory(0));
		
		// balance
		txtBalance.append("$ 0");
		
		// traps
		String[] traps = Main.d.getRooms().get(Main.d.CURRENT_ROOM).getTraps();
		txtTraps.append("[1]  " + traps[0]+"\n");
		txtTraps.append("[2]  " + traps[1]+"\n");
		txtTraps.append("[3]  " + traps[2]);
		
		// console
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		txtConsole.append("["+dateFormat.format(date)+"] Application initialized");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		Message m = (Message) arg;
		
		try{
			txtBalance.setText("$"+m.getString());
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
