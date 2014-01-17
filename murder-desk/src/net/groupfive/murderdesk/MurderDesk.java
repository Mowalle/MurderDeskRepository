package net.groupfive.murderdesk;

import net.groupfive.murderdesk.screens.GameScreen;

import com.badlogic.gdx.Game;

public class MurderDesk extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
