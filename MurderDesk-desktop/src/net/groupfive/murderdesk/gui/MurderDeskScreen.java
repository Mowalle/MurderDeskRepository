package net.groupfive.murderdesk.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.groupfive.murderdesk.Main;
import net.groupfive.murderdesk.MurderDesk;

public class MurderDeskScreen extends JFrame{
	
	private static int instance = 1;
	public JPanel content;
	
	public MurderDeskScreen(String title){
		super(title);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init(title);		
		instance ++;
	}
	
	private void init(String title){
		// add the main panel (with title and border)
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(800,600));
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.BLACK);
		
		// create border borders
		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2,2,0,2),
				BorderFactory.createTitledBorder(
					BorderFactory.createTitledBorder(
							BorderFactory.createMatteBorder(24, 2, 2, 2, Color.WHITE),
							title,
							TitledBorder.CENTER,
							TitledBorder.TOP,
							Main.ftTitle1,
							Color.WHITE),
					"Murdersoft " + MurderDesk.VERSION + " - (c) 2014 EGI/5",
					TitledBorder.CENTER,
					TitledBorder.BOTTOM,
					Main.ftSmall,
					Color.WHITE));
		mainPanel.setBorder(border);
		
		// add extra title content
		JLabel lblTerminal = new JLabel("Terminal #16/" + instance);
		lblTerminal.setFont(Main.ftCamcorder.deriveFont(Font.PLAIN, 18));
		lblTerminal.setBounds(10, 5, 150, 21);
		mainPanel.add(lblTerminal);
		JLabel lblTime = new JLabel("13:42", SwingConstants.RIGHT);
		lblTime.setFont(Main.ftCamcorder.deriveFont(Font.PLAIN, 18));
		lblTime.setBounds(640, 5, 150, 21);
		mainPanel.add(lblTime);
		
		// add the container for all content
		content = new JPanel();
		content.setLayout(null);
		content.setOpaque(false);
		content.setBounds(10,32,780,556);
		mainPanel.add(content);
		
		// add
		getContentPane().add(mainPanel);
	}
}
