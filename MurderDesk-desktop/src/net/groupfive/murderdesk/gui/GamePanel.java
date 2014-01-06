package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;

import net.groupfive.murderdesk.MurderDesk;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

@SuppressWarnings("serial")
public class GamePanel extends ContentPanel {
	
	LwjglCanvas canvas;

	public GamePanel(String t) {
		super(t);
		setLayout(new BorderLayout());
				
		canvas = new LwjglCanvas(new MurderDesk(), true);
		add(canvas.getCanvas(), BorderLayout.CENTER);	
	}
}