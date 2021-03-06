package net.groupfive.murderdesk.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.groupfive.murderdesk.model.Door;
import net.groupfive.murderdesk.model.Player;
import net.groupfive.murderdesk.model.Room;
import net.groupfive.murderdesk.model.World;
import net.groupfive.murderdesk.model.Player.Condition;
import net.groupfive.murderdesk.model.Player.State;

import com.badlogic.gdx.math.Vector2;

public class PlayerController {

	enum Keys {
		LEFT_DOWN, LEFT_UP, RIGHT_DOWN, RIGHT_UP
	}

	private World world;
	private Player player;

	static Map<Keys, Boolean> keys = new HashMap<PlayerController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT_DOWN, false);
		keys.put(Keys.LEFT_UP, false);
		keys.put(Keys.RIGHT_DOWN, false);
		keys.put(Keys.RIGHT_UP, false);
	};

	// Needed for interpolation of player movement.
	/** Old position of player before movement. **/
	private Vector2 oldPosition = new Vector2();
	/** Destination position of player for movement, **/
	private Vector2 newPosition = new Vector2();

	// Needed for random movement
	private boolean movingRandom = true;
	private Random randGenerator = new Random();
	private int randWaitTime;
	private float movementTimer = 0f;
	// To move towards doors
	private boolean directedMovement = false;
	private boolean changeFirst = true;
	private Door lastDoorUsed;
	

	public PlayerController(World world) {
		this.world = world;
		this.player = world.getPlayer();
	}

	// ** Key presses and touches **************** //

	public void leftDownPressed() {
		keys.get(keys.put(Keys.LEFT_DOWN, true));
	}

	public void leftUpPressed() {
		keys.get(keys.put(Keys.LEFT_UP, true));
	}

	public void rightDownPressed() {
		keys.get(keys.put(Keys.RIGHT_DOWN, true));
	}

	public void rightUpPressed() {
		keys.get(keys.put(Keys.RIGHT_UP, true));
	}

	public void leftDownReleased() {
		keys.get(keys.put(Keys.LEFT_DOWN, false));
	}

	public void leftUpReleased() {
		keys.get(keys.put(Keys.LEFT_UP, false));
	}

	public void rightDownReleased() {
		keys.get(keys.put(Keys.RIGHT_DOWN, false));
	}

	public void rightUpReleased() {
		keys.get(keys.put(Keys.RIGHT_UP, false));
	}

	/** The main update method. **/
	public void update(float delta) {

		// Updating random movement if necessary
		if (movingRandom)
			updateRandomMovement(delta);

		// Processing the input - setting the states of Player
		processInput();

		// Check if player should be teleported
		if (checkCollisionWithDoor(player.getPosition().x,
				player.getPosition().y)) {

			Door door = getCollidingDoor(player.getPosition().x,
					player.getPosition().y);

			// If player is inside door, teleport him and set him up for new
			// movement
			if (door.getPosition().x == player.getPosition().x
					&& door.getPosition().y == player.getPosition().y) {
				lastDoorUsed = door;

				player.getPosition().x = door.getTargetDoor().getPosition().x;
				player.getPosition().y = door.getTargetDoor().getPosition().y;
				player.setMyRoom(door.getTargetDoor().getMyRoom());

				if (door.getTargetDoor().isFacingLeft()) {
					player.setFacingLeft(true);
					player.setFacingDown(true);
					player.setState(State.WALKING);
					oldPosition = player.getPosition().cpy();
					newPosition = oldPosition.cpy().add(-0.5f, -0.5f);
				} else {
					player.setFacingLeft(false);
					player.setFacingDown(true);
					player.setState(State.WALKING);
					oldPosition = player.getPosition().cpy();
					newPosition = oldPosition.cpy().add(0.5f, -0.5f);
				}

			}

		}

		// Check if the latest used door has been closed since the player used
		// it
		if (lastDoorUsed != null && !lastDoorUsed.isOpen()) {
			lastDoorUsed = null;
		}

		// TODO Collision handling
		if (player.getState().equals(State.WALKING)) {
			// If there is a door placed, ignore collision and use the door
			if (checkCollisionWithDoor(newPosition.x, newPosition.y)) {

			} else if (checkCollisionWithTile(newPosition)) {
				player.setState(State.IDLE);
				oldPosition = new Vector2();
				newPosition = new Vector2();
			}
		}

		// If player is walking
		if (player.getState().equals(State.WALKING)) {

			player.getPosition().x += (newPosition.x - oldPosition.x)
					* (delta / player.getSpeed());
			player.getPosition().y += (newPosition.y - oldPosition.y)
					* (delta / player.getSpeed());

			// If player x == newPosition x
			if (newPosition.x < oldPosition.x
					&& player.getPosition().x <= newPosition.x) {
				player.getPosition().x = newPosition.x;
			} else if (newPosition.x > oldPosition.x
					&& player.getPosition().x >= newPosition.x) {
				player.getPosition().x = newPosition.x;
			}
			// If player y == newPosition y
			if (newPosition.y < oldPosition.y
					&& player.getPosition().y <= newPosition.y) {
				player.getPosition().y = newPosition.y;
			} else if (newPosition.y > oldPosition.y
					&& player.getPosition().y >= newPosition.y) {
				player.getPosition().y = newPosition.y;
			}

			// If player has reached destination tile
			if (player.getPosition().x == newPosition.x
					&& player.getPosition().y == newPosition.y) {
				player.setState(State.IDLE);
				oldPosition.set(new Vector2());
				newPosition.set(new Vector2());
			}
		}

		// simply updates the state time
		player.update(delta);
	}

	/** Door checking **/
	private boolean checkCollisionWithDoor(float posX, float posY) {

		Room room = player.getMyRoom();

		if (!room.hasDoors()) {
			return false;
		} else {
			for (Door door : room.getDoors()) {
				if (posX == door.getPosition().x
						&& posY == door.getPosition().y && door.isOpen()) {
					return true;
				}
			}
			return false;
		}

	}

	private Door getCollidingDoor(float posX, float posY) {

		Room room = player.getMyRoom();

		for (Door door : room.getDoors()) {
			if (posX == door.getPosition().x && posY == door.getPosition().y
					&& door.isOpen()) {
				return door;
			}
		}
		return null;
	}

	/** Collision checking **/
	private boolean checkCollisionWithTile(Vector2 tilePosition) {

		Room room = player.getMyRoom();

		// Convert screen to map coordinates
		float mapX = room.convertToMapCoordinates(tilePosition).x;
		float mapY = room.convertToMapCoordinates(tilePosition).y;

		if (mapX >= room.getWidth() || mapX < 0) {
			// System.out.println("Reached edge of map!");
			return true;
		}

		if (mapY >= room.getHeight() || mapY < 0) {
			// System.out.println("Reached edge of map!");
			return true;
		}

		if (room.checkCollision((int) mapX, (int) mapY)) {
			// System.out.println("Collision at tile " + mapX + ", " + mapY);
			return true;
		}

		return false;
	}

	private void updateRandomMovement(float delta) {

		if (player.getState().equals(State.IDLE)) {

			leftUpReleased();
			leftDownReleased();
			rightUpReleased();
			rightDownReleased();

			Door targetDoor = null;

			// If door is open, use directed walking towards door
			if (player.getMyRoom().hasDoors()) {
				for (Door door : player.getMyRoom().getDoors()) {
					// If open door is found
					if (door.isOpen()) {
						// If door was not recently used
						if (lastDoorUsed == null) {
							targetDoor = door;
							directedMovement = true;
							break;
						} else {
							// If door is not the one that was recently used
							if (!lastDoorUsed.equals(door.getTargetDoor())) {
								targetDoor = door;
								directedMovement = true;
								break;
							} else {
								directedMovement = false;
							}
						}
					}
				}
			} else {
				directedMovement = false;
			}

			if (directedMovement) {

				Vector2 playerTile = player.getMyRoom()
						.convertToMapCoordinates(player.getPosition());
				Vector2 doorTile = player.getMyRoom().convertToMapCoordinates(
						targetDoor.getPosition());

				boolean changedX = false;
				boolean changedY = false;

				if (changeFirst) {
					if (playerTile.x < doorTile.x) {
						// If next tile is door or tile does not collide
						if (checkCollisionWithDoor(player.getPosition().x + 0.5f,
								player.getPosition().y - 0.5f)
								|| !checkCollisionWithTile(player.getPosition()
										.cpy().add(+0.5f, -0.5f))) {
							// change the tileX
							rightDownPressed();
							changedX = true;
						}
					} else if (playerTile.x > doorTile.x) {
						// If next tile is door or tile does not collide
						if (checkCollisionWithDoor(player.getPosition().x - 0.5f,
								player.getPosition().y + 0.5f)
								|| !checkCollisionWithTile(player.getPosition()
										.cpy().add(-0.5f, +0.5f))) {
							// change the tileX
							leftUpPressed();
							changedX = true;
						}
					}
				}

				if (!changedX) {
					if (playerTile.y < doorTile.y) {
						if (checkCollisionWithDoor(
								player.getPosition().x - 0.5f,
								player.getPosition().y - 0.5f)
								|| !checkCollisionWithTile(player.getPosition()
										.cpy().add(-0.5f, -0.5f))) {
							// change the tileY
							leftDownPressed();
							changedY = true;
						}
					} else if (playerTile.y > doorTile.y) {
						if (checkCollisionWithDoor(
								player.getPosition().x + 0.5f,
								player.getPosition().y + 0.5f)
								|| !checkCollisionWithTile(player.getPosition()
										.cpy().add(0.5f, 0.5f))) {
							// change the tileY
							rightUpPressed();
							changedY = true;
						}
					}
				}
				
				// To remove deadlock
				if (!changedX && !changedY) {
					rightDownPressed();
					changeFirst = false;
				}
				
				if (changedY) {
					changeFirst = true;
				}

			} else {
				if (movementTimer >= randWaitTime) {
					movementTimer = 0f;

					if (!player.getCondition().equals(Condition.PANIC)) {
						randWaitTime = randGenerator.nextInt(4);
					} else {
						randWaitTime = 0;
					}

					pickRandomDirection();

				} else {
					movementTimer += delta;
				}
			}
		}

	}

	private void pickRandomDirection() {

		int randDirection = randGenerator.nextInt(4);

		switch (randDirection) {
		case 0:
			leftUpPressed();
			break;
		case 1:
			leftDownPressed();
			break;
		case 2:
			rightUpPressed();
			break;
		case 3:
			rightDownPressed();
			break;
		}
	}

	/** Change Bob's state and parameters based on input controls **/
	private boolean processInput() {

		if (keys.get(Keys.LEFT_DOWN)) {
			if (player.getState().equals(State.IDLE)) {
				player.setFacingLeft(true);
				player.setFacingDown(true);
				player.setState(State.WALKING);
				oldPosition = player.getPosition().cpy();
				newPosition = oldPosition.cpy().add(-0.5f, -0.5f);
			}
		} else if (keys.get(Keys.LEFT_UP)) {
			if (player.getState().equals(State.IDLE)) {
				player.setFacingLeft(true);
				player.setFacingDown(false);
				player.setState(State.WALKING);
				oldPosition = player.getPosition().cpy();
				newPosition = oldPosition.cpy().add(-0.5f, 0.5f);
			}
		} else if (keys.get(Keys.RIGHT_DOWN)) {
			if (player.getState().equals(State.IDLE)) {
				player.setFacingLeft(false);
				player.setFacingDown(true);
				player.setState(State.WALKING);
				oldPosition = player.getPosition().cpy();
				newPosition = oldPosition.cpy().add(0.5f, -0.5f);
			}
		} else if (keys.get(Keys.RIGHT_UP)) {
			if (player.getState().equals(State.IDLE)) {
				player.setFacingLeft(false);
				player.setFacingDown(false);
				player.setState(State.WALKING);
				oldPosition = player.getPosition().cpy();
				newPosition = oldPosition.cpy().add(0.5f, 0.5f);
			}
		}
		return false;
	}

	public boolean isMovingRandom() {
		return movingRandom;
	}

	public void setMovingRandom(boolean random) {
		if (!random)
			this.directedMovement = false;
		this.movingRandom = random;
	}
}
