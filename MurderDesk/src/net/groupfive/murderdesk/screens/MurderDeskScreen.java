package net.groupfive.murderdesk.screens;

import com.badlogic.gdx.Screen;

/**
 * Common class for a game screen, e.g. main menu, game loop, game over screen
 * and so on.
 */
public abstract class MurderDeskScreen implements Screen {

	/**
	 * Called when the screen should update itself, e.g. update logic
	 * etc.
	 */
	public abstract void update(float delta);

	/** Called when a screen should render itself */
	public abstract void draw(float delta);

	/**
	 * Called by MurderDesk to check whether the screen is done.
	 * 
	 * @return true when the screen is done, false otherwise
	 */
	public abstract boolean isDone();

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

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

	}

}
