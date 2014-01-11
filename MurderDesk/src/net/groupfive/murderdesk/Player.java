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

	/** Direction constant for the player to walk. */
	public final static int LEFT_UP = 0;
	/** Direction constant for the player to walk. */
	public final static int RIGHT_DOWN = 1;
	/** Direction constant for the player to walk. */
	public final static int RIGHT_UP = 2;
	/** Direction constant for the player to walk. */
	public final static int LEFT_DOWN = 3;

	/** Width of the frames on the sprite sheet in pixel. */
	private final static int FRAME_WIDTH = 64;
	/** Height of the frames on the sprite sheet in pixel. */
	private final static int FRAME_HEIGHT = 64;
	/**
	 * x-Offset of where to render the player sprite in relation to the bottom
	 * left corner of the underlying tile.
	 */
	private final static int SPRITE_OFFSET_X = 0;
	/**
	 * y-Offset of where to render the player sprite in relation to the bottom
	 * left corner of the underlying tile.
	 */
	private final static int SPRITE_OFFSET_Y = 8;

	/**
	 * The player's coordinates in tile coordinates.
	 */
	private int tiledX, tiledY;

	/**
	 * The player's coordinates on the screen.
	 */
	private float x, y;

	/**
	 * The player's width and height in pixel.
	 */
	private float width, height;

	/** Determines whether the player is allowed to move or not. */
	private boolean allowMovement = true;

	/** Shows whether movement animation is still being played */
	private boolean animating;

	private int currentDirection = 1;

	// For random movement
	private Random randGenerator;
	private int randDirection;
	private int waitTime;
	private float movementTimer = 0f;

	// For interpolating movement
	private float originalX, originalY;
	private float newX, newY;
	private float movementSpeed = 0.5f; // 1 tile in ... seconds

	// Stats, ...
	public static final int DEFAULT_PULSE = 80;

	private static final int PULSE_MAXIMUM = 0;
	private static int PULSE_MAXMIMUM = 140;
	private static int PULSE_MINIMUM = 60;

	private int pulse = DEFAULT_PULSE; // Start Value
	private int health = 100;
	private int mentalPower = 100;
	private float regenerationTimer = 0f;

	private boolean dead = false;

	/** Contains sprite sheet for character. */
	private Texture spriteSheet;

	private TextureRegion[][] frames;
	private TextureRegion halfSprite;
	private int splitSpriteY = 32; // TODO Hardcoded values

	private Animation walkAnimationLU; // Left Up animation
	private Animation idleAnimationLU;
	private Animation walkAnimationRU; // Right Down animation
	private Animation idleAnimationRU;
	private Animation walkAnimationLD; // Left Down animation
	private Animation idleAnimationLD;
	private Animation walkAnimationRD; // Right Down animation
	private Animation idleAnimationRD;

	private Animation currentAnimation; // Current Animation

	private float animationStateTime;

	public Player() {
		spriteSheet = new Texture("textures/juji.png");

		frames = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);
		walkAnimationLU = new Animation(0.125f, frames[6]);
		idleAnimationLU = new Animation(0.125f, frames[2]);

		walkAnimationRU = new Animation(0.125f, frames[7]);
		walkAnimationRU.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationRU = new Animation(0.125f, frames[3]);

		walkAnimationRD = new Animation(0.125f, frames[5]);
		walkAnimationRD.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationRD = new Animation(0.125f, frames[1]);

		walkAnimationLD = new Animation(0.125f, frames[4]);
		walkAnimationLD.setPlayMode(Animation.LOOP_PINGPONG);
		idleAnimationLD = new Animation(0.125f, frames[0]);

		currentAnimation = walkAnimationLD;

		animationStateTime = 0f;
		animating = false;

		randGenerator = new Random();
		waitTime = randGenerator.nextInt(4);
	}

	public void update(float delta, GameMap map) {

		if (dead) {
			// TODO End the game
			allowMovement = false;
			System.out.println("Player just died!");
		}

		if (allowMovement) {
			if (!animating) {

				if (movementTimer < waitTime) {
					movementTimer += delta;
				} else {
					movementTimer = 0f;
					if (mentalPower >= 34) { // TODO Hardcoded value
						waitTime = randGenerator.nextInt(4);
						movementSpeed = 0.5f;
					} else {
						waitTime = 0;
						movementSpeed = 0.25f;
					}

					int newTileX = tiledX;
					int newTileY = tiledY;

					randDirection = randGenerator.nextInt(4);

					// To disable random movement
					randDirection = -1;

					// Moving with keys, should be replaced with AI
					if (Gdx.input.isKeyPressed(Keys.W) || randDirection == 0) {
						currentDirection = LEFT_UP;
						newTileX -= 1;
						newTileY += 0;
					} else if (Gdx.input.isKeyPressed(Keys.S)
							|| randDirection == 1) {
						currentDirection = RIGHT_DOWN;
						newTileX += 1;
						newTileY += 0;
					} else if (Gdx.input.isKeyPressed(Keys.D)
							|| randDirection == 2) {
						currentDirection = RIGHT_UP;
						newTileX += 0;
						newTileY -= 1;
					} else if (Gdx.input.isKeyPressed(Keys.A)
							|| randDirection == 3) {
						currentDirection = LEFT_DOWN;
						newTileX += 0;
						newTileY += 1;
					}

					if (!map.checkCollisionTile(newTileX, newTileY)) {
						if (tiledX != newTileX || tiledY != newTileY) {
							tiledX = newTileX;
							tiledY = newTileY;
							originalX = x;
							originalY = y;
							newX = map.getCornerTop().x
									+ map.convertMapToIsometricCoordinates(
											tiledX, tiledY).x
									- (map.getTilePixelWidth() / 2);
							newY = map.getCornerTop().y
									- map.convertMapToIsometricCoordinates(
											tiledX, tiledY).y
									- (map.getTilePixelHeight());
							animating = true;
							// System.out.println("Moving from " + originalX
							// + ", " + originalY + " to " + newX + ", "
							// + newY);
						}
					}
				}

			} else {

				// Do interpolation stuff
				x += (newX - originalX) * (delta / movementSpeed);
				y += (newY - originalY) * (delta / movementSpeed);

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
					animating = false;
				}
			}
		}

		setCurrentAnimation(currentDirection);

		animationStateTime += delta;

		// reset animationStateTime when animation is done
		if (animationStateTime > 0.5) {
			animationStateTime = 0f;
		}

		// Update the pulse
		pulse = (int) (80 - (1 - health / 100.0) * 20 + (1 - mentalPower / 100.0) * 60);

		// Check if player is dead
		if (pulse >= PULSE_MAXMIMUM || pulse <= PULSE_MINIMUM) {
			dead = true;
		}

		// Update the regeneration timer
		regenerationTimer += delta;

		// If there has not been a regeneration tick for 1 second
		if (regenerationTimer >= 1f) {
			System.out.println(health + "||" + mentalPower + "||" + pulse);
			if (health < 100) {
				health++;
			}
			if (mentalPower < 100) {
				mentalPower++;
			}

			regenerationTimer = 0f;
		}
	}

	public void draw(boolean topHalf, SpriteBatch spriteBatch) {

		if (topHalf) {
			halfSprite = new TextureRegion(currentAnimation.getKeyFrame(
					animationStateTime, true), 0, 0, 64, splitSpriteY);
			spriteBatch.draw(halfSprite, x + SPRITE_OFFSET_X, y + SPRITE_OFFSET_Y + (64 - splitSpriteY));
		} else {
			halfSprite = new TextureRegion(currentAnimation.getKeyFrame(
					animationStateTime, true), 0, splitSpriteY, 64, 64 - splitSpriteY);
			spriteBatch.draw(halfSprite, x + SPRITE_OFFSET_X, y + SPRITE_OFFSET_Y);
		}
	}

	public void allowMovement(boolean canMove) {
		allowMovement = canMove;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		x = boundingBox.x;
		y = boundingBox.y;
		width = boundingBox.width;
		height = boundingBox.height;
	}

	public void setCurrentDirection(int direction) {
		if (!animating) {
			direction = direction % 4;
			currentDirection = direction;
		} else {
			System.out
					.println("Could not invoke Player.setCurrentDirection(...): Player is moving!");
		}
	}

	public void spawn(int tileX, int tileY, GameMap map) {

		tiledX = tileX;
		tiledY = tileY;

		x = map.getCornerTop().x
				+ map.convertMapToIsometricCoordinates(tiledX, tiledY).x
				- (map.getTilePixelWidth() / 2);
		y = map.getCornerTop().y
				- map.convertMapToIsometricCoordinates(tiledX, tiledY).y
				- (map.getTilePixelHeight());
	}

	public void spawn(GameMap map) {

		if (map.hasLayer("Objects")) {
			if (map.hasObject("Objects", "Spawn")) {
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

				tiledX = (int) mapX;
				tiledY = (int) mapY;

				// Get isometic coordinates of the player box
				// Might be moved to GameMap.java later

				spawn(tiledX, tiledY, map);

			} else {
				spawn(0, 0, map);
			}
		} else {
			spawn(0, 0, map);
		}

	}

	public void dispose() {
		spriteSheet.dispose();
	}

	private void setCurrentAnimation(int direction) {

		switch (direction) {
		case 0:
			if (animating)
				currentAnimation = walkAnimationLU;
			else
				currentAnimation = idleAnimationLU;
			break;
		case 1:
			if (animating)
				currentAnimation = walkAnimationRD;
			else
				currentAnimation = idleAnimationRD;
			break;
		case 2:
			if (animating)
				currentAnimation = walkAnimationRU;
			else
				currentAnimation = idleAnimationRU;
			break;
		case 3:
			if (animating)
				currentAnimation = walkAnimationLD;
			else
				currentAnimation = idleAnimationLD;
			break;
		default:
			break;
		}
	}

	// ***************************
	// Getter- and Setter-Methods
	// ***************************

	public int getTiledX() {
		return tiledX;
	}

	public void setTiledX(int tileX) {
		this.tiledX = tileX;
	}

	public int getTiledY() {
		return tiledY;
	}

	public void setTiledY(int tileY) {
		this.tiledY = tileY;
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

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

	public int getCurrentDirection() {
		return currentDirection;
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	/**
	 * @return the pulse
	 */
	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		if (pulse > PULSE_MAXMIMUM) {
			this.pulse = PULSE_MAXIMUM;
		} else if (pulse < PULSE_MINIMUM) {
			this.pulse = PULSE_MINIMUM;
		} else {
			this.pulse = pulse;
		}
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health
	 *            the health to set
	 */
	public void setHealth(int health) {
		this.health = health % 101;
	}

	/**
	 * @return the mentalPower
	 */
	public int getMentalPower() {
		return mentalPower;
	}

	/**
	 * @param mentalPower
	 *            the mentalPower to set
	 */
	public void setMentalPower(int mentalPower) {
		this.mentalPower = mentalPower % 101;
	}
	
	public int getSplitSpriteY() {
		return splitSpriteY;
	}
	
	public void setSplitSpriteY(int splitSpriteY) {
		this.splitSpriteY = splitSpriteY;
	}

	public boolean isAnimating() {
		return animating;
	}

	public boolean isDead() {
		return dead;
	}
}
