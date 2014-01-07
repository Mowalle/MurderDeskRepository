package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.MurderDesk;
import net.groupfive.murderdesk.Player;
import net.groupfive.murderdesk.ui.GameHUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class GameScreen extends MurderDeskScreen {

	private final MurderDesk game;

	public OrthographicCamera camera;

	private Player player;

	// Map stuff
	private GameMap map;
	private IsometricTiledMapRenderer renderer;
	
	private GameHUD hud;
	
	/** is done flag **/
    private boolean isDone = false;

	public GameScreen(MurderDesk game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MurderDesk.width, MurderDesk.height);
		camera.position.set(0, 0, 0);

		camera.update();

		/*
		 * Map Setup
		 */
		map = new GameMap("maps/IsoTest.tmx");

		renderer = new IsometricTiledMapRenderer(map.getTiledMap());
		
		hud = new GameHUD();

		/*
		 * Player Object Setup
		 */
		player = new Player();

		player.spawn(5, 5, map);

		/*
		 * Test Messages
		 */

		System.out.println("Player Data:");

		System.out.println("Position: " + player.getX() + ", " + player.getY());
		System.out.println("Dimensions: " + player.getWidth() + ", "
				+ player.getHeight());

		System.out.println("Camera: " + camera.position.x + ", " + camera.position.y
				+ ", " + camera.position.z);
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
		// // Left
		// if (cam.position.x < Gdx.graphics.getWidth() / 2) {
		// cam.position.x = Gdx.graphics.getWidth() / 2;
		// }
		//
		// // Bottom
		// if (cam.position.y < 0) {
		// cam.position.y = 0;
		// }
		//
		// // Right
		// if (cam.position.x > (Gdx.graphics.getWidth() / 2) +
		// map.getMapPixelWidth() - cam.viewportWidth) {
		// cam.position.x = (Gdx.graphics.getWidth() / 2) +
		// map.getMapPixelWidth() - cam.viewportWidth;
		// }
		//
		// // Top
		// if (cam.position.y > map.getMapPixelHeight() - cam.viewportHeight) {
		// cam.position.y = map.getMapPixelHeight() - cam.viewportHeight;
		// }

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

		camera.update();

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
		
		hud.draw(delta);
		
	}

	@Override
	public boolean isDone() {	
		return isDone;
	}

}
