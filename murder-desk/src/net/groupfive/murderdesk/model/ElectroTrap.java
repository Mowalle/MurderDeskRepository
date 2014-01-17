package net.groupfive.murderdesk.model;

import java.util.Random;

import net.groupfive.murderdesk.model.Player.DeathType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ElectroTrap extends Trap {

	Array<Vector2> electroFields = new Array<Vector2>();
	private Random randGenerator = new Random();

	private int intensitySummand = 0;

	public ElectroTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);

		createNewField();
	}

	public ElectroTrap(Player target, Room room) {
		this("electrotrap", "A simple electrotrap.", target, room);
	}

	public Array<Vector2> getElectroFields() {
		return electroFields;
	}

	public void increase() {
		if (intensitySummand < 20)
			intensitySummand++;

		createNewField();
	}

	public void decrease() {
		if (intensitySummand > 0) {
			intensitySummand--;
		}
		createNewField();
	}

	private void createNewField() {
		electroFields = new Array<Vector2>();

		int randChance = 0;

		for (int i = 0; i < myRoom.getWidth(); i++) {
			for (int j = 0; j < myRoom.getHeight(); j++) {

				if (myRoom.getTileId(0, i, j) != -1) {
					randChance = randGenerator.nextInt(99) + 1;

					if (randChance <= 5 + intensitySummand) {
						electroFields.add(myRoom
								.convertToScreenCoordinates(new Vector2(
										(float) i, (float) j)));
					}
				}
			}
		}
	}

	@Override
	protected void applyTrapOnActivation() {
		for (Vector2 electroField : electroFields) {
			if (target.getState().equals(Player.State.IDLE)
					&& target.getPosition().x == electroField.x
					&& target.getPosition().y == electroField.y) {
				target.setPulse(target.getPulse() + 30);
			}
		}
	}

	@Override
	protected void applyTrapOnDeactivation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyTrapOverTime() {
		if (stateTime >= 0.5f) {
			stateTime = 0;
			for (Vector2 electroField : electroFields) {
				if (target.getState().equals(Player.State.IDLE)
						&& target.getPosition().x == electroField.x
						&& target.getPosition().y == electroField.y) {
					target.setPulse(target.getPulse() + 15);
					target.setHealth(target.getHealth() - 15);
				}
				if (target.getHealth() <= 0) {
					target.setState(Player.State.DEAD);
					target.setDeathType(DeathType.ELECTROCUTION);
				}
			}
		}
	}
}
