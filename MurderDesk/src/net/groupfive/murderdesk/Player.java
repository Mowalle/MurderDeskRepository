package net.groupfive.murderdesk;

import net.groupfive.murderdesk.screens.PlayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Sprite {

	public float width, height;

	private static final float MOVEMENT_COOLDOWN = 0.125f;
	private float currentMovementCooldown = 0.0f;
	
	public Player(Sprite sprite) {
		super(sprite);
	}

	public void update(float delta, GameMap map) {

		if (currentMovementCooldown >= MOVEMENT_COOLDOWN) {
			
			// Collisioncheck missing
			
			// Moving with keys, should be replaced with AI
			if (Gdx.input.isKeyPressed(Keys.W)) {
				setX(getX() + 32);
				setY(getY() + 16);
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				setX(getX() - 32);
				setY(getY() - 16);
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				setX(getX() - 32);
				setY(getY() + 16);
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				setX(getX() + 32);
				setY(getY() - 16);
			}
			
			currentMovementCooldown = 0.0f;
		}
		
		currentMovementCooldown += delta;

	}

	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
	}

	public void setBoundingBox(Rectangle boundingBox) {
		System.out.println("[Player]boundingBox: " + boundingBox.getX() + ", "
				+ boundingBox.getY());
		setX(boundingBox.x);
		setY(boundingBox.y);
		width = boundingBox.width;
		height = boundingBox.height;
	}
}
