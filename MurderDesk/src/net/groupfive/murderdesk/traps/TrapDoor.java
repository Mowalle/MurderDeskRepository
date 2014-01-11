package net.groupfive.murderdesk.traps;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

public class TrapDoor extends Trap {

	/** Trapdoor's position in pixel coordinates */
	private float x, y;
	/** Trapdoor's position in tile coordinates */
	private float tiledX, tiledY;
	/** Trapdoor's width and height in pixel coordinates */
	private float tiledWidth, tiledHeight;

	/** Trapdoor's texture */
	Texture sprite;

	// Getters -----------
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getTiledWidth() {
		return tiledWidth;
	}

	public float getTiledHeight() {
		return tiledHeight;
	}
	// --------------------
	
	/**
	 * Creates a new Trapdoor trap. 'id' has to be the same String as defined in
	 * the tiled map editor.
	 * 
	 * @param trapDoorId
	 * @param map
	 */
	public TrapDoor(String id, GameMap map) {
		this.myMap = map;
		this.id = id;
		sprite = new Texture("textures/traps/TrapDoor.png");
		tiledWidth = tiledHeight = 2;

		// If layer "Traps" exists
		if (myMap.hasLayer("Traps")) {
			// If object with name id exists
			if (myMap.hasObject("Traps", id)) {
				// if type is "TrapDoor"
				if (myMap.getMapObject("Traps", id).getProperties().get("type")
						.equals("TrapDoor")) {

					// Get trap box
					Rectangle trapBox = ((RectangleMapObject) map.getMapObject(
							"Traps", id)).getRectangle();

					// Get map coordinates of the trap box
					tiledX = map.convertScreenToMapCoordinates(
							trapBox.getX() * 2, 0).x;
					tiledY = map.convertScreenToMapCoordinates(0,
							map.getMapPixelHeight() - trapBox.getY()
									- tiledHeight * map.getTilePixelHeight()).y;

					x = map.getCornerTop().x
							+ map.convertMapToIsometricCoordinates(tiledX,
									tiledY).x - (map.getTilePixelWidth());
					y = map.getCornerTop().y
							- map.convertMapToIsometricCoordinates(tiledX,
									tiledY).y - (map.getTilePixelHeight() * 2);

					System.out.println(x + ", " + y + ", " + tiledWidth + ", "
							+ tiledHeight);

				} else {
					System.out
							.println("Trapdoor could not be created correctly: object "
									+ id
									+ " has type "
									+ myMap.getMapObject("Traps", id)
											.getProperties().get("type")
									+ " instead of \"TrapDoor\"!");
				}

			} else {
				System.out
						.println("Trapdoor could not be created correctly: id "
								+ id + " does not exist on layer \"Traps\"!");
			}

		} else {
			System.out
					.println("Trapdoor could not be created correctly: GameMap does not contain layer \"Traps\"!");
		}
	}

	// Override update method so that we can use checkCondition(player)
//	@Override
//	public void update(Player player) {
//		if (!isActive) {
//			if (checkCondition(player)) {
//				activate(player);
//			}
//		} else {
//			if (checkCondition(player)) {
//				applyTrapOverTime(player);
//			} else {
//				deactivate(player);
//			}
//		}
//	}

	@Override
	public void draw(float delta, SpriteBatch spriteBatch) {
		if (isActive)
			spriteBatch.draw(sprite, x, y);
	}

	protected boolean checkCondition(Player player) {
		// Condition for the trapdoor, which is in this case whether the player
		// stands ontop of it or not.
		if (!player.isAnimating()) {
			if (player.getTiledX() >= tiledX && player.getTiledX() < tiledX + tiledWidth
					&& player.getTiledY() >= tiledY
					&& player.getTiledY() < tiledY + tiledHeight) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected boolean checkCondition() {
		if (Gdx.input.isKeyPressed(Keys.SPACE))
			return true;
		return false;
	}

	@Override
	protected void applyTrapOverTime(Player player) {

	}

	@Override
	protected void applyTrapOnActivation(Player player) {
		// If player is close to trap, turn him and apply stat effects
		if (player.getTiledX() >= tiledX && player.getTiledX() <= tiledX + 1 && player.getTiledY() == tiledY - 1) {
			player.setCurrentDirection(Player.LEFT_DOWN);
			player.setPulse(player.getPulse() + 10);
			// TODO
		} else if (player.getTiledX() >= tiledX && player.getTiledX() <= tiledX + 1 && player.getTiledY() == tiledY + 2) {
			player.setCurrentDirection(Player.RIGHT_UP);
			player.setPulse(player.getPulse() + 10);
			// TODO
		} else if (player.getTiledX() == tiledX - 1 && player.getTiledY() >= tiledY && player.getTiledY() <= tiledY + 1) {
			player.setCurrentDirection(Player.RIGHT_DOWN);
			player.setPulse(player.getPulse() + 10);
		} else if (player.getTiledX() == tiledX + 2 && player.getTiledY() >= tiledY && player.getTiledY() <= tiledY + 1) {
			player.setCurrentDirection(Player.LEFT_UP);
			player.setPulse(player.getPulse() + 10);
			
		}
	}

	@Override
	protected void applyTrapOnDeactivation(Player player) {
		// TODO
		System.out.println(id
				+ " was deactivated: applyTrapOnDeactivation(...)");
		System.out.println("Player: " + player.getTiledX() + ", " + player.getTiledY());
	}

	public void dispose() {
		sprite.dispose();
	}

}
