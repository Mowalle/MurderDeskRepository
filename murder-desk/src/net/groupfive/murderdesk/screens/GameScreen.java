package net.groupfive.murderdesk.screens;

import net.groupfive.murderdesk.Main;
import net.groupfive.murderdesk.controller.CameraController;
import net.groupfive.murderdesk.controller.PlayerController;
import net.groupfive.murderdesk.controller.RoomController;
import net.groupfive.murderdesk.model.Player;
import net.groupfive.murderdesk.model.World;
import net.groupfive.murderdesk.view.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

public class GameScreen implements Screen, InputProcessor {

	private World world;
	private WorldRenderer renderer;
	private CameraController camController;
	private PlayerController playerController;
	private RoomController roomController;

	private int width, height;

	/** The highscore. */
	private int highscore = 0;
	private float highscoreTimer = 0f;

	@Override
	public void show() {
		world = new World();
		renderer = new WorldRenderer(world, false);

		camController = new CameraController(renderer.getCamera());
		playerController = new PlayerController(world);
		playerController.setMovingRandom(true);
		roomController = new RoomController(world);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camController.update(delta);
		playerController.update(delta);
		roomController.update(delta);
		
		renderer.render();
		
		if (!world.getPlayer().getState().equals(Player.State.DEAD)) {
			highscoreTimer += delta;

			if (highscoreTimer >= 10f) {
				highscoreTimer = 0f;
				highscore += Math.abs(Player.PULSE_DEFAULT
						- world.getPlayer().getPulse());
				if(Main.gui != null){
					Main.gui.setBalance(highscore);
				}
			}
		}

	}

	@Override
	public void resize(int width, int height) {
		renderer.setSize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	public int getHighscore() {
		return highscore;
	}
	
	public void setHighscore(int highscore){
		this.highscore = highscore;
	}

	// * InputProcessor methods ***************************//

	@Override
	public boolean keyDown(int keycode) {
		// Camera controls
		if (keycode == Keys.UP)
			camController.upPressed();
		if (keycode == Keys.DOWN)
			camController.downPressed();
		if (keycode == Keys.LEFT)
			camController.leftPressed();
		if (keycode == Keys.RIGHT)
			camController.rightPressed();

		if (!playerController.isMovingRandom()) {
			// Player controls
			if (keycode == Keys.W)
				playerController.leftUpPressed();
			if (keycode == Keys.A)
				playerController.leftDownPressed();
			if (keycode == Keys.S)
				playerController.rightDownPressed();
			if (keycode == Keys.D)
				playerController.rightUpPressed();
		}

		// Room controls
		if (keycode == Keys.NUMPAD_1) 
			roomController.roomOnePressed(); // room 1
		if (keycode == Keys.NUMPAD_2)
			roomController.roomTwoPressed(); // room 2
		if (keycode == Keys.NUMPAD_3)
			roomController.roomThreePressed(); // room 3
		
		if (keycode == Keys.NUMPAD_7)
			roomController.doorOnePressed(); // door 1
		if (keycode == Keys.NUMPAD_8)
			roomController.doorNonePressed(); // door none
		if (keycode == Keys.NUMPAD_9)
			roomController.doorTwoPressed(); // door 2
		
		if (keycode == Keys.NUM_1)
			roomController.trapOnePressed(); // trap 1
		if (keycode == Keys.NUM_2)
			roomController.trapTwoPressed(); // trap 2
		if (keycode == Keys.NUM_3)
			roomController.trapThreePressed(); // trap 3
		
		if (keycode == Keys.END)
			roomController.trapFinalPressed();
		if (keycode == Keys.SPACE)
			roomController.trapTooglePressed(); // trap toggle
		if (keycode == Keys.PLUS)
			roomController.trapIncreasePressed(); // trap intensity increase
		if (keycode == Keys.MINUS)
			roomController.trapDecreasePressed(); // trap intensity decrease

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// Camera
		if (keycode == Keys.UP)
			camController.upReleased();
		if (keycode == Keys.DOWN)
			camController.downReleased();
		if (keycode == Keys.LEFT)
			camController.leftReleased();
		if (keycode == Keys.RIGHT)
			camController.rightReleased();

		// Player
		//TODO Ranodmize player movement
		if (!playerController.isMovingRandom()) {
			if (keycode == Keys.W)
				playerController.leftUpReleased();
			if (keycode == Keys.A)
				playerController.leftDownReleased();
			if (keycode == Keys.S)
				playerController.rightDownReleased();
			if (keycode == Keys.D)
				playerController.rightUpReleased();
		}

		// Room
		if (keycode == Keys.NUMPAD_1)
			roomController.roomOneReleased(); // room 1
		if (keycode == Keys.NUMPAD_2)
			roomController.roomTwoReleased(); // room 2
		if (keycode == Keys.NUMPAD_3)
			roomController.roomThreeReleased(); // room 3
		
		if (keycode == Keys.NUMPAD_7)
			roomController.doorOneReleased(); // door 1
		if (keycode == Keys.NUMPAD_8)
			roomController.doorNoneReleased(); // door none
		if (keycode == Keys.NUMPAD_9)
			roomController.doorTwoReleased(); // door 2

		if (keycode == Keys.NUM_1)
			roomController.trapOneReleased(); // trap 1
		if (keycode == Keys.NUM_2)
			roomController.trapTwoReleased(); // trap 2
		if (keycode == Keys.NUM_3)
			roomController.trapThreeReleased(); // trap 3
		
		if (keycode == Keys.END)
			roomController.trapFinalReleased();
		if (keycode == Keys.SPACE)
			roomController.trapToggleReleased(); // trrap toggle
		if (keycode == Keys.PLUS)
			roomController.trapIncreaseReleased(); // trap intensity increase
		if (keycode == Keys.MINUS)
			roomController.trapDecreaseReleased(); // trap intensity decrease

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public World getWorld(){
		return world;
	}
	
	public WorldRenderer getWorldRenderer(){
		return renderer;
	}

}
