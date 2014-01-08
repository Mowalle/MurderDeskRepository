package net.groupfive.murderdesk;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameMap {

	/** Underlying tiled map. */
	private TiledMap map;

	/** Width of underlying tiled map in tiles. */
	private int mapWidth;

	/** Height of underlying tiled map in tiles. */
	private int mapHeight;

	/** Width of underlying tiled map tiles in pixel. */
	private int tilePixelWidth;

	/** Height of underlying tiled map tiles in tiles. */
	private int tilePixelHeight;

	/** Width of underlying tiled map in pixel. */
	private int mapPixelWidth;

	/** Height of underlying tiled map in pixel. */
	private int mapPixelHeight;

	/** Pixel coordinates of map's top corner. */
	private Vector2 topCorner;

	/**
	 * Array containing indices of the layers that are rendered above the
	 * player.
	 */
	private int[] aboveLayers;

	/**
	 * Array containing indices of the layers that are rendered below the
	 * player.
	 */
	private int[] belowLayers;

	public GameMap(String fileName) {
		this(new TmxMapLoader().load(fileName));
	}

	public GameMap(TiledMap map) {
		setTiledMap(map);
	}

	/**
	 * Returns map object "objectName" on layer "layer".
	 */
	public MapObject getMapObject(String layer, String objectName) {
		return map.getLayers().get(layer).getObjects().get(objectName);
	}

	/**
	 * Converts screen coordinates to tile coordinates of this map.
	 */
	public Vector2 convertScreenToMapCoordinates(float x, float y) {

		Vector2 result = new Vector2();

		result.x = x / tilePixelWidth;
		result.y = y / tilePixelHeight;

		return result;
	}

	/**
	 * After http://clintbellanger.net/articles/isometric_math/ Converts map
	 * coordinates to the corresponding isometric coordinates.
	 */
	public Vector2 convertMapToIsometricCoordinates(float mapX, float mapY) {
		Vector2 result = new Vector2();

		result.x = (mapX - mapY) * (tilePixelWidth / 2);
		result.y = (mapX + mapY) * (tilePixelHeight / 2);

		return result;
	}

	public boolean hasLayer(String layerName) {
		boolean found = false;

		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i).getName().equals(layerName)) {
				found = true;
				break;
			}
		}
		return found;
	}

	public boolean hasSpawn() {
		boolean found = false;

		if (hasLayer("Objects")) {
			for (int i = 0; i < map.getLayers().get("Objects").getObjects()
					.getCount(); i++) {
				if (map.getLayers().get("Objects").getObjects().get(i)
						.getName().equalsIgnoreCase("Spawn")) {
					found = true;
					break;
				}
			}
		}

		return found;
	}

	public TiledMap getTiledMap() {
		return map;
	}

	/**
	 * Set the underlying TileMap of the GameMap.
	 * 
	 * @param map
	 */
	public void setTiledMap(TiledMap map) {
		// set up new map
		this.map = map;

		MapProperties prop = map.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		topCorner = new Vector2(mapHeight * (tilePixelWidth / 2),
				(mapHeight + 1) * (tilePixelHeight / 2));
		System.out.println(topCorner);
		calculateLayerDepth();

	}

	/**
	 * Calculates which layers are rendered above the player (called
	 * "WalkBehind") and which are not.
	 */
	private void calculateLayerDepth() {
		Array<Integer> above = new Array<Integer>();
		Array<Integer> below = new Array<Integer>();

		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i).getName().equalsIgnoreCase("WalkBehind")) {
				above.add(i);
			} else {
				below.add(i);
			}
		}

		aboveLayers = new int[above.size];
		belowLayers = new int[below.size];

		for (int i = 0; i < above.size; i++) {
			aboveLayers[i] = above.get(i);
		}

		for (int i = 0; i < below.size; i++) {
			belowLayers[i] = below.get(i);
		}

	}

	/**
	 * Returns the indices of the layers that should be rendered above the
	 * player (called "WalkBehind").
	 * 
	 * @return
	 */
	public int[] getAboveLayers() {
		return aboveLayers;
	}

	/**
	 * Returns the indices of the layers that should be rendered below the
	 * player (NOT called "WalkBehind").
	 * 
	 * @return
	 */
	public int[] getBelowLayers() {
		return belowLayers;
	}

	public Rectangle getBoundingBox() {
		Rectangle boundingBox = new Rectangle();

		Vector2 topLeft;
		Vector2 bottomLeft;
		Vector2 bottomRight;
		Vector2 topRight;

		topLeft = new Vector2(0, (mapHeight + 1)* (tilePixelHeight / 2));
		bottomLeft = new Vector2(0, (mapWidth - 1) * (tilePixelHeight / 2) * -1);
		
		bottomRight = new Vector2 ((mapWidth + mapHeight) * (tilePixelWidth / 2), (mapWidth - 1) * (tilePixelHeight / 2) * -1);
		topRight = new Vector2((mapWidth + mapHeight) * (tilePixelWidth / 2), (mapHeight + 1)* (tilePixelHeight / 2));

		boundingBox.x = bottomLeft.x;
		boundingBox.y = bottomLeft.y;
		boundingBox.width = bottomRight.x - bottomLeft.x;
		boundingBox.height = topLeft.y - bottomLeft.y;
		
		return boundingBox;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int getTilePixelWidth() {
		return tilePixelWidth;
	}

	public int getTilePixelHeight() {
		return tilePixelHeight;
	}

	public int getMapPixelWidth() {
		return mapPixelWidth;
	}

	public int getMapPixelHeight() {
		return mapPixelHeight;
	}

	public Vector2 getTopCorner() {
		return topCorner;
	}

	/**
	 * 
	 * @param layerIndex
	 * @param x
	 * @param y
	 * @return
	 */
	public int getTileId(int layerIndex, int x, int y) {

		TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(
				layerIndex);

		// The "tileLayer.getHeight() - 1 - y" is to invert the cells, because
		// they are read out mirrored for some reason.
		TiledMapTileLayer.Cell cell = tileLayer.getCell(x,
				tileLayer.getHeight() - 1 - y);

		if (cell == null) {
			return -1;
		} else if (cell.getTile() == null) {
			return -1;
		} else {
			return cell.getTile().getId();
		}

	}

	public boolean checkCollisionTile(int x, int y) {

		return checkCollisionTile("Collision", x, y);
	}

	public boolean checkCollisionTile(String collisionLayerName, int x, int y) {

		if (x >= ((TiledMapTileLayer) map.getLayers().get(collisionLayerName))
				.getWidth() || x < 0) {
			System.out.println("Reached edge of map!");
			return true;
		}

		if (y >= ((TiledMapTileLayer) map.getLayers().get(collisionLayerName))
				.getHeight() || y < 0) {
			System.out.println("Reached edge of map!");
			return true;
		}

		// Look at all layers
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			// if layer is called "Collision"
			if (map.getLayers().get(i).getName().equalsIgnoreCase("Collision")) {
				// if tile x,y contains something else than nothing then
				// collision
				if (getTileId(i, x, y) != -1) {
					System.out.println("Collision at: " + x + ", " + y);
					return true;
				}
			}
		}

		return false;
	}
}
