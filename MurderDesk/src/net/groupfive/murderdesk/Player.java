package net.groupfive.murderdesk;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Sprite {

	public float width, height;
	
	public Player(Sprite sprite) {
		super(sprite);
	}
	
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
	}
	
	public void setBoundingBox(Rectangle boundingBox) {
		System.out.println("[Player]boundingBox: " + boundingBox.getX() + ", " + boundingBox.getY());
		setX(boundingBox.x);
		setY(boundingBox.y);
		width = boundingBox.width;
		height = boundingBox.height;
	}
}
