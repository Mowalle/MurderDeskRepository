package net.groupfive.murderdesk.traps;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TrapFlood extends Trap {

	private int waterLevel = 0;
	private int renderX;
	private int renderY;

	private float suffocationTimer = 0f;
	private boolean suffocating = false;

	private TextureRegion[] frames;
	private TextureRegion currentFrame;

	private Texture spriteSheet;

	private Player player;

	public TrapFlood(GameMap map, Player player) {
		spriteSheet = new Texture("textures/traps/TrapFlood_" + map.getName()
				+ ".png");
		this.player = player;

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

	@Override
	public void update(float delta, Player player) {

		if (!isActive) {
			if (checkCondition()) {
				activate(player);
			}
		} else {
			if (checkDeactivationCondition()) {
				deactivate(player);
			} else {

				if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
					if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
						waterLevel = 0;
					} else if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
						waterLevel = 1;
					} else if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
						waterLevel = 2;
					} else if (Gdx.input.isKeyPressed(Keys.NUM_4)) {
						waterLevel = 3;
					} else if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
						waterLevel = 4;
					}
				}

				if (waterLevel >= 3) {
					suffocationTimer += delta;
					if (suffocationTimer >= 3) {
						suffocationTimer = 0f;
						suffocating = true;
					}
				} else {
					suffocationTimer = 0;
					suffocating = false;
				}
				applyTrapOverTime(player);
				statChangeTimer += delta;
			}
		}

	}

	@Override
	public void draw(float delta, SpriteBatch spriteBatch) {

		if (isActive) {
			currentFrame = frames[waterLevel];
			player.setSplitSpriteY(Player.FRAME_HEIGHT
					- (waterLevel * (Player.FRAME_HEIGHT / 4)));

			spriteBatch.draw(currentFrame, renderX, renderY);
		}
	}

	@Override
	protected boolean checkCondition() {
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkDeactivationCondition() {
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if (Gdx.input.isKeyPressed(Keys.NUM_0)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void applyTrapOverTime(Player player) {

		if (suffocating) {
			suffocating = false;
			if (waterLevel == 3) {
				player.setHealth(player.getHealth() - 5);
			} else if (waterLevel == 4) {
				player.setHealth(player.getHealth() - 7);
			}
		}

		if (statChangeTimer >= 2.0f) {
			statChangeTimer = 0f;
			player.setPulse(player.getPulse() + (waterLevel + 1)
					* 4);
		}

	}

	@Override
	public void dispose() {
		spriteSheet.dispose();
	}
	
	@Override
	protected void applyTrapOnActivation(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOnDeactivation(Player player) {
		// TODO Auto-generated method stub
	}

}
