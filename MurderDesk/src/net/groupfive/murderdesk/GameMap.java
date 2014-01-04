package net.groupfive.murderdesk;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

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

	public TiledMap getTiledMap() {
		return map;
	}

	public void setTiledMap(TiledMap map) {
		this.map = map;

		MapProperties prop = map.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		topCorner = new Vector2((mapWidth - 1) * (tilePixelWidth / 2),
				(mapHeight - 1) * (tilePixelHeight / 2));
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
}
