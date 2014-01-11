package net.groupfive.murderdesk.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class CharacterPanel extends JPanel implements ActionListener {
	
	private BufferedImage[] sprites;
	int pref = 0;
	int sprite;
	Timer timer;
	
	public CharacterPanel(){
		timer = new Timer (500, this);
	}
	
	public void load(InputStream s, int rows, int cols){
		try {
			BufferedImage bigImg = ImageIO.read(s);
			int width = bigImg.getWidth()/cols;
			int height = bigImg.getHeight()/rows;
			sprites = new BufferedImage[rows * cols];

			for (int i = 0; i < rows; i++)
			{
			    for (int j = 0; j < cols; j++)
			    {
			        sprites[(i * cols) + j] = bigImg.getSubimage(
			            j * width,
			            i * height,
			            width,
			            height
			        );
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void animate(boolean animate){
		if(animate){
			timer.start();
		} else {
			timer.stop();
			sprite = pref;
		}
	}
	
	public void setPreferredSprite(int i){
		this.pref = i;
		this.sprite = i;
	}
	
	@Override
	public void paintComponent(Graphics g){
	    super.paintComponent(g); 
	    g.drawImage(sprites[sprite],0,0,sprites[sprite].getWidth()*(getHeight()/sprites[sprite].getHeight()),getHeight(),null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(sprite >= sprites.length - 1){
			sprite = 0;
		}
		repaint();
		sprite++;
	}
}
