package net.groupfive.murderdesk;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

public class Player {

	/** Contains sprite sheet for character. */
	private Texture spriteSheet;

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

	/** Width of the frames on the sprite sheet in pixel. */
	private final static int FRAME_WIDTH = 24;
	/** Height of the frames on the sprite sheet in pixel. */
	private final static int FRAME_HEIGHT = 32;
	/**
	 * x-Offset of where to render the player sprite in relation to the bottom
	 * left corner of the underlying tile.
	 */
	private final static int SPRITE_OFFSET_X = 20;
	/**
	 * y-Offset of where to render the player sprite in relation to the bottom
	 * left corner of the underlying tile.
	 */
	private final static int SPRITE_OFFSET_Y = 15;

	/**
	 * Direction constants for the player to walk.
	 */
	private static final int LEFT_UP = 0;
	private static final int RIGHT_DOWN = 1;
	private static final int RIGHT_UP = 2;
	private static final int LEFT_DOWN = 3;

	/** Shows whether movement animation is still being played */
	private boolean moving;

	private int currentDirection = 1;

	/** Left Up animation */
	private Animation walkAnimationLU;
	private Animation idleAnimationLU;
	/** Right Up animation */
	private Animation walkAnimationRU;
	private Animation idleAnimationRU;
	/** Left Down animation */
	private Animation walkAnimationLD;
	private Animation idleAnimationLD;
	/** Right Dorn animation */
	private Animation walkAnimationRD;
	private Animation idleAnimationRD;

	private Animation currentAnimation;

	TextureRegion[][] frames;

	float animationStateTime;

	/**
	 * For interpolating the movement
	 */
	private float originalX, originalY;
	private float newX, newY;
	private static final float MOVEMENT_SPEED = 0.5f; // 1 tile in ... seconds

	/*
	 * For random movement
	 */
	private Random randGenerator;
	private int randDirection;
	private int waitTime;
	private float currentCooldownTime = 0f;

	public Player() {
		spriteSheet = new Texture("textures/Character.png");

		frames = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);
		walkAnimationLU = new Animation(0.125f, frames[0]);
		walkAnimationLU.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationLU = new Animation(0.125f, frames[0][1]);

		walkAnimationRU = new Animation(0.125f, frames[1]);
		walkAnimationRU.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationRU = new Animation(0.125f, frames[1][1]);

		walkAnimationRD = new Animation(0.125f, frames[2]);
		walkAnimationRD.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationRD = new Animation(0.125f, frames[2][1]);

		walkAnimationLD = new Animation(0.125f, frames[3]);
		walkAnimationLD.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationLD = new Animation(0.125f, frames[3][1]);

		currentAnimation = walkAnimationLD;

		animationStateTime = 0f;
		moving = false;

