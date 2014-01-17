package net.groupfive.murderdesk.model;

public class FloodTrap extends Trap {

	private int waterLevel = 0;

	public FloodTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);
	}

	public FloodTrap(Player target, Room room) {
		this("floodtrap", "A simple floodtrap.", target, room);
	}

	public void increaseWaterLevel() {
		if (waterLevel < 4)
			waterLevel++;
	}

	public void decreaseWaterLevel() {
		if (waterLevel > 0)
			waterLevel--;
	}
	
	public int getWaterLevel() {
		return waterLevel;
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
		if (stateTime >= 3) {
			stateTime = 0;
			target.setPulse(target.getPulse() + (5 * (waterLevel + 1)));
			
			if (waterLevel >= 2) {
				target.setHealth (target.getHealth() - (5 * (waterLevel + 1)));
			}
		}
	}

}
