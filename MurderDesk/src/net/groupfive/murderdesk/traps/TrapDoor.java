package net.groupfive.murderdesk.traps;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.Player;

public class TrapDoor extends Trap {

	/** Trapdoor's position in pixel coordinates */
	private float x, y;
	/** Trapdoor's position in pixel coordinates */
	private float tiledX, tiledY;
	/** Trapdoor's width and height in tile coordinates */
	private float width, height;

	/** Trapdoor's texture */
	Texture sprite;

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
		width = height = 2;

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
					tiledY = map.convertScreenToMapCoordinates(
							0,
							map.getMapPixelHeight() - trapBox.getY()
									- height * map.getTilePixelHeight()).y;

					x = map.getCornerTop().x
							+ map.convertMapToIsometricCoordinates(tiledX, tiledY).x - (map.getTilePixelWidth());
					y = map.getCornerTop().y
							- map.convertMapToIsometricCoordinates(tiledX, tiledY).y - (map.getTilePixelHeight() * 2);					

					System.out.println(x + ", " + y);
					
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
	@Override
	public void update(Player player) {
		if (!isActive) {
			if (checkCondition(player)) {
				activate(player);
			} else {
				deactivate(player);
			}
		} else {
			applyTrapOverTime(player);
		}
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(sprite, x, y);
	}

	protected boolean checkCondition(Player player) {
		// Hardcoded Values
		if (player.tileX >= x && player.tileX <= x + (width / 2)
				&& player.tileY >= y && player.tileY <= y + (height / 2)) {

		}
	}

	@Override
	protected boolean checkCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void applyTrapOverTime(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOnActivation(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOnDeactivation(Player player) {
		// TODO Auto-generated method stub

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

	public void dispose() {
		sprite.dispose();
	}


}
