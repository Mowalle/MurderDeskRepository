package net.groupfive.murderdesk.model;

public class GasTrap extends Trap{

	private int gasLevel = 0;
	
	public GasTrap(String name, String description, Player target, Room room) {
		super(name, description, target, room);
		// TODO Auto-generated constructor stub
	}
	
	public GasTrap(Player target, Room room) {
		this("gastrap", "A simple gastrap.", target, room);
	}
	
	public void increaseGasLevel() {
		if (gasLevel < 10)
			gasLevel++;
	}
	
	public void decreaseGasLevel() {
		if (gasLevel > 0)
			gasLevel--;
	}
	
	public int getGasLevel() {
		return gasLevel;
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
		if (stateTime > 3) {
			stateTime = 0;
			target.setPulse(target.getPulse() + 2 * gasLevel);
			
			if (gasLevel >= 5) {
				target.setHealth(target.getHealth() - gasLevel);
			}
		}
		
	}

	
	
}
