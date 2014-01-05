package net.groupfive.murderdesk;

import net.groupfive.murderdesk.screens.PlayScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MurderDesk extends Game {

	public static final String TITLE = "The Murder Desk";
	public static final String VERSION = "v0.0.2";

	public static int width;
	public static int height;

	public SpriteBatch spriteBatch;
	public BitmapFont font;

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();
		font = new BitmapFont();

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		Texture.setEnforcePotImages(false);

		this.setScreen(new PlayScreen(this));
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		font.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

}
