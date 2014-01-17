package net.groupfive.murderdesk.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {

	/** AI controlled hero **/
	Player player;
	/** A world has multiple rooms through which player needs to go through. **/
	Array<Room> rooms = new Array<Room>();
	/** A world has a current rooms that is displayed on-screen. **/
	private Room currentRoom;

	// Getters
	public Player getPlayer() {
		return player;
	}

	public void addRoom(Room room) {
		if (!rooms.contains(room, false)) {
			rooms.add(room);
		}
	}
	
	public Array<Room> getRooms() {
		return rooms;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room room) {
		if (rooms.contains(room, false)) {
			this.currentRoom = rooms.get(rooms.indexOf(room, false));
		} else {
			rooms.add(room);
			this.currentRoom = room;
		}
	}

	public void setCurrentRoom(int index) {
		index = index % rooms.size;
		this.currentRoom = rooms.get(index);
	}

	public World() {
		createDemoWorld();
	}

	private void createDemoWorld() {
		// ** Room Creation ******************* //
		
		// Create Rooms
		Room basement = new Room("data/maps/01basement.tmx", "basement");
		Room corridor = new Room("data/maps/02corridor.tmx", "corridor");
		Room foodchamber = new Room("data/maps/03foodchamber.tmx", "foodchamber");

		// Add doors (max. two per room)
		Vector2 basementDoorLeft = new Vector2(2, 12);
		Vector2 basementDoorRight = new Vector2(12, 2);
		
		basement.addDoor(new Door(basementDoorLeft, false, basement));
		basement.addDoor(new Door(basementDoorRight, true, basement));
		
		Vector2 corridorDoorLeft = new Vector2(2, 12);
		Vector2 corridorDoorRight = new Vector2(12, 2);

		corridor.addDoor(new Door(corridorDoorLeft, false, corridor));
		corridor.addDoor(new Door(corridorDoorRight, true, corridor));
		
		Vector2 foodChamberDoorLeft = new Vector2(2, 12);
		Vector2 foodChamberDoorRight = new Vector2(12, 2);
		
		foodchamber.addDoor(new Door(foodChamberDoorLeft, false, foodchamber));
		foodchamber.addDoor(new Door(foodChamberDoorRight, true, foodchamber));
		
		basement.getDoors().get(0).connectTo(corridor.getDoors().get(1));
		corridor.getDoors().get(0).connectTo(foodchamber.getDoors().get(1));
		
		// Add Rooms to world
		addRoom(basement);
		addRoom(corridor);
		addRoom(foodchamber);
		
		// Set the current room (Player will get spawned here)
		setCurrentRoom(0);
		
		// ** Player Spawn ******************* //
		Vector2 spawn = new Vector2(4, 4); // This is the spawn point.
										   // Coordinates correspond to
										   // coordinates in Tiled Map Editor.

		player = new Player(getCurrentRoom(), spawn);
		
		// ** Trap Creation ******************* //
		
		// Add Traps (max. 3 per room)
		basement.addTrap(new TrapdoorTrap(player, basement, new Vector2(6, 10)));
		
		// Set current trap
		currentRoom.setCurrentTrap(1);
		
		System.out.println("Player spawned: Spawn " + spawn + "--->"
				+ player.getPosition() + "(Screen)");
	}
}
