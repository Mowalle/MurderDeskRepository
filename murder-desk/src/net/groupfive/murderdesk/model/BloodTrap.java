package net.groupfive.murderdesk.model;

public class BloodTrap extends Trap {

	private int bloodLevel = 0;

	public BloodTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);
	}

	public BloodTrap(Player target, Room room) {
		this("bloodtrap", "A simple bloodtrap.", target, room);
	}

	public void increaseBloodLevel() {
		if (bloodLevel < 4)
			bloodLevel++;
	}

	public void decreaseBloodLevel() {
		if (bloodLevel > 0)
			bloodLevel--;
	}

	public int getBloodLevel() {
		if(bloodLevel > 4){
			return 4;
		}
		return bloodLevel;
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
		bloodLevel = intensity/2;
	}

	@Override
	protected void applyTrapOverTime() {
		if (stateTime >= 3) {
			stateTime = 0;
			target.setPulse(target.getPulse() + (7 * bloodLevel));
		}
	}

}
