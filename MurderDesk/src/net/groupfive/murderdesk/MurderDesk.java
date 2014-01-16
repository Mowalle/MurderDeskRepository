package net.groupfive.murderdesk;

import net.groupfive.murderdesk.screens.GameScreen;
import net.groupfive.murderdesk.screens.MurderDeskScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MurderDesk extends Game {

	public static final String TITLE = "The Murder Desk";
	public static final String VERSION = "v0.1";

	public static int width;
	public static int height;

	public SpriteBatch spriteBatch;
	public BitmapFont font;
	
	public static Controller controller; // allows to broadcast data to the GUI, without forcing the GUI to update all the time
	
	@Override
	public void render() {

		MurderDeskScreen currentScreen = getScreen();

		// update the screen
		currentScreen.render(Gdx.graphics.getDeltaTime());

		// When the screen is done we change to the
		// next screen. Ideally the screen transitions are handled
		// in the screen itself or in a proper state machine.
		if (currentScreen.isDone()) {

			// dispose the resources of the current screen
			currentScreen.dispose();

			// // if the current screen is a main menu screen we switch to
			// // the game loop
			// if (currentScreen instanceof MainMenu) {
			// setScreen(new GameLoop());
			// } else {
			// // if the current screen is a game loop screen we switch to the
			// // game over screen
			// if (currentScreen instanceof GameLoop) {
			// setScreen(new GameOver());
			// } else if (currentScreen instanceof GameOver) {
			// // if the current screen is a game over screen we switch to the
			// // main menu screen
			// setScreen(new MainMenu());
			// }
			// }
			this.dispose(); 
		}

		// logger.log();

	}

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();
		font = new BitmapFont();

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		setScreen(new GameScreen(this));

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		font.dispose();
	}

	/**
	 * For this game each of our screens is an instance of MurderDeskScreen.
	 * 
	 * @return the currently active {@link MurderDeskScreen}.
	 */
	@Override
	public MurderDeskScreen getScreen() {
		return (MurderDeskScreen) super.getScreen();
	}
	
	/**
	 * 
	 * @param c The controller
	 */
	public void setController(Controller c){
		this.controller = c;
	}
	
	/**
	 * 
	 * @return The controller object
	 */
	public Controller getController(){
		return controller;
	}
}
