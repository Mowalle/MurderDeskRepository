package net.groupfive.murderdesk.gdx.controller;

import java.util.HashMap;
import java.util.Map;

import net.groupfive.murderdesk.gdx.model.Trap;
import net.groupfive.murderdesk.gdx.model.TrapdoorTrap;
import net.groupfive.murderdesk.gdx.model.World;

public class RoomController {

	enum Keys {
		ROOM_ONE, ROOM_TWO, ROOM_THREE, DOOR_ONE, DOOR_TWO, DOOR_NONE, TRAP_ONE, TRAP_TWO, TRAP_THREE, TRAP_TOGGLE, TRAP_INCREASE, TRAP_DECREASE, TRAP_FINAL
	}

	static Map<Keys, Boolean> keys = new HashMap<RoomController.Keys, Boolean>();
	static {
		keys.put(Keys.TRAP_TOGGLE, false);
		keys.put(Keys.TRAP_INCREASE, false);
		keys.put(Keys.TRAP_DECREASE, false);
		keys.put(Keys.TRAP_FINAL, false);
		keys.put(Keys.TRAP_ONE, false);
		keys.put(Keys.TRAP_TWO, false);
		keys.put(Keys.TRAP_THREE, false);
		keys.put(Keys.ROOM_ONE, false);
		keys.put(Keys.ROOM_TWO, false);
		keys.put(Keys.ROOM_THREE, false);
		keys.put(Keys.DOOR_ONE, false);
		keys.put(Keys.DOOR_TWO, false);
		keys.put(Keys.DOOR_NONE, false);

	};

	private World world;

	public RoomController(World world) {
		this.world = world;
	}

	// ** Key presses and touches **************** //

	public void roomOnePressed() {
		keys.get(keys.put(Keys.ROOM_ONE, true));
	}

	public void roomOneReleased() {
		keys.get(keys.put(Keys.ROOM_ONE, false));
	}

	public void roomTwoPressed() {
		keys.get(keys.put(Keys.ROOM_TWO, true));
	}

	public void roomTwoReleased() {
		keys.get(keys.put(Keys.ROOM_TWO, false));
	}

	public void roomThreePressed() {
		keys.get(keys.put(Keys.ROOM_THREE, true));
	}

	public void roomThreeReleased() {
		keys.get(keys.put(Keys.ROOM_THREE, false));
	}

	public void doorOnePressed() {
		keys.get(keys.put(Keys.DOOR_ONE, true));
	}

	public void doorOneReleased() {
		keys.get(keys.put(Keys.DOOR_ONE, false));
	}

	public void doorTwoPressed() {
		keys.get(keys.put(Keys.DOOR_TWO, true));
	}

	public void doorTwoReleased() {
		keys.get(keys.put(Keys.DOOR_TWO, false));
	}

	public void doorNonePressed() {
		keys.get(keys.put(Keys.DOOR_NONE, true));
	}

	public void doorNoneReleased() {
		keys.get(keys.put(Keys.DOOR_NONE, false));
	}
	
	public void trapOnePressed() {
		keys.get(keys.put(Keys.TRAP_ONE, true));
	}

	public void trapOneReleased() {
		keys.get(keys.put(Keys.TRAP_ONE, false));
	}
	
	public void trapTwoPressed() {
		keys.get(keys.put(Keys.TRAP_TWO, true));
	}

	public void trapTwoReleased() {
		keys.get(keys.put(Keys.TRAP_TWO, false));
	}
	
	public void trapThreePressed() {
		keys.get(keys.put(Keys.TRAP_THREE, true));
	}

	public void trapThreeReleased() {
		keys.get(keys.put(Keys.TRAP_THREE, false));
	}

	public void trapTooglePressed() {
		keys.get(keys.put(Keys.TRAP_TOGGLE, true));
	}

	public void trapToggleReleased() {
		keys.get(keys.put(Keys.TRAP_TOGGLE, false));
	}

	public void trapIncreasePressed() {
		keys.get(keys.put(Keys.TRAP_INCREASE, true));
	}

	public void trapIncreaseReleased() {
		keys.get(keys.put(Keys.TRAP_INCREASE, false));
	}

	public void trapDecreasePressed() {
		keys.get(keys.put(Keys.TRAP_DECREASE, true));
	}

	public void trapDecreaseReleased() {
		keys.get(keys.put(Keys.TRAP_DECREASE, false));
	}
	
	public void trapFinalPressed() {
		keys.get(keys.put(Keys.TRAP_FINAL, true));
	}
	
	public void trapFinalReleased() {
		keys.get(keys.put(Keys.TRAP_FINAL, false));
	}

	/** The main update method. **/
	public void update(float delta) {
		// Processing the input - setting the states of Player
		processInput();

		// simply updates the state time
		if (world.getCurrentRoom().getTraps().size > 0) {
			Trap trap = world.getCurrentRoom().getCurrentTrap();
			trap.update(delta);
		}
	}

	/** Change trap's state and parameters based on input controls **/
	private boolean processInput() {

		if (keys.get(Keys.ROOM_ONE)) {
			// If there is a defined room
			if (world.getRooms().size >= 1) {
				// If current room is different then pressed key
				if (!world.getCurrentRoom().equals(world.getRooms().get(0))) {
					// If world has at least one trap
					if (world.getCurrentRoom().getTraps().size > 0) {
						// If current trap is deactivated
						if (!world.getCurrentRoom().getCurrentTrap().isActive())
							world.setCurrentRoom(0);
					} else {
						// else switch directly
						world.setCurrentRoom(0);
					}
				}
			}
		} else if (keys.get(Keys.ROOM_TWO)) {
			// If there are two defined room
			if (world.getRooms().size >= 2) {
				// If current room is different then pressed key
				if (!world.getCurrentRoom().equals(world.getRooms().get(1))) {
					// If world has at least one trap
					if (world.getCurrentRoom().getTraps().size > 0) {
						// If current trap is deactivated
						if (!world.getCurrentRoom().getCurrentTrap().isActive())
							world.setCurrentRoom(1);
					} else {
						// else switch directly
						world.setCurrentRoom(1);
					}
				}
			}
		} else if (keys.get(Keys.ROOM_THREE)) {
			// If there are two defined room
			if (world.getRooms().size >= 3) {
				// If current room is different then pressed key
				if (!world.getCurrentRoom().equals(world.getRooms().get(2))) {
					// If world has at least one trap
					if (world.getCurrentRoom().getTraps().size > 0) {
						// If current trap is deactivated
						if (!world.getCurrentRoom().getCurrentTrap().isActive())
							world.setCurrentRoom(2);
					} else {
						// else switch directly
						world.setCurrentRoom(2);
					}
				}
			}
		}

		if (world.getCurrentRoom().getTraps().size > 0) {
			Trap trap = world.getCurrentRoom().getCurrentTrap();

			if (keys.get(Keys.TRAP_TOGGLE)) {
				if (trap instanceof TrapdoorTrap) {
					if (!trap.isActive()) {
						trap.activate();
						System.out
								.println(world.getCurrentRoom().getName()
										+ ", "
										+ world.getCurrentRoom().getTraps().size);
						trapToggleReleased(); // To limit input triggering
					} else if (trap.isActive()) {
						trap.deactivate();
						trapToggleReleased(); // To limit input triggering
					}
				}
			}
		}
		return false;
	}

}
