package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel{

	public ContentPanel(String t){
		setOpaque(false);
		setLayout(new BorderLayout());
		
		// some defaults to reuse
		Border whiteBorder = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0,1,0,1),
				BorderFactory.createLineBorder(Color.WHITE, 2));
		
		// add border
		TitledBorder title = BorderFactory.createTitledBorder(whiteBorder, t, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, GUI.ftTitle2, Color.WHITE);
		setBorder(title);
	}
}
