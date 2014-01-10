package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.MurderDesk;
import net.groupfive.murderdesk.Player;
import net.groupfive.murderdesk.JujiPlayer; //Only for testing purposes
import net.groupfive.murderdesk.traps.TrapDoor;
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

	private JujiPlayer player;

	Texture test = new Texture("textures/mousemap.png"); //Only for testing purposes

	// Map stuff
	private GameMap map;
	private IsometricTiledMapRenderer renderer;
	
	// Traps
	TrapDoor testTrap;

	private GameHUD hud;

	/** is done flag **/
	private boolean isDone = false;


	public GameScreen(MurderDesk game) {
		this.game = game;

		camera = new OrthographicCamera();
		viewport = new Rectangle(25, 150, 640, 400); // Hardcoded Values

		camera.setToOrtho(false, viewport.width, viewport.height);
		camera.position.set(0, 0, 0);

		camera.update();

		/*
		 * Map Setup
		 */
		map = new GameMap("maps/test1.tmx");
		System.out.println("Map BoundingBox: " + map.getBoundingBox().x + ", " + map.getBoundingBox().y + ", " + map.getBoundingBox().width + ", "
				+ map.getBoundingBox().height);
		
		testTrap = new TrapDoor("trapdoor01", map);
	
		camera.position.set(map.getMapPixelWidth() / 2, map.getMapPixelHeight() / 2 + map.getBoundingBox().y, 0);
		
		renderer = new IsometricTiledMapRenderer(map.getTiledMap());

		hud = new GameHUD();

		/*
		 * Player Object Setup
		 */
		player = new JujiPlayer();

		player.spawn(map);

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
		testTrap.dispose();
		renderer.dispose();
		player.dispose();
	}

	@Override
	public void update(float delta) {

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

		// Camera Bounds
		

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

//		game.spriteBatch.begin();
//		game.spriteBatch.draw(test, map.getBoundingBox().getX(), map
//				.getBoundingBox().getY(), map.getBoundingBox().getWidth(), map
//				.getBoundingBox().getHeight());
//		game.spriteBatch.end();
		
		renderer.setView(camera);
		renderer.getSpriteBatch().setProjectionMatrix(camera.combined);
		
		// Render layers below the player (NOT "WalkBehind")
		renderer.render(map.getBelowLayers());

		game.spriteBatch.begin();
		game.spriteBatch.setProjectionMatrix(camera.combined);
		
		testTrap.draw(game.spriteBatch);
		
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
