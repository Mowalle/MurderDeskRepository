package net.goupfive.murderdesk.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Door {

	public enum Type {
		DEFAULT, EXIT, METAL
	}
	
	private Type type = Type.DEFAULT;
	
	private Vector2 position = new Vector2();
	private Rectangle bounds = new Rectangle();

	/** Width of the door. In this case defines only the base tile. **/
	public static final float SIZE_X = 1f; 
	/** Height of the door. In this case defines only the base tile. **/
	public static final float SIZE_Y = 1f;

	private Door targetDoor;
	/** Reference to the room that contains this door. **/
	private Room myRoom;

	private boolean open;
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
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
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
	
	public boolean isFacingLeft() {
		return facingLeft;
	}	
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		setOpen(open, true);
	}
	
	private void setOpen(boolean open, boolean chaining) {
		
		if (targetDoor == null) {
			System.out.println("Door at " + myRoom.convertToMapCoordinates(position) + " cannot be opened: door is not connected!");
			this.open = false;
		} else {
			this.open = open;
			
			if (chaining)
				targetDoor.setOpen(open, false);
			
		}
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if (!(o instanceof Room))
//			return false;
//		Door d = (Door) o;
//		return (this.myRoom.equals(d.getMyRoom()));
//	}

}
