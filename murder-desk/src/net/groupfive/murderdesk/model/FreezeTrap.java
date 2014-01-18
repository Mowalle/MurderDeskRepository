package net.groupfive.murderdesk.model;

public class FreezeTrap extends Trap {

	public int freezeLevel = 0;
	
	public FreezeTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);
	}

	public FreezeTrap(Player target, Room room) {
		this("freezetrap", "A simple freezetrap.", target, room);
	}
	
	public void increaseFreezeLevel() {
		if (freezeLevel < 10)
			freezeLevel++;
	}
	
	public void decreaseFreezeLevel() {
		if (freezeLevel > 0)
			freezeLevel--;
	}
	
	public int getFreezeLevel() {
		return freezeLevel;
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
	public void setIntensity(int intensity){
		this.freezeLevel = intensity;
	}
	
	@Override
	protected void applyTrapOverTime() {
		if (stateTime > 3) {
			stateTime = 0;
			target.setPulse(target.getPulse() + 2 * freezeLevel);
			
			if (freezeLevel >= 5) {
				target.setHealth(target.getHealth() - freezeLevel);
			}
		}
	}

}
