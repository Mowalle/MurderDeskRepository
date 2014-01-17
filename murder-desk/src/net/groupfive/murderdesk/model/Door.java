package net.groupfive.murderdesk.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Door {

	private Vector2 position = new Vector2();
	private Rectangle bounds = new Rectangle();

	/** Width of the door. In this case defines only the base tile. **/
	public static final float SIZE_X = 1f; 
	/** Height of the door. In this case defines only the base tile. **/
	public static final float SIZE_Y = 1f;

	private Door targetDoor;
	/** Reference to the room that contains this door. **/
	private Room myRoom;

	private boolean opened;
	private boolean facingLeft;

	public Door(Vector2 position, boolean facingLeft, Room room) {
		this.position = room.convertToScreenCoordinates(position);
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.width = SIZE_X;
		this.bounds.height = SIZE_Y;
		this.facingLeft = facingLeft;
		this.myRoom = room;
	}
	
	public void connectTo(Door door) {
		this.targetDoor = door;
		door.setTargetDoor(this);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Door getTargetDoor() {
		return targetDoor;
	}
	
	public void setTargetDoor(Door door) {
		this.targetDoor = door;
	}

	public Room getMyRoom() {
		return myRoom;
	}
	
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

}
