package net.groupfive.murderdesk.traps;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TrapFlood extends Trap {

	private int waterLevel = 0;
	private int renderX;
	private int renderY;

	private float waterLevelTimer = 0f;

	private TextureRegion[] frames;
	private TextureRegion currentFrame;

	private Texture spriteSheet;

	public TrapFlood(GameMap map) {
		spriteSheet = new Texture("textures/traps/TrapFlood.png");

		frames = new TextureRegion[] {
				new TextureRegion(spriteSheet, 0, 480 * 0, 960, 480),
				new TextureRegion(spriteSheet, 0, 480 * 1, 960, 480),
				new TextureRegion(spriteSheet, 0, 480 * 2, 960, 480),
				new TextureRegion(spriteSheet, 0, 480 * 3, 960, 480),
				new TextureRegion(spriteSheet, 0, 480 * 4, 960, 480) };

		currentFrame = frames[0];

		renderX = (int) map.getBoundingBox().x;
		renderY = (int) map.getBoundingBox().y;
	}

	public void draw(float delta, Player player, SpriteBatch spriteBatch) {

		currentFrame = frames[waterLevel];
		player.setSplitSpriteY(64 - (waterLevel * 16));

		spriteBatch.draw(currentFrame, renderX, renderY);

		if (waterLevelTimer >= 5f && waterLevel < 4) {
			waterLevelTimer = 0f;
			waterLevel++;
		}

		waterLevelTimer += delta;

	}

	@Override
	protected boolean checkCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void applyTrapOverTime(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOnActivation(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOnDeactivation(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(float delta, SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub

	}

}
