package net.groupfive.murderdesk.traps;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrapGas extends Trap {

	private int renderX;
	private int renderY;

	private float suffocationTimer = 0f;
	private boolean suffocating = false;

	private Texture spriteSheet;

	public TrapGas(GameMap map, Player player) {
		spriteSheet = new Texture("textures/traps/TrapGas_" + map.getName()
				+ ".png");

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
			if (!checkCondition()) {
				deactivate(player);
			} else {

				if (suffocationTimer >= 3) {
					suffocationTimer = 0f;
					suffocating = true;
				} else {
					suffocating = false;
					suffocationTimer += delta;
				}

				applyTrapOverTime(player);
				statChangeTimer += delta;
			}
		}

	}

	@Override
	public void draw(float delta, SpriteBatch spriteBatch) {

		if (isActive) {
			spriteBatch.draw(spriteSheet, renderX, renderY);
		}
	}

	@Override
	protected boolean checkCondition() {
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if (Gdx.input.isKeyPressed(Keys.G)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void applyTrapOverTime(Player player) {

		if (suffocating) {
			suffocating = false;
			player.setHealth(player.getHealth() - 5);
		}

		if (statChangeTimer >= 2.0f) {
			statChangeTimer = 0f;
			player.setMentalPower(player.getMentalPower() - 5);
		}

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
	public void dispose() {
		spriteSheet.dispose();
		
	}

}
