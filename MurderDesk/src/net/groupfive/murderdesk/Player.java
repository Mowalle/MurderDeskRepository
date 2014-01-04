package net.groupfive.murderdesk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player {

	/** Contains sprite sheet for character. */
	private Texture spriteSheet;

	private boolean moving;

	/** Left Up animation */
	private Animation walkAnimationLU;
	/** Right Up animation */
	private Animation walkAnimationRU;
	/** Left Down animation */
	private Animation walkAnimationLD;
	/** Right Dorn animation */
	private Animation walkAnimationRD;

	private Animation currentAnimation;

	TextureRegion[][] frames;
	TextureRegion currentFrame;

	float animationStateTime;

	/**
	 * The player's coordinates on the screen.
	 */
	private float x, y;

	/**
	 * The player's width and height in pixel.
	 */
	public float width, height;

	/**
	 * The player's coordinates in tile coordinates.
	 */
	public int tileX, tileY;

	/**
	 * For interpolating the movement
	 */
	private float originalX, originalY;
	private float newX, newY;
	private static final float MOVEMENT_SPEED = 0.25f; // 1 tile in ... seconds

	public Player() {
		spriteSheet = new Texture("textures/Character.png");

		// Hardcoded values
		frames = TextureRegion.split(spriteSheet, 24, 32);
		walkAnimationLU = new Animation(0.125f, frames[0]);
		walkAnimationLU.setPlayMode(Animation.LOOP_PINGPONG);
		walkAnimationRU = new Animation(0.125f, frames[1]);
		walkAnimationRU.setPlayMode(Animation.LOOP_PINGPONG);
		walkAnimationLD = new Animation(0.125f, frames[2]);
		walkAnimationLD.setPlayMode(Animation.LOOP_PINGPONG);
		walkAnimationRD = new Animation(0.125f, frames[3]);
		walkAnimationRD.setPlayMode(Animation.LOOP_PINGPONG);

		currentAnimation = walkAnimationLU;

		animationStateTime = 0f;
		moving = false;
	}

	public void update(float delta, GameMap map) {

		if (!moving) {

			int newTileX = tileX;
			int newTileY = tileY;

			// Moving with keys, should be replaced with AI
			if (Gdx.input.isKeyPressed(Keys.W)) {
				currentAnimation = walkAnimationLU;
				newTileX -= 1;
				newTileY += 0;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				currentAnimation = walkAnimationLD;
				newTileX += 1;
				newTileY += 0;
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				currentAnimation = walkAnimationRD;
				newTileX += 0;
				newTileY += 1;
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				currentAnimation = walkAnimationRU;
				newTileX += 0;
				newTileY -= 1;
			}

			if (!map.checkCollisionTile(newTileX, newTileY)) {
				if (tileX != newTileX || tileY != newTileY) {
					tileX = newTileX;
					tileY = newTileY;
					originalX = x;
					originalY = y;
					newX = map.getTopCorner().x
							+ map.convertMapToIsometricCoordinates(tileX, tileY).x;
					newY = map.getTopCorner().y
							- map.convertMapToIsometricCoordinates(tileX, tileY).y;
					moving = true;
					System.out.println("Moving from " + originalX + ", " + originalY + " to " + newX + ", " + newY);
				}
			}

		} else {

			// Do interpolation stuff
			x += (newX - originalX) * (delta / MOVEMENT_SPEED);
			y += (newY - originalY) * (delta / MOVEMENT_SPEED);
			System.out.println(x + ", " + y);

			if (newX < originalX && x <= newX) {
				x = newX;
			} else if (newX > originalX && x >= newX) {
				x = newX;
			}
			
			if (newY < originalY && y <= newY) {
				y = newY;
			} else if (newY > originalY && y >= newY) {
				y = newY;
			}
			
			if (x == newX && y == newY) {
				moving = false;
			}
		}

		animationStateTime += delta;

		// reset animationStateTime when animation is done
		if (animationStateTime > 0.5) {
			animationStateTime = 0f;
		}

	}

	public void draw(SpriteBatch spriteBatch) {

		currentFrame = currentAnimation.getKeyFrame(animationStateTime, true);

		// Hardcoded Values
		spriteBatch.draw(currentFrame, x + 20, y + 15);
	}

	public void setBoundingBox(Rectangle boundingBox) {
		setX(boundingBox.x);
		setY(boundingBox.y);
		setWidth(boundingBox.width);
		setHeight(boundingBox.height);
	}

	/*
	 * Getter- and setter-methods.
	 */
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
