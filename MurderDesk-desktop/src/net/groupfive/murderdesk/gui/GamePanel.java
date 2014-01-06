package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.groupfive.murderdesk.MurderDesk;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

public class GamePanel extends ContentPanel {
	
	LwjglCanvas canvas;

	public GamePanel(String t) {
		super(t);
		
		setOpaque(true);
		setBackground(Color.PINK);

		//setLayout(new BorderLayout());
		//setLayout(null);
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = MurderDesk.TITLE + " " + MurderDesk.VERSION;
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 100;
		cfg.x = 0;
		cfg.y = 0;
		
		canvas = new LwjglCanvas(new MurderDesk(), true);
		canvas.getCanvas().setSize(300,300);
		add(canvas.getCanvas());
		

	}
}