package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.MurderDesk;
import net.groupfive.murderdesk.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PlayScreen implements Screen {

	final MurderDesk game;

	public OrthographicCamera cam;

	private Player player;
	// Should be replaced later
	private Rectangle playerBox;

	// Map stuff
	private GameMap map;
	private IsometricTiledMapRenderer renderer;

	public FPSLogger logger;

	public PlayScreen(MurderDesk game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {

//		 Comment in to log fps in console.
//		 logger.log();

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
		
		// Camera Bounds
		// Left
		if (cam.position.x < Gdx.graphics.getWidth() / 2) {
			cam.position.x = Gdx.graphics.getWidth() / 2;
		}
		
		// Bottom
		if (cam.position.y < 0) {
			cam.position.y = 0;
		}
		
		// Right
		if (cam.position.x > (Gdx.graphics.getWidth() / 2) + map.getMapPixelWidth() - cam.viewportWidth) {
			cam.position.x = (Gdx.graphics.getWidth() / 2) + map.getMapPixelWidth() - cam.viewportWidth;
		}
		
		// Top
		if (cam.position.y > map.getMapPixelHeight() - cam.viewportHeight) {
			cam.position.y = map.getMapPixelHeight() - cam.viewportHeight;
		}

		// Update the player object
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

		// Render layers below the player (NOT "WalkBehind")
		renderer.render(map.getBelowLayers());

		// SpriteBatch Begin
		game.spriteBatch.begin();

		game.spriteBatch.setProjectionMatrix(cam.combined);
		player.draw(game.spriteBatch);

		// SpriteBath End
		game.spriteBatch.end();

		// Render layers above the player ("WalkBehind")
		renderer.render(map.getAboveLayers());

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		logger = new FPSLogger();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, MurderDesk.width, MurderDesk.height);
		cam.position.set(0, 0, 0);
		
		cam.update();

		/*
		 * Map Setup
		 */
		map = new GameMap("maps/IsoTest.tmx");

		renderer = new IsometricTiledMapRenderer(map.getTiledMap());
		
		/*
		 * Player Object Setup
		 */
		player = new Player();

		// If map contains object "Spawn", get a rectangle from the bounding
		// box.
		if (map.getMapObject("Objects", "Spawn") instanceof RectangleMapObject) {
			playerBox = ((RectangleMapObject) map.getMapObject("Objects",
					"Spawn")).getRectangle();

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

			player.tileX = (int) mapX;
			player.tileY = (int) mapY;

			// Get isometic coordinates of the player box
			// Might be moved to GameMap.java later
			playerBox.setX(map.getTopCorner().x
					+ map.convertMapToIsometricCoordinates(mapX, mapY).x);
			playerBox.setY(map.getTopCorner().y
					- map.convertMapToIsometricCoordinates(mapX, mapY).y);

			player.setBoundingBox(playerBox);
			
			/*
			 * Test Messages
			 */

			System.out.println("Player Data:");

			System.out.println("Position: " + player.getX() + ", "
					+ player.getY());
			System.out.println("Dimensions: " + player.getWidth() + ", "
					+ player.getHeight());

		}

		System.out.println("Camera: " + cam.position.x + ", " + cam.position.y + ", " + cam.position.z);
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
		player.dispose();
	}

}
