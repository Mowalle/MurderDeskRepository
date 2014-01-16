package net.groupfive.murderdesk.gui;

import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import net.groupfive.murderdesk.Main;
import net.groupfive.murderdesk.MurderDesk;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.sun.awt.AWTUtilities;

@SuppressWarnings("serial")
public class GamePanel extends JFrame {
	
	private final static int WIDTH = 630;
	private final static int HEIGHT = 387;
	public GamePanel() {
				
		// configuration
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = MurderDesk.TITLE + " " + MurderDesk.VERSION;
		cfg.useGL20 = true;
		cfg.width = WIDTH;
		cfg.height = HEIGHT;
		
		// create canvas & add to frame
		LwjglCanvas canvas = new LwjglCanvas(Main.murderDesk, cfg);
		canvas.getCanvas().setSize(WIDTH,HEIGHT);	
		getContentPane().add(canvas.getCanvas());
		
		// (un)decorate the frame
		JRootPane root = getRootPane();
		root.getRootPane().putClientProperty("Window.shadow", root);
		setUndecorated(true);
		setFocusable(false);
		setAlwaysOnTop(true);
		AWTUtilities.setWindowShape(this, new Rectangle2D.Double(0, 0, WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public MurderDesk getGame(){
		return Main.murderDesk;
	}
}