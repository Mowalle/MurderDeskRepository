package net.groupfive.murderdesk.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraController {

	enum Keys {
		UP, DOWN, LEFT, RIGHT
	}

	private static final float CAMERA_SPEED = 5f;

	private OrthographicCamera cam;

	static Map<Keys, Boolean> keys = new HashMap<CameraController.Keys, Boolean>();
	static {
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
	};

	public CameraController(OrthographicCamera cam) {
		this.cam = cam;
	}

	// ** Key presses and touches **************** //

	public void upPressed() {
		keys.get(keys.put(Keys.UP, true));
	}

	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}

	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}

	public void downReleased() {
		keys.get(keys.put(Keys.DOWN, false));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	/** The main update method **/
	public void update(float delta) {
		// Processing the input - setting position of cam
		processInput();
		cam.update();
	}

	private boolean processInput() {
		if (keys.get(Keys.UP))
			cam.position.y += CAMERA_SPEED * 2f * Gdx.graphics.getDeltaTime();
		if (keys.get(Keys.DOWN))
			cam.position.y -= CAMERA_SPEED * 2f * Gdx.graphics.getDeltaTime();
		if (keys.get(Keys.LEFT))
			cam.position.x -= CAMERA_SPEED * Gdx.graphics.getDeltaTime();
		if (keys.get(Keys.RIGHT))
			cam.position.x += CAMERA_SPEED * Gdx.graphics.getDeltaTime();
		return false;
	}
}
