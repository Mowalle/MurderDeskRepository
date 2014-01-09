package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.groupfive.murderdesk.MurderDesk;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import net.groupfive.murderdesk.MurderDesk;

public class GamePanel extends JFrame {
	LwjglCanvas canvas;

	public GamePanel () {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = getContentPane();
		canvas = new LwjglCanvas(new MurderDesk(), true);

		container.add(canvas.getCanvas(), BorderLayout.CENTER);

		pack();
		setVisible(true);
		setSize(800, 600);
	}
	
	/*
	public static void main (String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				new GamePanel();
			}
		});
	}
	*/
}

/*
package net.groupfive.murderdesk.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.groupfive.murderdesk.MurderDesk;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

@SuppressWarnings("serial")
public class GamePanel extends JFrame {
	
	LwjglCanvas canvas;

	public GamePanel(String t) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		Container container = getContentPane();
		canvas = new LwjglCanvas(new Drop(), true);
		canvas.getCanvas().setSize(800,600);
		setBounds(0,0,800,600);
		container.add(canvas.getCanvas());
	}
}
*/