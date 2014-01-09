package net.groupfive.murderdesk.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHUD {

	SpriteBatch spriteBatch;
	BitmapFont font;
	Texture background;

	public GameHUD() {
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/Minecraftia.fnt"),
				Gdx.files.internal("fonts/Minecraftia.png"), false);
		
		background = new Texture("textures/uiTest.png");
	}

	public void draw(float delta) {

		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0);
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 35,
				70);
		spriteBatch.end();

	}

	public void dispose() {
		background.dispose();
	}

}