		randGenerator = new Random();
		waitTime = randGenerator.nextInt(4);
	}

	public void update(float delta, GameMap map) {

		if (!moving) {

			if (currentCooldownTime < waitTime) {
				currentCooldownTime += delta;
			} else {
				currentCooldownTime = 0f;
				waitTime = randGenerator.nextInt(4);

				int newTileX = tileX;
				int newTileY = tileY;

				randDirection = randGenerator.nextInt(4);
				
//				//To disable random movement
//				randDirection = -1;

				// Moving with keys, should be replaced with AI
				if (Gdx.input.isKeyPressed(Keys.W) || randDirection == 0) {
					currentDirection = LEFT_UP;
					newTileX -= 1;
					newTileY += 0;
				} else if (Gdx.input.isKeyPressed(Keys.S) || randDirection == 1) {
					currentDirection = RIGHT_DOWN;
					newTileX += 1;
					newTileY += 0;
				} else if (Gdx.input.isKeyPressed(Keys.D) || randDirection == 2) {
					currentDirection = RIGHT_UP;
					newTileX += 0;
					newTileY -= 1;
				} else if (Gdx.input.isKeyPressed(Keys.A) || randDirection == 3) {
					currentDirection = LEFT_DOWN;
					newTileX += 0;
					newTileY += 1;
				}

				if (!map.checkCollisionTile(newTileX, newTileY)) {
					if (tileX != newTileX || tileY != newTileY) {
						tileX = newTileX;
						tileY = newTileY;
						originalX = x;
						originalY = y;
						newX = map.getTopCorner().x
								+ map.convertMapToIsometricCoordinates(tileX, tileY).x - (map.getTilePixelWidth() / 2);
						newY = map.getTopCorner().y
								- map.convertMapToIsometricCoordinates(tileX, tileY).y - (map.getTilePixelHeight());
						moving = true;
						System.out.println("Moving from " + originalX + ", "
								+ originalY + " to " + newX + ", " + newY);
					}
				}
			}

		} else {

			// Do interpolation stuff
			x += (newX - originalX) * (delta / MOVEMENT_SPEED);
			y += (newY - originalY) * (delta / MOVEMENT_SPEED);

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

			// If goal was reached
			if (x == newX && y == newY) {
				moving = false;
			}
		}

		switch (currentDirection) {
		case 0:
			if (moving)
				currentAnimation = walkAnimationLU;
			else
				currentAnimation = idleAnimationLU;
			break;
		case 1:
			if (moving)
				currentAnimation = walkAnimationRD;
			else
				currentAnimation = idleAnimationRD;
			break;
		case 2:
			if (moving)
				currentAnimation = walkAnimationRU;
			else
				currentAnimation = idleAnimationRU;
			break;
		case 3:
			if (moving)
				currentAnimation = walkAnimationLD;
			else
				currentAnimation = idleAnimationLD;
			break;
		default:
			break;
		}

		animationStateTime += delta;

		// reset animationStateTime when animation is done
		if (animationStateTime > 0.5) {
			animationStateTime = 0f;
		}

	}

	public void draw(SpriteBatch spriteBatch) {

		spriteBatch.draw(
				currentAnimation.getKeyFrame(animationStateTime, true), x
						+ SPRITE_OFFSET_X, y + SPRITE_OFFSET_Y);
	}

	public void setBoundingBox(Rectangle boundingBox) {
		x = boundingBox.x;
		y = boundingBox.y;
		width = boundingBox.width;
		height = boundingBox.height;
	}

	public void spawn(int tiledX, int tiledY, GameMap map) {

		tileX = tiledX;
		tileY = tiledY;

		System.out.println(map.getTopCorner().y + " - " + map.convertMapToIsometricCoordinates(tiledX, tiledY).y);
		
		x = map.getTopCorner().x
				+ map.convertMapToIsometricCoordinates(tiledX, tiledY).x - (map.getTilePixelWidth() / 2);
		y = map.getTopCorner().y
				- map.convertMapToIsometricCoordinates(tiledX, tiledY).y - (map.getTilePixelHeight());
	}

	public void spawn(GameMap map) {

		if (map.hasLayer("Objects")) {
			if (map.hasSpawn()) {
				Rectangle playerBox = new Rectangle();

				float mapX, mapY;

				// If map contains object "Spawn", get a rectangle from the
				// bounding
				// box.
				if (map.getMapObject("Objects", "Spawn") instanceof RectangleMapObject) {
					playerBox = ((RectangleMapObject) map.getMapObject(
							"Objects", "Spawn")).getRectangle();

					// Get map coordinates of the player box
					// Might be moved to GameMap.java later
					mapX = map.convertScreenToMapCoordinates(
							playerBox.getX() * 2, 0).x;
					mapY = map.convertScreenToMapCoordinates(
							0,
							map.getMapPixelHeight() - playerBox.getY()
									- map.getTilePixelHeight()).y;
				} else {
					mapX = 0;
					mapY = 0;
				}

				tileX = (int) mapX;
				tileY = (int) mapY;
				
				// Get isometic coordinates of the player box
				// Might be moved to GameMap.java later
				

				spawn(tileX, tileY, map);
				

			} else {
				spawn(0, 0, map);
			}
		} else {
			spawn(0, 0, map);
		}

	}

	/*
	 * Getter- and setter-methods.
	 */
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void dispose() {
		spriteSheet.dispose();
	}
}
