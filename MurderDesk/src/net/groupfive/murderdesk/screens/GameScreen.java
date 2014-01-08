package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.MurderDesk;
import net.groupfive.murderdesk.Player;
import net.groupfive.murderdesk.ui.GameHUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen extends MurderDeskScreen {

	private final MurderDesk game;

	public OrthographicCamera camera;
	public Rectangle viewport;

	private Player player;

	Texture test = new Texture("textures/mousemap.png");

	// Map stuff
	private GameMap map;
	private IsometricTiledMapRenderer renderer;

	private GameHUD hud;

	/** is done flag **/
	private boolean isDone = false;

	public GameScreen(MurderDesk game) {
		this.game = game;

		camera = new OrthographicCamera();
		viewport = new Rectangle(25, 150, 640, 400);

		camera.setToOrtho(false, viewport.width, viewport.height);
		camera.position.set(0, 0, 0);

		camera.update();

		/*
		 * Map Setup
		 */
		map = new GameMap("maps/IsoTest.tmx");
		System.out.println("Map: " + map.getMapPixelWidth() + ", "
				+ map.getMapPixelHeight());
		camera.position.set(map.getBoundingBox().width / 2,
				map.getBoundingBox().height / 2, 0);

		renderer = new IsometricTiledMapRenderer(map.getTiledMap());

		hud = new GameHUD();

		/*
		 * Player Object Setup
		 */
		player = new Player();

		player.spawn(1, 1, map);

		System.out.println("Player: " + player.getX() + ", " + player.getY());

		/*
		 * Test Messages
		 */

		System.out.println("Camera: " + camera.position.x + ", "
				+ camera.position.y + ", " + camera.position.z);
	}

	@Override
	public void dispose() {
		map.getTiledMap().dispose();
		renderer.dispose();
		player.dispose();
	}

	@Override
	public void update(float delta) {

		// Comment in to log fps in console.
		// logger.log();

		/*
		 * Update logic here
		 */
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.position.y += 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.position.y -= 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.position.x -= 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.position.x += 5;
		}

		// // Camera Bounds
		// if (map.getMapPixelWidth() > viewport.width) {
		//
		// // Left
		// if (camera.position.x < viewport.width / 2) {
		// camera.position.x = viewport.width / 2;
		// }
		//
		// // // Right
		// // if (cam.position.x > (Gdx.graphics.getWidth() / 2) +
		// // map.getMapPixelWidth() - cam.viewportWidth) {
		// // cam.position.x = (Gdx.graphics.getWidth() / 2) +
		// // map.getMapPixelWidth() - cam.viewportWidth;
		// // }
		// }

		// // Bottom
		// if (camera.position.y < 0) {
		// camera.position.y = 0;
		// }

		//
		// // Top
		// if (camera.position.y > 16) {
		// camera.position.y = 16;
		// }

		// System.out.println("Camera: " + camera.position.x + ", "
		// + camera.position.y + ", " + camera.position.z);

		// Update the player object
		player.update(delta, map);

	}

	@Override
	public void draw(float delta) {

		/*
		 * Rendering stuff here
		 */

		// Clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
				(int) viewport.width, (int) viewport.height);

		camera.update();

		game.spriteBatch.begin();
		game.spriteBatch.draw(test, map.getBoundingBox().getX(), map
				.getBoundingBox().getY(), map.getBoundingBox().getWidth(), map
				.getBoundingBox().getHeight());
		game.spriteBatch.end();
		
		renderer.setView(camera);
		renderer.getSpriteBatch().setProjectionMatrix(camera.combined);

		// Render layers below the player (NOT "WalkBehind")
		renderer.render(map.getBelowLayers());

		game.spriteBatch.begin();
		game.spriteBatch.setProjectionMatrix(camera.combined);

		player.draw(game.spriteBatch);

		game.spriteBatch.end();

		// Render layers above the player ("WalkBehind")
		renderer.render(map.getAboveLayers());

		Gdx.gl.glViewport((int) 0, (int) 0, (int) MurderDesk.width,
				(int) MurderDesk.height);
		hud.draw(delta);

	}

	@Override
	public boolean isDone() {
		return isDone;
	}

}
