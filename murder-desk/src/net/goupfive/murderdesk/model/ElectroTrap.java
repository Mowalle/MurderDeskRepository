package net.goupfive.murderdesk.model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ElectroTrap extends Trap {

	Array<Vector2> electroFields = new Array<Vector2>();
	private Random randGenerator = new Random();

	private int intensitySummand = 0;

	public ElectroTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);

		int randChance = 0;

		for (int i = 0; i < room.getWidth(); i++) {
			for (int j = 0; j < room.getHeight(); j++) {

				if (room.getTileId(0, i, j) != -1) {
					randChance = randGenerator.nextInt(99) + 1;

					if (randChance <= 10 + intensitySummand) {
						electroFields.add(myRoom
								.convertToScreenCoordinates(new Vector2(
										(float) i, (float) j)));
					}
				}
			}
		}
	}

	public ElectroTrap(Player target, Room room) {
		this("electrotrap", "A simple electrotrap.", target, room);
	}

	public Array<Vector2> getElectroFields() {
		return electroFields;
	}

	@Override
	protected void applyTrapOnActivation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOnDeactivation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOverTime() {
		for (Vector2 electroField : electroFields) {
			if (target.getState().equals(Player.State.IDLE)
					&& target.getPosition().x == electroField.x
					&& target.getPosition().y == electroField.y) {
				target.setPulse(target.getPulse() + 30);
				target.setHealth(target.getHealth() - 30);
			}
		}
	}
}
