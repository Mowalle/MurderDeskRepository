package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class NormalTextScroll extends JScrollPane {
		
	public NormalTextScroll(Component view){
		super(view);
		setOpaque(false);
		getViewport().setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
}
