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
public class CharacterPanel extends JPanel {
	
	private BufferedImage[] sprites;
	int pref = 0;
	int sprite;
	
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
	
	public void setSprite(int i){
		this.pref = i;
		this.sprite = i;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
	    super.paintComponent(g); 
	    if(sprites != null){
	    	g.drawImage(sprites[sprite],0,0,sprites[sprite].getWidth()*(getHeight()/sprites[sprite].getHeight()),getHeight(),null);
	    }
	}
}
