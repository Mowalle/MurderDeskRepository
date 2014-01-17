package net.goupfive.murderdesk.model;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Room {

	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT = 32;

	/** Name of the room. **/
	private String name;

	/** Underlying tiled map. **/
	private TiledMap map;
	/** Width of room in tiles. **/
	private int width;
	/** Height of room in tiles. **/
	private int height;

	/** Array containing the room's doors. **/
	private Array<Door> doors = new Array<Door>();

	/** Array containing the room#s traps. */
	private Array<Trap> traps = new Array<Trap>();
	/** Currently activated door. **/
	private Trap currentTrap;

	public Room(String filepath) {

		loadTiledMap(filepath);

		String[] tmp = filepath.split("/");
		this.name = tmp[tmp.length - 1].substring(0,
				tmp[tmp.length - 1].length() - 4);
	}

	public Room(String filepath, String name) {
		this.name = name;
		loadTiledMap(filepath);
	}

	private void loadTiledMap(String filepath) {

		this.map = new TmxMapLoader().load(filepath);

		MapProperties prop = map.getProperties();
		width = prop.get("width", Integer.class);
		height = prop.get("height", Integer.class);
		int tileWidth = prop.get("tilewidth", Integer.class);
		int tileHeight = prop.get("tileheight", Integer.class);

		// Print Error if map is incompatible
		if (tileWidth != TILE_WIDTH || tileHeight != TILE_HEIGHT) {
			try {
				throw new Exception(
						"Error loading map "
								+ this.name
								+ ": tilewidth is not 64 pixel or tileheight is not 32 pixel!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Map " + this.name + " (" + filepath
					+ ") was loaded successfully!");
		}
	}

	public boolean checkCollision(int tileX, int tileY) {

		// Look at all layers
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			// if layer is called "Collision"
			if (map.getLayers().get(i).getName().equalsIgnoreCase("Collision")) {
				// if tile x,y contains something else than nothing then
				// collision
				if (getTileId(i, tileX, tileY) != -1) {
					return true;
				}
			}
		}

		return false;
	}

	public int getTileId(int layerIndex, int tileX, int tileY) {

		TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(
				layerIndex);

		// The "tileLayer.getHeight() - 1 - y" is to invert the cells, because
		// they are read out mirrored for some reason.
		TiledMapTileLayer.Cell cell = tileLayer.getCell(tileX,
				tileLayer.getHeight() - 1 - tileY);

		if (cell == null) {
			return -1;
		} else if (cell.getTile() == null) {
			return -1;
		} else {
			return cell.getTile().getId();
		}
	}

	/**
	 * Converts a Vector2 from isometric map coordinates to screen coordinates.
	 * 
	 * @param v
	 *            - Contains the isometric map coordinates.
	 * @return The screen coordinates.
	 */
	public Vector2 convertToScreenCoordinates(Vector2 v) {
		Vector2 result = new Vector2();

		result.x = (height - 1) / 2f - ((v.y - v.x) / 2f);
		result.y = (height - 1) / 2f - ((v.y + v.x) / 2f);

		return result;
	}
	
	public Vector2 convertToMapCoordinates(Vector2 v) {
		Vector2 result = new Vector2();
		
		result.x = v.x - v.y;
		result.y = -v.x - v.y + (this.height - 1);
		
		return result;
	}
	
	public String getName() {
		return name;
	}

	public TiledMap getTiledMap() {
		return map;
	}

	/**
	 * @return the mapWidth
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the mapHeight
	 */
	public int getHeight() {
		return height;
	}

	public Array<Door> getDoors() {
		return doors;
	}

	public void addDoor(Door door) {
		if (doors.size < 2) {
			doors.add(door);
		}
	}
	
	public boolean hasDoors() {
		if (doors.size > 0)
			return true;
		return false;
	}

	public Array<Trap> getTraps() {
		return traps;
	}

	public void addTrap(Trap trap) {
		if (traps.size < 3) {
			traps.add(trap);
			// Avoids nullPointer
			if (traps.size == 1) {
				currentTrap = traps.get(0);
			}
		}
	}
	
	public boolean hasTraps() {
		if (traps.size > 0)
			return true;
		return false;
	}

	public Trap getCurrentTrap() {
		if (traps.size == 0) {
			System.out.println("Cannot return currentTrap: No traps defined in room " + this.name);
			return null;
		}
		return currentTrap;
	}

	public void setCurrentTrap(int index) {

		if (traps.size == 0) {
			System.out.println("Cannot set currentTrap: No traps defined in room " + this.name);
			return;
		}
		
		if (currentTrap.isActive()) {
			System.out
					.println("Cannot switch traps: Current trap is still active!");
			return;
		}
		if (index < 0)
			return;

		if (index < traps.size) {
			currentTrap = traps.get(index);
		} else {
			currentTrap = traps.get(traps.size - 1);
		}
	}

	public Rectangle getBoundingBox() {
		Rectangle boundingBox = new Rectangle();
		
		Vector2 pointLeft = new Vector2 (0, 0.5f);
		Vector2 pointTop = new Vector2(0.5f * height, (height + 1) * 0.5f);
		Vector2 pointBottom = new Vector2(0.5f * width, (width - 1) * 0.5f * -1);
		Vector2 pointRight = new Vector2((width + height) * 0.5f, height * 0.5f * -1);
		
		Vector2 topLeft = new Vector2(pointLeft.x, pointTop.y);
		Vector2 bottomLeft = new Vector2(pointLeft.x, pointBottom.y);
		Vector2 bottomRight = new Vector2(pointRight.x, pointBottom.y);
		
		boundingBox.x = bottomLeft.x;
		boundingBox.y = bottomLeft.y;
		boundingBox.width = bottomRight.x - bottomLeft.x;
		boundingBox.height = topLeft.y - bottomLeft.y;
		
		return boundingBox;
	}
	
	/**
	 * Two rooms are considered equal when their name is the same.
	 * 
	 * @param r
	 *            - room to compare with.
	 * @return
	 */
	public boolean equals(Room r) {
		return this.name.equals(r.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Room))
			return false;
		Room r = (Room) o;
		return this.name.equals(r.getName());
	}
}
