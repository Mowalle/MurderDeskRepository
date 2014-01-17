package net.groupfive.murderdesk.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.groupfive.murderdesk.Main;
import net.groupfive.murderdesk.MurderDesk;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.sun.awt.AWTUtilities;

@SuppressWarnings("serial")
public class GamePanel extends JFrame implements Runnable {
	
	private final static int WIDTH = 630;
	private final static int HEIGHT = 387;
	
	JLabel number;
	JLabel name;
	public JFrame label;
	
	public GamePanel(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public MurderDesk getGame(){
		return Main.murderDesk;
	}
	
	public void setRoom(int number, String name){
		this.number.setText(Integer.toString(number));
		this.name.setText(name);
	}

	@Override
	public void run() {
		// configuration
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
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
		setAlwaysOnTop(false);
		AWTUtilities.setWindowShape(this, new Rectangle2D.Double(0, 0, WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * CODE FOR THE LABEL ON TOP OF THE FRAME
		 */
		label = new JFrame();
		label.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		label.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		number = new JLabel();
		number.setForeground(Color.WHITE);
		number.setFont(Main.gui.ftCamcorderInv.deriveFont(Font.PLAIN, 50));
		label.add(number);
		name = new JLabel();
		name.setForeground(Color.WHITE);
		name.setBorder(new EmptyBorder(19,0,0,0));
		name.setFont(Main.gui.ftCamcorderInv.deriveFont(Font.PLAIN, 30));
		label.add(name);
		
		label.getRootPane().getRootPane().putClientProperty("Window.shadow", root);
		label.setUndecorated(true);
		label.setFocusable(false);
		label.setAlwaysOnTop(true);
		label.setPreferredSize(new Dimension(WIDTH, 100));
		label.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
		
		label.pack();
		
		// move label to postition of main frame
		addComponentListener(new ComponentAdapter() {
		    public void componentMoved(ComponentEvent e) {
		        label.setLocation(e.getComponent().getX() + 10, e.getComponent().getY() + 320);
		    }
		});
	}
}