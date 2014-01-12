package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.MurderDesk;
import net.groupfive.murderdesk.Player;
import net.groupfive.murderdesk.traps.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends MurderDeskScreen {

	private final MurderDesk game;

	public OrthographicCamera camera;
	public Rectangle viewport;

	private Player player;

	// TODO Remove this
	Texture test = new Texture("textures/mousemap.png"); // Only for testing
														 // purposes

	// Map stuff
	private Array<GameMap> maps;
	private GameMap currentMap;
	private IsometricTiledMapRenderer renderer;

	/** is done flag **/
	private boolean done = false;

	private int highscore = 0;
	private float highscoreTimer = 0f;

	public GameScreen(MurderDesk game) {
		this.game = game;

		camera = new OrthographicCamera();
		viewport = new Rectangle(25, 150, 640, 400); // Hardcoded Values

		camera.setToOrtho(false, viewport.width, viewport.height);
		camera.position.set(0, 0, 0);

		camera.update();

		System.out.println("Camera: " + camera.position.x + ", "
				+ camera.position.y + ", " + camera.position.z);

		/*
		 * Map Setup
		 */
		maps = new Array<GameMap>();
		maps.add(new GameMap("maps/test1.tmx"));
		maps.add(new GameMap("maps/IsoTest.tmx"));

		// FIXME Has to be compatible to multiple maps
		setCurrentMap(0);

		/*
		 * Player Object Setup
		 */
		player = new Player();

		// TODO Check for invalid integers
		player.spawn(5, 6, currentMap);

		System.out.println("Player: " + player.getX() + ", " + player.getY());

		// TODO Has to be moved into GameMap.java
		currentMap.getTraps().add(new TrapDoor("trapdoor01", currentMap));
		currentMap.getTraps().add(new TrapFlood(currentMap, player));
		currentMap.getTraps().add(new TrapGas(currentMap, player));
		currentMap.setCurrentTrap(0);
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

		if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
			if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
				currentMap.setCurrentTrap(0);
			} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
				currentMap.setCurrentTrap(1);
			} else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
				currentMap.setCurrentTrap(2);
			}
		}

		// Trap Update
		currentMap.getCurrentTrap().update(delta, player);

		// Update the player object
		player.update(delta, currentMap);

		// Updates the highscore each second by the difference between the
		// player's current pulse and the player's default pulse.
		highscoreTimer += delta;

		if (highscoreTimer >= 10) {
			highscoreTimer = 0f;
			highscore += Math.abs(player.getPulse() - Player.DEFAULT_PULSE);
		}
		
//		//FIXME Doesn't end app but crashes for unknown reason
//		if (player.isDead()) {
//			done = true;
//		}

	}

	@Override
	public void draw(float delta) {

		/*
		 * Rendering stuff here
		 */

		// Clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
		// (int) viewport.width, (int) viewport.height);

		camera.update();

		// game.spriteBatch.begin();
		// game.spriteBatch.draw(test, currentMap.getBoundingBox().getX(),
		// currentMap
		// .getBoundingBox().getY(), currentMap.getBoundingBox().getWidth(),
		// currentMap
		// .getBoundingBox().getHeight());
		// game.spriteBatch.end();

		renderer.setView(camera);
		renderer.getSpriteBatch().setProjectionMatrix(camera.combined);

		// Render layers below the player (NOT "WalkBehind")
		renderer.render(currentMap.getBelowLayers());

		game.spriteBatch.begin();
		game.spriteBatch.setProjectionMatrix(camera.combined);

		player.draw(false, game.spriteBatch);

		currentMap.getCurrentTrap().draw(delta, game.spriteBatch);

		player.draw(true, game.spriteBatch);

		game.spriteBatch.end();

		// Render layers above the player ("WalkBehind")
		renderer.render(currentMap.getAboveLayers());

		// Gdx.gl.glViewport((int) 0, (int) 0, (int) MurderDesk.width,
		// (int) MurderDesk.height);
	}

	public void setCurrentMap(int mapIndex) {

		currentMap = maps.get(mapIndex);

		System.out.println("Map BoundingBox: " + currentMap.getBoundingBox().x
				+ ", " + currentMap.getBoundingBox().y + ", "
				+ currentMap.getBoundingBox().width + ", "
				+ currentMap.getBoundingBox().height);

		camera.position.set(
				currentMap.getMapPixelWidth() / 2,
				currentMap.getMapPixelHeight() / 2
						+ currentMap.getBoundingBox().y, 0);

		renderer = new IsometricTiledMapRenderer(currentMap.getTiledMap());

	}
	
	public GameMap getCurrentMap(){
		return currentMap;
	}

	@Override
	public void dispose() {
		currentMap.getTiledMap().dispose();
		currentMap.getCurrentTrap().dispose();
		player.dispose();
	}

	@Override
	public boolean isDone() {
		return done;
	}
	
	public Player getPlayer(){
		return player;
	}

	/**
	 * @return the highscore
	 */
	public int getHighscore() {
		return highscore;
	}

}
