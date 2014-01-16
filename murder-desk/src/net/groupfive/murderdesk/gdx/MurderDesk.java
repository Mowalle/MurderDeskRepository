package net.groupfive.murderdesk.gdx;

import net.groupfive.murderdesk.gdx.screens.GameScreen;

import com.badlogic.gdx.Game;

public class MurderDesk extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
