package net.groupfive.murderdesk.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TrapdoorTrap extends Trap {

	public static final float SIZE_X = 2f; // Frame Width of Sprite / Tile Width
										   // of Map
	public static final float SIZE_Y = 2f; // two units high

	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();

	public TrapdoorTrap(String name, String description, Player target,
			Room room, Vector2 pos) {
		super(name, description, target, room);
		this.position = room.convertToScreenCoordinates(pos);
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.width = SIZE_X;
		this.bounds.height = SIZE_Y;
	}

	public TrapdoorTrap(Player target, Room room, Vector2 pos) {
		this("trapdoor", "A simple trapdoor.", target, room, pos);
	}

	@Override
	protected void applyTrapOnActivation() {
		// If target is standing still
		if (target.getState().equals(Player.State.IDLE)) {

			if (target.getPosition().x >= position.x + 1f
					&& target.getPosition().x <= position.x + 1.5f
					&& target.getPosition().y >= position.y + 0.5f
					&& target.getPosition().y <= position.y + 1f) {
				target.setFacingLeft(true);
				target.setFacingDown(true);
				target.setPulse(target.getPulse() + 20);
			} else if (target.getPosition().x >= position.x + 1f
					&& target.getPosition().x <= position.x + 1.5f
					&& target.getPosition().y >= position.y - 1f
					&& target.getPosition().y <= position.y - 0.5f) {
				target.setFacingLeft(true);
				target.setFacingDown(false);
				target.setPulse(target.getPulse() + 20);
			} else if (target.getPosition().x >= position.x - 0.5f
					&& target.getPosition().x <= position.x
					&& target.getPosition().y >= position.y - 1f
					&& target.getPosition().y <= position.y - 0.5f) {
				target.setFacingLeft(false);
				target.setFacingDown(false);
				target.setPulse(target.getPulse() + 20);
			} else if (target.getPosition().x >= position.x - 0.5f
					&& target.getPosition().x <= position.x
					&& target.getPosition().y >= position.y + 0.5f
					&& target.getPosition().y <= position.y + 1f) {
				target.setFacingLeft(false);
				target.setFacingDown(true);
				target.setPulse(target.getPulse() + 20);
			}
		}
	}

	@Override
	protected void applyTrapOnDeactivation() {
		// TODO
	}

	@Override
	protected void applyTrapOverTime() {
		// Kill player if he is inside trap

		// If player is standing still (to not interrupt movement)
		if (target.getState().equals(Player.State.IDLE)) {
			// If player stands directly on trapdoor
			if (target.getPosition().x >= position.x
					&& target.getPosition().x <= position.x + 1f
					&& target.getPosition().y >= position.y - 0.5f
					&& target.getPosition().y <= position.y + 0.5f) {
				target.setState(Player.State.DEAD);
			}
		}
	}

	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getBounds() {
		return bounds;
	}

}
