package net.goupfive.murderdesk.model;

public abstract class Trap {

	protected Player target;

	// TODO Needs a way to be removed from here!
	/**
	 * Needs a reference to the containing room to check if in the same room as
	 * player.
	 **/
	protected Room myRoom;

	private String name;
	private String description;

	protected boolean active = false;

	protected float stateTime = 0f;

	public Trap(String name, String description, Player target, Room room) {
		this.target = target;
		this.myRoom = room;
		this.name = name;
		this.description = description;
	}

	/**
	 * Activates this trap and calls applyTrapOnActivation(target).
	 */
	public void activate() {
		active = true;
		if (target.getMyRoom().equals(myRoom))
			applyTrapOnActivation();
	}

	public void deactivate() {
		active = false;
		if (target.getMyRoom().equals(myRoom))
			applyTrapOnDeactivation();
	}

	public void update(float delta) {
		stateTime += delta;
		if (active)
			if (target.getMyRoom().equals(myRoom))
				applyTrapOverTime();
	}

	public boolean isActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Room getMyRoom() {
		return myRoom;
	}
	
	public float getStateTime() {
		return stateTime;
	}

	protected abstract void applyTrapOnActivation();

	protected abstract void applyTrapOnDeactivation();

	protected abstract void applyTrapOverTime();

}
