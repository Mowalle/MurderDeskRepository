package net.groupfive.murderdesk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Sprite {

	public float width, height;
	public int tileX, tileY;

	private static final float MOVEMENT_COOLDOWN = 0.125f;
	private float currentMovementCooldown = 0.0f;

	public Player(Sprite sprite) {
		super(sprite);
	}

	public void update(float delta, GameMap map) {

		if (currentMovementCooldown >= MOVEMENT_COOLDOWN) {

			// Collisioncheck missing

			int newX = tileX;
			int newY = tileY;
			boolean pressedButton = false;
			// Moving with keys, should be replaced with AI
			if (Gdx.input.isKeyPressed(Keys.W)) {
				newX -= 1;
				newY += 0;
				pressedButton = true;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				newX += 1;
				newY += 0;
				pressedButton = true;
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				newX += 0;
				newY += 1;
				pressedButton = true;
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				newX += 0;
				newY -= 1;
				pressedButton = true;
			}

			if (pressedButton) {
				if (!map.checkCollisionTile(newX, newY)) {
					tileX = newX;
					tileY = newY;
				}
			}

			currentMovementCooldown = 0.0f;
		}

		currentMovementCooldown += delta;

		setX(map.getTopCorner().x
				+ map.convertMapToIsometricCoordinates(tileX, tileY).x);
		setY(map.getTopCorner().y
				- map.convertMapToIsometricCoordinates(tileX, tileY).y);

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
