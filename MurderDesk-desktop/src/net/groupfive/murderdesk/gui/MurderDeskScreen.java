package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.groupfive.murderdesk.GUI;
import net.groupfive.murderdesk.Main;
import net.groupfive.murderdesk.MurderDesk;

@SuppressWarnings("serial")
public class MurderDeskScreen extends JFrame{
	
	private static int instance = 1;
	public JPanel content;
	private JPanel mainPanel;
	private String title;
	int blue = 0;
	Clip clip;
	
	public MurderDeskScreen(String title){
		super(title);
		this.title = title;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();		
		instance ++;
	}
	
	private void init(){
		// add the main panel (with title and border)
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(800,600));
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setLayout(new BorderLayout());
		
		// add the container for all content
		content = new JPanel();
		content.setLayout(null);
		content.setOpaque(false);
		content.setBounds(10,32,780,556);
		
		getContentPane().add(mainPanel);
	}
	
	public void boot(){
		JPanel bootContainer = new JPanel();
		bootContainer.setLayout(new GridLayout(0,1));
		bootContainer.setBackground(Color.BLUE);
		mainPanel.add(bootContainer, BorderLayout.CENTER);
		
		final JLabel first = new JLabel(" ");
		bootContainer.add(first);
		
		final JLabel title = new JLabel();
		title.setFont(GUI.ftDSTerminal.deriveFont(Font.PLAIN, 100));
		title.setText("Controlsoft");
		title.setForeground(new Color(0,0,255));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		bootContainer.add(title);
		
		final JLabel about = new JLabel();
		about.setFont(GUI.ftRegular);
		about.setText(" ");
		about.setForeground(Color.WHITE);
		about.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		bootContainer.add(about);
			
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("/sounds/startup.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioIn);
	     } catch (Exception e){
	    	 e.printStackTrace();
	     }
		
		Timer fadeIn = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	blue+=5;
            	if(blue == 10){
        			clip.start();
            	}
            	if(blue > 255){
            		((Timer) e.getSource()).stop();
            	}
            	if(blue <=255){
            		title.setForeground(new Color(blue,blue,255));
            	}
            	if(blue == 200){
            		about.setText("Version " + MurderDesk.VERSION + " - (c) 2014 EGI/5");
            	}
            }
		});
		fadeIn.setInitialDelay(2000);
		fadeIn.start();
	}
	
	public void load(){
		mainPanel.removeAll();
		mainPanel.setLayout(null);
		// create border borders
		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2,2,0,2),
				BorderFactory.createTitledBorder(
					BorderFactory.createTitledBorder(
							BorderFactory.createMatteBorder(24, 2, 2, 2, Color.WHITE),
							title,
							TitledBorder.CENTER,
							TitledBorder.TOP,
							GUI.ftTitle1,
							Color.WHITE),
					"Murdersoft " + MurderDesk.VERSION + " - (c) 2014 EGI/5",
					TitledBorder.CENTER,
					TitledBorder.BOTTOM,
					GUI.ftSmall,
					Color.WHITE));
		mainPanel.setBorder(border);
		
		// add extra title content
		JLabel lblTerminal = new JLabel("Terminal #16/" + instance);
		lblTerminal.setFont(GUI.ftCamcorder.deriveFont(Font.PLAIN, 18));
		lblTerminal.setBounds(10, 5, 150, 21);
		mainPanel.add(lblTerminal);
		JLabel lblTime = new JLabel("13:42", SwingConstants.RIGHT);
		lblTime.setFont(GUI.ftCamcorder.deriveFont(Font.PLAIN, 18));
		lblTime.setBounds(640, 5, 150, 21);
		mainPanel.add(lblTime);
		
		mainPanel.add(content);
	}
}
