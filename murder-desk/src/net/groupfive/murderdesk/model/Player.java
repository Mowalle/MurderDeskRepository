package net.groupfive.murderdesk.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

	public enum State {
		IDLE, WALKING, DEAD, DEAD_GORE, DEAD_TRAPDOOR, DEAD_ELECTROCUTION
	}

	public enum Condition {
		NORMAL, PANIC, HURT
	}

	public static final float SIZE_X = 31f / 64f; // Frame Width of Sprite /Tile
	public static final float SIZE_Y = 52f / 32f; // two units high

	public static final int PULSE_DEFAULT = 80;
	public static final int PULSE_MAXIMUM = 210;
	public static final int PULSE_MINIMUM = 30;

	private int pulse = 80;
	private int health = 100;
	private float regenerationTimer = 0f;

	private float speed = 0.5f;

	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();
	State state = State.IDLE;
	Condition condition = Condition.NORMAL;
	boolean facingLeft = true;
	boolean facingDown = true;
	float stateTime = 0f;

	/**
	 * Reference to the room the player currently is in. Might be different then
	 * world.currentRoom.
	 **/
	private Room myRoom;

	public Player(Room initRoom, Vector2 position) {
		this.myRoom = initRoom;
		this.position = initRoom.convertToScreenCoordinates(position);
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.width = SIZE_X;
		this.bounds.height = SIZE_Y;

		// TODO Can actually be removed if player is spawned with normal pulse
		update(0f);

	}

	public void update(float delta) {
		stateTime += delta;

		// Check if player is dead
		if (health <= 0 || pulse >= PULSE_MAXIMUM || pulse <= PULSE_MINIMUM) {
			health = 0;
			state = State.DEAD;
		}

		// Check if player is hurt
		if (health <= 34) {
			condition = Condition.HURT;
		} else if (pulse >= 120) { // Check if player is in panic
			condition = Condition.PANIC;
		} else {
			condition = Condition.NORMAL;
		}

		// Apply Condition effects
		switch (condition) {
		case HURT:
			speed = 1f;
			break;
		case PANIC:
			speed = 0.25f;
			break;
		case NORMAL:
			speed = 0.5f;
			break;
		default:
			break;
		}

		if (!state.equals(State.DEAD) && !state.equals(State.DEAD_GORE)
				&& !state.equals(State.DEAD_TRAPDOOR)
				&& !state.equals(State.DEAD_ELECTROCUTION)) {
			// Restore Health and Pulse every second
			regenerationTimer += delta;

			if (regenerationTimer >= 1f) {
				System.out.println("Health:\t" + health + "\t||\tPulse:\t"
						+ pulse + "\t||\tState:\t" + state
						+ "\t||\tCondition:\t" + condition);

				if (health < 100) {
					health++;
				}
				if (pulse > PULSE_DEFAULT) {
					pulse--;
				} else if (pulse < PULSE_DEFAULT) {
					pulse++;
				}

				regenerationTimer = 0f;
			}
		} else {
			System.out.println("Health:\t" + health + "\t||\tPulse:\t" + pulse
					+ "\t||\tState:\t" + state + "\t||\tCondition:\t"
					+ condition);
		}
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public boolean isFacingDown() {
		return facingDown;
	}

	public void setFacingDown(boolean facingDown) {
		this.facingDown = facingDown;
	}

	public Room getMyRoom() {
		return myRoom;
	}

	public void setMyRoom(Room room) {
		this.myRoom = room;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		this.bounds.setX(position.x);
		this.bounds.setY(position.y);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public State getState() {
		return state;
	}

	public void setState(State newState) {
		this.state = newState;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}