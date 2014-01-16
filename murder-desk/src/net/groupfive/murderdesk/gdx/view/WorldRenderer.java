package net.groupfive.murderdesk.gdx.view;

import net.groupfive.murderdesk.gdx.model.Door;
import net.groupfive.murderdesk.gdx.model.Player;
import net.groupfive.murderdesk.gdx.model.Room;
import net.groupfive.murderdesk.gdx.model.Trap;
import net.groupfive.murderdesk.gdx.model.TrapdoorTrap;
import net.groupfive.murderdesk.gdx.model.World;
import net.groupfive.murderdesk.gdx.model.Player.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class WorldRenderer {

	private static final float CAMERA_WIDTH = 10f;
	private static final float CAMERA_HEIGHT = 12.5f;
	private static final float WALKING_FRAME_DURATION = 0.1f;

	private static final float PLAYER_OFFSET_X = (1f - Player.SIZE_X) / 2f;
	private static final float PLAYER_OFFSET_Y = (2f - Player.SIZE_Y) / 2f;

	private static final float TRAP_OFFSET_X = 0f;
	private static final float TRAP_OFFSET_Y = -0.5f;

	private World world;
	private OrthographicCamera cam;

	/** For debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	/** Custom renderer for map rendering **/
	IsometricScaledTiledMapRenderer isoRenderer;

	/** Textures **/
	private Texture playerSpriteSheet;
	private TextureRegion playerIdleLeftDown;
	private TextureRegion playerIdleLeftUp;
	private TextureRegion playerIdleRightDown;
	private TextureRegion playerIdleRightUp;
	private TextureRegion playerFrame;

	private Texture trapdoorTrapSprite;
	private TextureRegion trapdoorClosed;
	private TextureRegion trapdoorOpened;

	// General container for current trap frame
	private TextureRegion trapFrame;

	/** Animations **/
	private Animation walkLeftDownAnimation;
	private Animation walkLeftUpAnimation;
	private Animation walkRightDownAnimation;
	private Animation walkRightUpAnimation;
	
	/** Overlay **/
	private ShapeRenderer light;
	private float lightIntensity = 1.0f;
	
	private SpriteBatch spriteBatch;
	private boolean debug = false;

	private int width;
	private int height;
	private float ppuX; // pixels per unit on x-axis
	private float ppuY; // pixels per unit on y-axis

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.ppuX = (float) width / CAMERA_WIDTH;
		this.ppuY = (float) height / CAMERA_HEIGHT;
	}

	public WorldRenderer(World world, boolean debug) {
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, 0);
		cam.update();
		this.debug = debug;
		spriteBatch = new SpriteBatch();
		// TODO Maybe use ppuX and ppuY instead?
		isoRenderer = new IsometricScaledTiledMapRenderer(world
				.getCurrentRoom().getTiledMap(), 1 / (float) Room.TILE_WIDTH,
				1 / (float) Room.TILE_HEIGHT);
		loadTextures();
	}

	private void loadTextures() {
		playerSpriteSheet = new Texture(
				Gdx.files.internal("textures/SimonSprite.png"));
		TextureRegion[][] frames = TextureRegion.split(playerSpriteSheet, 31,
				52);
		playerIdleLeftDown = frames[0][0];
		playerIdleLeftUp = frames[0][2];
		playerIdleRightDown = frames[0][1];
		playerIdleRightUp = frames[0][3];

		// TODO Load other textures and animations (for traps etc...)

		TextureRegion[] walkLeftDownFrames = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			walkLeftDownFrames[i] = frames[1][i];

		}
		walkLeftDownAnimation = new Animation(WALKING_FRAME_DURATION,
				walkLeftDownFrames);

		TextureRegion[] walkLeftUpFrames = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			walkLeftUpFrames[i] = frames[3][i];

		}
		walkLeftUpAnimation = new Animation(WALKING_FRAME_DURATION,
				walkLeftUpFrames);

		TextureRegion[] walkRightDownFrames = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			walkRightDownFrames[i] = frames[2][i];

		}
		walkRightDownAnimation = new Animation(WALKING_FRAME_DURATION,
				walkRightDownFrames);

		TextureRegion[] walkRightUpFrames = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			walkRightUpFrames[i] = frames[4][i];

		}
		walkRightUpAnimation = new Animation(WALKING_FRAME_DURATION,
				walkRightUpFrames);

		// * Trap textures ***************************//
		trapdoorTrapSprite = new Texture(
				Gdx.files.internal("textures/TrapdoorTrap.png"));
		trapdoorClosed = new TextureRegion(trapdoorTrapSprite, 0, 0, 128, 64);
		trapdoorOpened = new TextureRegion(trapdoorTrapSprite, 0, 64, 128, 64);
		
		// Light //
		light = new ShapeRenderer();
	}

	public void render() {

		cam.update();

		// True because player over layers
		drawRoom(true);

		// FIXME Has to be moved into spriteBatch when use of real sprites
		if (world.getCurrentRoom().getDoors().size > 0) {
			drawDoor();
		}

		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();

		if (world.getCurrentRoom().getTraps().size > 0)
			drawTrap();

		// Player should only be rendered if he is inside the room that is
		// currently displayed
		if (world.getCurrentRoom().equals(world.getPlayer().getMyRoom()))
			drawPlayer();
		spriteBatch.end();

		// False because layers over player
		drawRoom(false);
		
		Gdx.gl.glEnable(GL10.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		light.begin(ShapeType.Filled);
		light.setColor(0,0,0,1-lightIntensity);
		light.rect(0, 0, width, height);
		light.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		if (debug) {
			drawDebug();
		}
	}

	private void drawRoom() {
		Room room = world.getCurrentRoom();
		isoRenderer.setMap(room.getTiledMap());
		isoRenderer.setView(cam);
		isoRenderer.getSpriteBatch().setProjectionMatrix(cam.combined);
		isoRenderer.render();
	}

	/**
	 * Renders the layers that are above or below the player.
	 * 
	 * @param playerAboveLayer
	 *            - If true, only the layers below the player are rendered, else
	 *            the layers above the player are rendered.
	 */
	private void drawRoom(boolean playerAboveLayer) {
		Room room = world.getCurrentRoom();
		com.badlogic.gdx.maps.tiled.TiledMap map = room.getTiledMap();
		isoRenderer.setMap(room.getTiledMap());
		isoRenderer.setView(cam);
		isoRenderer.getSpriteBatch().setProjectionMatrix(cam.combined);
		isoRenderer.getSpriteBatch().begin();

		if (playerAboveLayer) {
			for (int i = 0; i < map.getLayers().getCount(); i++) {
				if (!map.getLayers().get(i).getName()
						.equalsIgnoreCase("WalkBehind") && map.getLayers().get(i).isVisible()) { // TODO Hardcoded
														   // value
					isoRenderer.renderTileLayer((TiledMapTileLayer) map
							.getLayers().get(i));
				}
			}
		} else {
			for (int i = 0; i < map.getLayers().getCount(); i++) {
				if (map.getLayers().get(i).getName()
						.equalsIgnoreCase("WalkBehind") && map.getLayers().get(i).isVisible()) {
					isoRenderer.renderTileLayer((TiledMapTileLayer) map
							.getLayers().get(i));
				}
			}
		}
		isoRenderer.getSpriteBatch().end();
	}

	// TODO Should be moved into drawRoom()
	private void drawDoor() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);

		// Render Player Box

		for (Door door : world.getCurrentRoom().getDoors()) {
			if (!door.isOpened()) {
				debugRenderer.setColor(new Color(0.5f, 0.5f, 0, 1));
			} else {
				debugRenderer.setColor(new Color(1f, 1f, 0, 1));
			}
			debugRenderer.rect(door.getPosition().x, door.getPosition().y,
					Door.SIZE_X, Door.SIZE_Y);
		}

		debugRenderer.end();
	}

	private void drawTrap() {
		Trap trap = world.getCurrentRoom().getCurrentTrap();

		if (trap instanceof TrapdoorTrap) {
			if (!trap.isActive()) {
				trapFrame = trapdoorClosed;
			} else {
				trapFrame = trapdoorOpened;
			}
			spriteBatch.draw(trapFrame, ((TrapdoorTrap) trap).getPosition().x
					+ TRAP_OFFSET_X, ((TrapdoorTrap) trap).getPosition().y
					+ TRAP_OFFSET_Y, TrapdoorTrap.SIZE_X, TrapdoorTrap.SIZE_Y);
		}
	}

	private void drawPlayer() {
		Player player = world.getPlayer();

		// Idle Animation
		if (player.isFacingLeft()) {
			if (player.isFacingDown()) {
				playerFrame = playerIdleLeftDown;
			} else {
				playerFrame = playerIdleLeftUp;
			}
		} else {
			if (player.isFacingDown()) {
				playerFrame = playerIdleRightDown;
			} else {
				playerFrame = playerIdleRightUp;
			}
		}

		// Walking Animation
		if (player.getState().equals(State.WALKING)) {
			if (player.isFacingLeft()) {
				if (player.isFacingDown()) {
					playerFrame = walkLeftDownAnimation.getKeyFrame(
							player.getStateTime(), true);
				} else {
					playerFrame = walkLeftUpAnimation.getKeyFrame(
							player.getStateTime(), true);
				}
			} else {
				if (player.isFacingDown()) {
					playerFrame = walkRightDownAnimation.getKeyFrame(
							player.getStateTime(), true);
				} else {
					playerFrame = walkRightUpAnimation.getKeyFrame(
							player.getStateTime(), true);
				}
			}
		}

		spriteBatch.draw(playerFrame, player.getPosition().x + PLAYER_OFFSET_X,
				player.getPosition().y + PLAYER_OFFSET_Y, Player.SIZE_X,
				Player.SIZE_Y);
	}

	private void drawDebug() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);

		// Render Player Box
		Player player = world.getPlayer();
		if (world.getCurrentRoom().equals(player.getMyRoom())) {
			debugRenderer.setColor(new Color(0, 1, 0, 1));
			debugRenderer.rect(player.getPosition().x + PLAYER_OFFSET_X,
					player.getPosition().y + PLAYER_OFFSET_Y, Player.SIZE_X,
					Player.SIZE_Y);
			debugRenderer.setColor(new Color(0.2f, 0.75f, 0.2f, 1));
			debugRenderer.rect(player.getPosition().x, player.getPosition().y,
					1f, 2f);
			debugRenderer.setColor(new Color(0, 1, 1, 1));
		}
		debugRenderer.end();
	}

	public OrthographicCamera getCamera() {
		return cam;
	}
	
	public void setLight(float intensity){
		this.lightIntensity = intensity;
	}

}
