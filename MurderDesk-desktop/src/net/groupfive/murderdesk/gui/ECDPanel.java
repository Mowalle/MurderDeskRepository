package net.groupfive.murderdesk.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import net.groupfive.murderdesk.Main;

@SuppressWarnings("serial")
public class ECDPanel extends JPanel implements ActionListener {
	
	ArrayList<Point> trace;
	Timer timer, beatTimer;
	double x = 0;
	double y = 0;
	int y_ = 0;
	double[] yList;
	int width = 0;
	int height = 0;
	int beat = 50;
	boolean beating = true;
	Clip clipAlive;
	Clip clipDead;
	boolean alive = true;
	boolean sound = false;
	
	public ECDPanel(){
		
		timer = new Timer (1, this);
		timer.start();
		
		trace = new ArrayList<Point>();
		yList = new double[] {-2,-4,-6,-4,-2,0,0,2,-15,-30,0,20,15,10,0,0,0,-1,-2,-3,-3,-2,-1};
		
		addAncestorListener ( new AncestorListener (){
	        public void ancestorAdded ( AncestorEvent event )
	        {
	           width = getWidth();
	           height = getHeight()/2+3;
	           System.out.println(width);
	        }

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}
	    });
		
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("/sounds/heart_beat_short.wav"));
			clipAlive = AudioSystem.getClip();
			clipAlive.open(audioIn);
			 
			audioIn = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("/sounds/heart_flat.wav"));
			clipDead = AudioSystem.getClip();
			clipDead.open(audioIn);
	     } catch (Exception e){
	    	 e.printStackTrace();
	     }
		
		beatTimer = new Timer(60000/beat, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	beating = true;
            	if(sound){
            		/*
            		if(sound){
            			Thread t = new Thread(new HeartBeep());
                    	t.start();
            		}
            		 */
            		clipAlive.start();
    	        	clipAlive.setFramePosition(0);
            	}
            }
		});
		beatTimer.start(); 
	}
	
	public void setBeat(int beat){
		this.beat = beat;
		beatTimer.setDelay(60000/beat);
	}
	
	public void kill(){
		this.alive = false;
		beatTimer.stop();
		if(sound) clipDead.loop(-1);
	}
	
	public void enableSound(boolean b){
		this.sound = b;
	}
	
	@Override
	public void paintComponent(Graphics g){
	    super.paintComponent(g);   // paint whatever normally gets painted.
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(2));
	    for(int i = 1; i < trace.size(); i++){
	    	Point p1 = trace.get(i-1);
	    	Point p2 = trace.get(i);
	    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,i * 0.01f));
	    	if(!(p1.getX() == width && p2.getX() == 0)){
	    		g2d.drawLine ((int)p1.getX(), (int)p1.getY()+height, (int)p2.getX(), (int)p2.getY()+height);
	    	}
	    }	    
	}
	
	public void actionPerformed (ActionEvent e)
    {
		if(x > width){
			x = 0;
		}
		if(y_ >= yList.length){
			y_ = 0;
		}
		if(trace.size() > 100){
			trace.remove(1);
		}
		
		Point p = new Point();
		p.setLocation(x, y);
		trace.add(p);
		
		x++;
		if(beating){
			y = yList[y_];
			y_++;
			if(y_ >= yList.length){
				y_ = 0;
				beating = false;
			}
		}
		
        repaint();
    }
}
