package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.MurderDesk;
import net.groupfive.murderdesk.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PlayScreen implements Screen {

	final MurderDesk game;

	public OrthographicCamera cam;

	private Player player;
	// Should be replaced later
	private Rectangle playerBox;

	// Map stuff
	private TiledMap map;
	private int mapWidth;
	private int mapHeight;
	private int tilePixelWidth;
	private int tilePixelWidthHalf;
	private int tilePixelHeight;
	private int tilePixelHeightHalf;
	private int mapPixelWidth;
	private int mapPixelHeight;

	private Vector2 mapTopCornerPixel;

	private IsometricTiledMapRenderer renderer;

	public PlayScreen(MurderDesk game) {
		this.game = game;
	}

	// Update logic here
	public void update(float delta) {

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.position.y += 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.position.y -= 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.position.x -= 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.position.x += 5;
		}

	}

	@Override
	public void render(float delta) {

		update(Gdx.graphics.getDeltaTime());

		// Clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		renderer.setView(cam);
		renderer.getSpriteBatch().setProjectionMatrix(cam.combined);

		int[] behind = { 0 };

		renderer.render(behind);

		game.spriteBatch.begin();

		game.spriteBatch.setProjectionMatrix(cam.combined);
		player.draw(game.spriteBatch);
		game.spriteBatch
				.draw(player.getTexture(), player.getX(), player.getY());
		// Top corner of map
		// game.spriteBatch.draw(playerTexture, 480, 240);
		game.spriteBatch.end();

		int[] above = { 1, 2 };

		renderer.render(above);

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		cam = new OrthographicCamera();
		cam.setToOrtho(false, MurderDesk.width, MurderDesk.height);
		cam.update();

		map = new TmxMapLoader().load("maps/IsoTest.tmx");
		MapProperties prop = map.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelWidthHalf = tilePixelWidth / 2;
		tilePixelHeight = prop.get("tileheight", Integer.class);
		tilePixelHeightHalf = tilePixelHeight / 2;

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		mapTopCornerPixel = new Vector2((mapWidth - 1) * tilePixelWidthHalf,
				(mapHeight - 1) * tilePixelHeightHalf);

		System.out.println("mapTopCornerPixel: " + mapTopCornerPixel);

		renderer = new IsometricTiledMapRenderer(map);

		player = new Player(new Sprite(new Texture("textures/mousemap.png")));

		if (map.getLayers().get("Objects").getObjects().get("Spawn") instanceof RectangleMapObject) {
			playerBox = ((RectangleMapObject) map.getLayers().get("Objects")
					.getObjects().get("Spawn")).getRectangle();

			System.out.println("playerBox.x: " + playerBox.x + " playerBox.y: "
					+ playerBox.y);

			float mapX = convertScreenToMapCoordinates(playerBox.getX() * 2,
					mapPixelHeight - playerBox.getY() - tilePixelHeight,
					tilePixelWidth, tilePixelHeight).x;
			float mapY = convertScreenToMapCoordinates(playerBox.getX() * 2,
					mapPixelHeight - playerBox.getY() - tilePixelHeight,
					tilePixelWidth, tilePixelHeight).y;

			System.out.println("mapX: " + mapX + " mapY: " + mapY);

			playerBox.x = mapTopCornerPixel.x
					+ convertMapToIsometricCoordinates(mapX, mapY,
							tilePixelWidth, tilePixelHeight).x;
			playerBox.y = mapTopCornerPixel.y
					- convertMapToIsometricCoordinates(mapX, mapY,
							tilePixelWidth, tilePixelHeight).y;

			player.setBoundingBox(playerBox);

		}

		// cam.position.set(player.getX() + player.width / 2, player.getY()
		// + player.height / 2, 0);

		System.out.println("Camera: " + cam.position.x + ", " + cam.position.y);
	}

	// Convert the parsed coordinates back to map coordinates
	private Vector2 convertScreenToMapCoordinates(float x, float y,
			int tileWidth, int tileHeight) {

		Vector2 result = new Vector2();

		result.x = x / tileWidth;
		result.y = y / tileHeight;

		return result;
	}

	// After http://clintbellanger.net/articles/isometric_math/
	private Vector2 convertMapToIsometricCoordinates(float mapX, float mapY,
			int tileWidth, int tileHeight) {
		Vector2 result = new Vector2();

		result.x = (mapX - mapY) * (tileWidth / 2);
		result.y = (mapX + mapY) * (tileHeight / 2);

		return result;
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

}
