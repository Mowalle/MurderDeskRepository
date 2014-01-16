package net.groupfive.murderdesk.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

import net.goupfive.murderdesk.model.Player;
import net.goupfive.murderdesk.model.Room;
import net.goupfive.murderdesk.model.World;
import net.goupfive.murderdesk.model.Player.State;

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
		// Processing the input - setting the states of Player
		processInput();

		// TODO Collision handling
		if (player.getState().equals(State.WALKING)) {
			if (checkCollisionWithTile(newPosition.x, newPosition.y)) {
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

	/** Collision checking **/
	private boolean checkCollisionWithTile(float tileX, float tileY) {

		Room room = player.getMyRoom();

		// Convert screen to map coordinates
		float mapX = tileX - tileY;
		float mapY = -tileX - tileY + (room.getHeight() - 1);

		if (mapX >= room.getWidth() || mapX < 0) {
			System.out.println("Reached edge of map!");
			return true;
		}

		if (mapY >= room.getHeight() || mapY < 0) {
			System.out.println("Reached edge of map!");
			return true;
		}

		if (room.checkCollision((int)mapX, (int)mapY)) {
			System.out.println("Collision at tile " + mapX + ", " + mapY);
			return true;
		}

		return false;
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
}
