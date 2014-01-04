package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.*;

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
	private GameMap map;
	private IsometricTiledMapRenderer renderer;

	public PlayScreen(MurderDesk game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {

		/*
		 * Update logic here
		 */
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
		
		player.update(delta, map);

		
		/*
		 * Rendering stuff here
		 */
		
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

		map = new GameMap("maps/IsoTest.tmx");

		System.out.println("mapTopCornerPixel: " + map.getTopCorner());

		renderer = new IsometricTiledMapRenderer(map.getTiledMap());

		player = new Player(new Sprite(new Texture("textures/mousemap.png")));

		// If map contains object "Spawn", get a rectangle from the bounding
		// box.
		if (map.getMapObject("Objects", "Spawn") instanceof RectangleMapObject) {
			playerBox = ((RectangleMapObject) map.getMapObject("Objects",
					"Spawn")).getRectangle();

			System.out.println("playerBox.x: " + playerBox.x + " playerBox.y: "
					+ playerBox.y);

			// Get map coordinates of the player box
			// Might be moved to GameMap.java later
			float mapX = map.convertScreenToMapCoordinates(
					playerBox.getX() * 2,
					map.getMapPixelHeight() - playerBox.getY()
							- map.getTilePixelHeight()).x;
			float mapY = map.convertScreenToMapCoordinates(
					playerBox.getX() * 2,
					map.getMapPixelHeight() - playerBox.getY()
							- map.getTilePixelHeight()).y;

			System.out.println("mapX: " + mapX + " mapY: " + mapY);

			// Get isometic coordinates of the player box
			// Might be moved to GameMap.java later
			playerBox.x = map.getTopCorner().x
					+ map.convertMapToIsometricCoordinates(mapX, mapY).x;
			playerBox.y = map.getTopCorner().y
					- map.convertMapToIsometricCoordinates(mapX, mapY).y;

			player.setBoundingBox(playerBox);

		}

		System.out.println("Camera: " + cam.position.x + ", " + cam.position.y);
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
		map.getTiledMap().dispose();
		renderer.dispose();
	}

}
