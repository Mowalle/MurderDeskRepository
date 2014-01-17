package net.groupfive.murderdesk.model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpikeTrap extends Trap {

	Array<Vector2> spikeFields = new Array<Vector2>();
	private Random randGenerator = new Random();

	private int intensitySummand = 0;

	public SpikeTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);

		createNewField();
	}

	public SpikeTrap(Player target, Room room) {
		this("spiketrap", "A simple spiketrap.", target, room);
	}
	
	public void increase() {
		if (intensitySummand < 10)
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
		spikeFields = new Array<Vector2>();
		
		int randChance = 0;

		for (int i = 0; i < myRoom.getWidth(); i++) {
			for (int j = 0; j < myRoom.getHeight(); j++) {

				if (myRoom.getTileId(0, i, j) != -1) {
					randChance = randGenerator.nextInt(99) + 1;

					if (randChance <= 2 + intensitySummand) {
						spikeFields.add(myRoom
								.convertToScreenCoordinates(new Vector2(
										(float) i, (float) j)));
					}
				}
			}
		}
	}

	@Override
	protected void applyTrapOnActivation() {
		for (Vector2 v : spikeFields) {
			if (target.getState().equals(Player.State.IDLE)
					&& target.getPosition().x == v.x
					&& target.getPosition().y == v.y) {
				target.setPulse(target.getPulse() + 30);
				target.setHealth(target.getHealth() - 30);
			}
		}
	}

	@Override
	protected void applyTrapOnDeactivation() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void applyTrapOverTime() {
		if (stateTime >= 3) {
			stateTime = 0;
			for (Vector2 v : spikeFields) {
				if (target.getState().equals(Player.State.IDLE)
						&& target.getPosition().x == v.x
						&& target.getPosition().y == v.y) {
					target.setPulse(target.getPulse() + 10);
					target.setHealth(target.getHealth() - 10);
				}
			}
		}
	}
	
	public Array<Vector2> getSpikeFields() {
		return spikeFields;
	}

}
