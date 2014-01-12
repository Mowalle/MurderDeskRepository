package net.groupfive.murderdesk.traps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.groupfive.murderdesk.GameMap;
import net.groupfive.murderdesk.Player;

public abstract class Trap {

	protected String id;

	protected GameMap myMap;

	protected boolean isActive;
	
	protected float statChangeTimer = 0f;

	/**
	 * Activates this trap and calls applyTrapOnActivation(player).
	 * 
	 * @param player
	 */
	public void activate(Player player) {
		isActive = true;
		applyTrapOnActivation(player);
	}

	/**
	 * Activates this trap and calls applayTrapOnDeactivation(player).
	 * 
	 * @param player
	 */
	public void deactivate(Player player) {
		isActive = false;
		applyTrapOnDeactivation(player);
	}

	/**
	 * Updates this trap. If the trap's condition is true,
	 * applyTrapOverTime(player) is called.
	 * 
	 * @param player
	 */
	public void update(float delta, Player player) {

		if (!isActive) {
			if (checkCondition()) {
				activate(player);
			}
		} else {
			if (checkCondition()) {
				applyTrapOverTime(player);
				statChangeTimer += delta;
			} else {
				deactivate(player);
			}
		}

	}

	public abstract void draw(float delta, SpriteBatch spriteBatch);

	/**
	 * Checks whether to activate or deactivate the trap.
	 * 
	 * @return If true, trap shall be activated, else it shall be deactivated.
	 */
	protected abstract boolean checkCondition();

	/**
	 * Applies the over-time-effect of the trap to the given player. Is called
	 * within the trap's update() method.
	 * 
	 * @param player
	 */
	protected abstract void applyTrapOverTime(Player player);

	/**
	 * Applies the effect during trap activation to the given player. Is called
	 * within the trap's activate() method.
	 * 
	 * @param player
	 */
	protected abstract void applyTrapOnActivation(Player player);

	/**
	 * Applies the effect during trap deactivation to the given player. Is
	 * called within the trap's deactivate() method.
	 * 
	 * @param player
	 */
	protected abstract void applyTrapOnDeactivation(Player player);

	/**
	 * Returns whether this trap is active.
	 * 
	 * @return true, when trap is active, else false.
	 */
	public boolean isActive() {
		return isActive;
	}

}
