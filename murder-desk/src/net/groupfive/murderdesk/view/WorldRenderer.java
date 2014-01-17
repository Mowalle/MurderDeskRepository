package net.groupfive.murderdesk.view;

import net.groupfive.murderdesk.model.BloodTrap;
import net.groupfive.murderdesk.model.Door;
import net.groupfive.murderdesk.model.ElectroTrap;
import net.groupfive.murderdesk.model.FloodTrap;
import net.groupfive.murderdesk.model.FreezeTrap;
import net.groupfive.murderdesk.model.GasTrap;
import net.groupfive.murderdesk.model.Player;
import net.groupfive.murderdesk.model.Player.DeathType;
import net.groupfive.murderdesk.model.Room;
import net.groupfive.murderdesk.model.SpikeTrap;
import net.groupfive.murderdesk.model.Trap;
import net.groupfive.murderdesk.model.TrapdoorTrap;
import net.groupfive.murderdesk.model.World;
import net.groupfive.murderdesk.model.Player.State;

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
import com.badlogic.gdx.math.Vector2;

public class WorldRenderer {

	private static final float CAMERA_WIDTH = 10f;
	private static final float CAMERA_HEIGHT = 12.5f;
	private static final float WALKING_FRAME_DURATION = 0.1f;
	private static final float DEATH_FRAME_DURATION = 1f;

	private static final float PLAYER_OFFSET_X = (1f - Player.SIZE_X) / 2f;
	private static final float PLAYER_OFFSET_Y = (2f - Player.SIZE_Y) / 2f;

	private static final float TRAP_OFFSET_X = 0f;
	private static final float TRAP_OFFSET_Y = -0.5f;

	private float splitYHalf = (52f / 32f);

	private World world;
	private OrthographicCamera cam;

	/** For debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	/** Custom renderer for map rendering **/
	CustomIsometricTiledMapRenderer isoRenderer;

	/** Textures **/
	private Texture playerSpriteSheet;
	private TextureRegion playerIdleLeftDown;
	private TextureRegion playerIdleLeftUp;
	private TextureRegion playerIdleRightDown;
	private TextureRegion playerIdleRightUp;

	private TextureRegion deathHangingLeftDown;
	private TextureRegion deathHangingLeftUp;
	private TextureRegion deathHangingRightDown;
	private TextureRegion deathHangingRightUp;

	private TextureRegion[] deathElecLeft;
	private TextureRegion[] deathElecRight;

	private TextureRegion[] deathLeftDownFrames;
	private TextureRegion[] deathLeftUpFrames;
	private TextureRegion[] deathRightDownFrames;
	private TextureRegion[] deathRightUpFrames;
	
	private TextureRegion[] deathGoreLeftDownFrames;
	private TextureRegion[] deathGoreLeftUpFrames;
	private TextureRegion[] deathGoreRightDownFrames;
	private TextureRegion[] deathGoreRightUpFrames;

	private TextureRegion playerFrame;
	private TextureRegion playerFrameTop;
	private TextureRegion playerFrameBottom;

	private Texture trapdoorTrapSprite;
	private TextureRegion trapdoorClosed;
	private TextureRegion trapdoorOpened;

	private Texture floodTrapSprite;
	private TextureRegion[] floodTrapFrames;

	private Texture bloodTrapSprite;
	private TextureRegion[] bloodTrapFrames;
	private Texture bloodRainSprite;
	private TextureRegion[] bloodRainFrames;

	private Texture electroTrapSprite;
	private Texture electroTrapBase;
	private TextureRegion[] electroFieldFrames;

	private Texture spikeTrapSprite;
	private TextureRegion[] spikeTrapFrames;

	private Texture gasTrapSprite;
	private Texture freezeTrapSprite;

	// General container for current trap frame
	private TextureRegion trapFrame = new TextureRegion();

	private Texture doorSprite;
	TextureRegion doorExit;
	TextureRegion doorDefault;
	TextureRegion doorMetal;

	private TextureRegion doorFrame = new TextureRegion();

	/** Animations **/
	private Animation walkLeftDownAnimation;
	private Animation walkLeftUpAnimation;
	private Animation walkRightDownAnimation;
	private Animation walkRightUpAnimation;

	private Animation deathLeftDownAnimation;
	private Animation deathLeftUpAnimation;
	private Animation deathRightDownAnimation;
	private Animation deathRightUpAnimation;

	private Animation deathGoreLeftDownAnimation;
	private Animation deathGoreLeftUpAnimation;
	private Animation deathGoreRightDownAnimation;
	private Animation deathGoreRightUpAnimation;

	private Animation deathElecLeftAnimation;
	private Animation deathElecRightAnimation;

	private Animation bloodRainAnimation;
	private Animation electroFieldAnimation;

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
		isoRenderer = new CustomIsometricTiledMapRenderer(world
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

		deathHangingLeftDown = frames[5][0];
		deathHangingLeftUp = frames[5][2];
		deathHangingRightDown = frames[5][1];
		deathHangingRightUp = frames[5][3];

		deathLeftDownFrames = new TextureRegion[2];
		deathLeftDownFrames[0] = frames[7][1];
		frames[7][2] = new TextureRegion(playerSpriteSheet,
				frames[7][2].getRegionX(), frames[7][2].getRegionY(),
				frames[7][2].getRegionWidth() * 2,
				frames[7][2].getRegionHeight());
		deathLeftDownFrames[1] = frames[7][2];
		deathLeftDownAnimation = new Animation(DEATH_FRAME_DURATION, deathLeftDownFrames);

		deathLeftUpFrames = new TextureRegion[2];
		deathLeftUpFrames[0] = frames[9][1];
		frames[9][2] = new TextureRegion(playerSpriteSheet,
				frames[9][2].getRegionX(), frames[9][2].getRegionY(),
				frames[9][2].getRegionWidth() * 2,
				frames[9][2].getRegionHeight());
		deathLeftUpFrames[1] = frames[9][2];
		deathLeftUpAnimation = new Animation(DEATH_FRAME_DURATION, deathLeftUpFrames);

		deathRightDownFrames = new TextureRegion[2];
		deathRightDownFrames[0] = frames[6][1];
		frames[6][2] = new TextureRegion(playerSpriteSheet,
				frames[6][2].getRegionX(), frames[6][2].getRegionY(),
				frames[6][2].getRegionWidth() * 2,
				frames[6][2].getRegionHeight());
		deathRightDownFrames[1] = frames[6][2];
		deathRightDownAnimation = new Animation(DEATH_FRAME_DURATION, deathRightDownFrames);

		deathRightUpFrames = new TextureRegion[2];
		deathRightUpFrames[0] = frames[8][1];
		frames[8][2] = new TextureRegion(playerSpriteSheet,
				frames[8][2].getRegionX(), frames[8][2].getRegionY(),
				frames[8][2].getRegionWidth() * 2,
				frames[8][2].getRegionHeight());
		deathRightUpFrames[1] = frames[8][2];
		deathRightUpAnimation = new Animation(DEATH_FRAME_DURATION, deathRightUpFrames);
		
		deathGoreLeftDownFrames = new TextureRegion[2];
		deathGoreLeftDownFrames[0] = frames[11][1];
		frames[11][2] = new TextureRegion(playerSpriteSheet,
				frames[11][2].getRegionX(), frames[11][2].getRegionY(),
				frames[11][2].getRegionWidth() * 2,
				frames[11][2].getRegionHeight());
		deathGoreLeftDownFrames[1] = frames[11][2];
		deathGoreLeftDownAnimation = new Animation(DEATH_FRAME_DURATION, deathGoreLeftDownFrames);

		deathGoreLeftUpFrames = new TextureRegion[2];
		deathGoreLeftUpFrames[0] = frames[13][1];
		frames[13][2] = new TextureRegion(playerSpriteSheet,
				frames[13][2].getRegionX(), frames[13][2].getRegionY(),
				frames[13][2].getRegionWidth() * 2,
				frames[13][2].getRegionHeight());
		deathGoreLeftUpFrames[1] = frames[13][2];
		deathGoreLeftUpAnimation = new Animation(DEATH_FRAME_DURATION, deathGoreLeftUpFrames);

		deathGoreRightDownFrames = new TextureRegion[2];
		deathGoreRightDownFrames[0] = frames[10][1];
		frames[10][2] = new TextureRegion(playerSpriteSheet,
				frames[10][2].getRegionX(), frames[10][2].getRegionY(),
				frames[10][2].getRegionWidth() * 2,
				frames[10][2].getRegionHeight());
		deathGoreRightDownFrames[1] = frames[10][2];
		deathGoreRightDownAnimation = new Animation(DEATH_FRAME_DURATION, deathGoreRightDownFrames);

		deathGoreRightUpFrames = new TextureRegion[2];
		deathGoreRightUpFrames[0] = frames[12][1];
		frames[12][2] = new TextureRegion(playerSpriteSheet,
				frames[12][2].getRegionX(), frames[12][2].getRegionY(),
				frames[12][2].getRegionWidth() * 2,
				frames[12][2].getRegionHeight());
		deathGoreRightUpFrames[1] = frames[12][2];
		deathGoreRightUpAnimation = new Animation(DEATH_FRAME_DURATION, deathGoreRightUpFrames);

		deathElecLeft = new TextureRegion[2];
		deathElecLeft[0] = frames[14][0];
		deathElecLeft[1] = frames[14][1];
		deathElecLeftAnimation = new Animation(DEATH_FRAME_DURATION, deathElecLeft);

		deathElecRight = new TextureRegion[2];
		deathElecRight[0] = frames[14][2];
		deathElecRight[1] = frames[14][3];
		deathElecRightAnimation = new Animation(DEATH_FRAME_DURATION, deathElecRight);

		// * Trap textures ***************************//
		trapdoorTrapSprite = new Texture(
				Gdx.files.internal("textures/TrapDoor.png"));
		trapdoorClosed = new TextureRegion(trapdoorTrapSprite, 0, 0, 128, 64);
		trapdoorOpened = new TextureRegion(trapdoorTrapSprite, 0, 64, 128, 64);

		floodTrapSprite = new Texture(
				Gdx.files.internal("textures/floodTrap.png"));
		floodTrapFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			floodTrapFrames[i] = new TextureRegion(floodTrapSprite, 0, 480 * i,
					960, 480);
		}
		bloodTrapSprite = new Texture(
				Gdx.files.internal("textures/bloodTrap.png"));
		bloodTrapFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			bloodTrapFrames[i] = new TextureRegion(bloodTrapSprite, 0, 480 * i,
					960, 480);
		}

		bloodRainSprite = new Texture(
				Gdx.files.internal("textures/bloodRain.png"));
		bloodRainFrames = new TextureRegion[2];
		bloodRainFrames[0] = new TextureRegion(bloodRainSprite, 0, 0, 960, 480);
		bloodRainFrames[1] = new TextureRegion(bloodRainSprite, 0, 480, 960,
				480);

		bloodRainAnimation = new Animation(0.25f, bloodRainFrames);

		gasTrapSprite = new Texture(Gdx.files.internal("textures/GasTrap.png"));

		freezeTrapSprite = new Texture(
				Gdx.files.internal("textures/FreezeTrap.png"));

		electroTrapSprite = new Texture(
				Gdx.files.internal("textures/electroTrap.png"));
		electroTrapBase = new Texture(
				Gdx.files.internal("textures/electricTrapBase.png"));
		electroFieldFrames = new TextureRegion[2];
		electroFieldFrames[0] = new TextureRegion(electroTrapSprite, 0, 0, 64,
				32);
		electroFieldFrames[1] = new TextureRegion(electroTrapSprite, 0, 32, 64,
				32);

		electroFieldAnimation = new Animation(0.25f, electroFieldFrames);

		spikeTrapSprite = new Texture(
				Gdx.files.internal("textures/SpikeTrap.png"));
		spikeTrapFrames = new TextureRegion[2];
		spikeTrapFrames[0] = new TextureRegion(spikeTrapSprite, 0, 0, 64, 37);
		spikeTrapFrames[1] = new TextureRegion(spikeTrapSprite, 64, 0, 64, 37);

		// * Door textures ***************************//
		doorSprite = new Texture(Gdx.files.internal("textures/doorSprites.png"));
		doorExit = new TextureRegion(doorSprite, 0, 0, 108, 73);
		doorDefault = new TextureRegion(doorSprite, 108, 0, 108, 73);
		doorMetal = new TextureRegion(doorSprite, 216, 0, 108, 73);
		trapdoorOpened = new TextureRegion(trapdoorTrapSprite, 0, 64, 128, 64);

		// Light //
		light = new ShapeRenderer();
	}

	public void render() {

		cam.update();

		// True because player over layers
		drawRoom(true);

		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();

		if (world.getCurrentRoom().hasDoors())
			drawDoor();

		Trap trap = null;
		if (world.getCurrentRoom().hasTraps()) {
			trap = world.getCurrentRoom().getCurrentTrap();

			splitYHalf = (52f / 32f);

			if (trap instanceof FloodTrap) {
				splitYHalf = (52f / 32f)
						* (1 - ((FloodTrap) trap).getWaterLevel() / 4f);
			} else if (trap instanceof BloodTrap) {
				splitYHalf = (52f / 32f)
						* (1 - ((BloodTrap) trap).getBloodLevel() / 4f);
			} else if (trap instanceof GasTrap || trap instanceof FreezeTrap) {
				splitYHalf = 0f;
			}
		}

		// Player should only be rendered if he is inside the room that is
		// currently displayed
		if (world.getCurrentRoom().equals(world.getPlayer().getMyRoom())) {
			drawPlayer(false);
		}

		// FIXME Draw some traps over walkBehind, some under player
		if (world.getCurrentRoom().hasTraps())
			drawTrap();

		// Player should only be rendered if he is inside the room that is
		// currently displayed
		if (world.getCurrentRoom().equals(world.getPlayer().getMyRoom())) {
			drawPlayer(true);
		}

		spriteBatch.end();

		// False because layers over player
		drawRoom(false);

		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		light.begin(ShapeType.Filled);
		light.setColor(0, 0, 0, 1 - lightIntensity);
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
				// TODO Hardcoded value
				if (!map.getLayers().get(i).getName()
						.equalsIgnoreCase("WalkBehind")
						&& map.getLayers().get(i).isVisible()) {
					isoRenderer.renderTileLayer((TiledMapTileLayer) map
							.getLayers().get(i));
				}
			}
		} else {
			for (int i = 0; i < map.getLayers().getCount(); i++) {
				if (map.getLayers().get(i).getName()
						.equalsIgnoreCase("WalkBehind")
						&& map.getLayers().get(i).isVisible()) {
					isoRenderer.renderTileLayer((TiledMapTileLayer) map
							.getLayers().get(i));
				}
			}
		}
		isoRenderer.getSpriteBatch().end();
	}

	private void drawDoor() {

		float doorOffsetX = 0f;
		float doorOffsetY = 0f;

		for (Door door : world.getCurrentRoom().getDoors()) {

			if (door.getType().equals(Door.Type.DEFAULT)) {
				if (!door.isOpen()) {
					if (door.isFacingLeft()) {
						doorFrame.setRegion(doorDefault, 0, 0, 27, 73);
						doorOffsetX = 4f / 64f;
						doorOffsetY = 3f / 64f;
					} else {
						doorFrame.setRegion(doorDefault, 27, 0, 27, 73);
						doorOffsetX = 0.5f + 2f / 64f;
						doorOffsetY = 0f + 2f / 64f;
					}
				} else {
					if (door.isFacingLeft()) {
						doorFrame.setRegion(doorDefault, 54, 0, 27, 73);
						doorOffsetX = 4f / 64f;
						doorOffsetY = 3f / 64f;
					} else {
						doorFrame.setRegion(doorDefault, 81, 0, 27, 73);
						doorOffsetX = 0.5f + 2f / 64f;
						doorOffsetY = 0f + 2f / 64f;
					}
				}
			} else if (door.getType().equals(Door.Type.EXIT)) {
				if (!door.isOpen()) {
					if (door.isFacingLeft()) {
						doorFrame.setRegion(doorExit, 0, 0, 27, 73);
						doorOffsetX = 4f / 64f;
						doorOffsetY = 3f / 64f;
					} else {
						doorFrame.setRegion(doorExit, 27, 0, 27, 73);
						doorOffsetX = 0.5f + 2f / 64f;
						doorOffsetY = 0f + 2f / 64f;
					}
				} else {
					if (door.isFacingLeft()) {
						doorFrame.setRegion(doorExit, 54, 0, 27, 73);
						doorOffsetX = 4f / 64f;
						doorOffsetY = 3f / 64f;
					} else {
						doorFrame.setRegion(doorExit, 81, 0, 27, 73);
						doorOffsetX = 0.5f + 2f / 64f;
						doorOffsetY = 0f + 2f / 64f;
					}
				}
			} else if (door.getType().equals(Door.Type.METAL)) {
				if (!door.isOpen()) {
					if (door.isFacingLeft()) {
						doorFrame.setRegion(doorMetal, 54, 0, 27, 73);
						doorOffsetX = 3f / 64f;
						doorOffsetY = 5f / 64f;
					} else {
						doorFrame.setRegion(doorMetal, 81, 0, 27, 73);
						doorOffsetX = 0.5f + 2f / 64f;
						doorOffsetY = 0f + 4f / 64f;
					}
				} else {
					if (door.isFacingLeft()) {
						doorFrame.setRegion(doorMetal, 0, 0, 27, 73);
						doorOffsetX = 2f / 64f;
						doorOffsetY = 3f / 64f;
					} else {
						doorFrame.setRegion(doorMetal, 27, 0, 27, 73);
						doorOffsetX = 0.5f + 2f / 64f;
						doorOffsetY = 0f + 4f / 64f;
					}
				}
			}

			// TODO Hardcoded values
			spriteBatch.draw(doorFrame, door.getPosition().x + doorOffsetX,
					door.getPosition().y + doorOffsetY, 27f / 64f, 73f / 32f);
		}

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

		} else if (trap instanceof FloodTrap) {
			if (trap.isActive()) {
				trapFrame = floodTrapFrames[((FloodTrap) trap).getWaterLevel()];

				spriteBatch.draw(trapFrame,
						trap.getMyRoom().getBoundingBox().x, trap.getMyRoom()
								.getBoundingBox().y, trap.getMyRoom()
								.getBoundingBox().width, trap.getMyRoom()
								.getBoundingBox().height);
			}
		} else if (trap instanceof BloodTrap) {
			if (trap.isActive()) {
				trapFrame = bloodTrapFrames[((BloodTrap) trap).getBloodLevel()];

				spriteBatch.draw(trapFrame,
						trap.getMyRoom().getBoundingBox().x, trap.getMyRoom()
								.getBoundingBox().y, trap.getMyRoom()
								.getBoundingBox().width, trap.getMyRoom()
								.getBoundingBox().height);

				trapFrame = bloodRainAnimation.getKeyFrame(trap.getStateTime(),
						true);
				spriteBatch.draw(trapFrame,
						trap.getMyRoom().getBoundingBox().x, trap.getMyRoom()
								.getBoundingBox().y, trap.getMyRoom()
								.getBoundingBox().width, trap.getMyRoom()
								.getBoundingBox().height);
			}
		} else if (trap instanceof GasTrap) {
			if (trap.isActive()) {
				trapFrame = new TextureRegion(gasTrapSprite);

				Color tmp = spriteBatch.getColor();
				spriteBatch.setColor(tmp.r, tmp.g, tmp.b,
						((GasTrap) trap).getGasLevel() / 10f);
				spriteBatch.draw(trapFrame,
						trap.getMyRoom().getBoundingBox().x, trap.getMyRoom()
								.getBoundingBox().y, trap.getMyRoom()
								.getBoundingBox().width, trap.getMyRoom()
								.getBoundingBox().height);
				spriteBatch.setColor(tmp);
			}
		} else if (trap instanceof FreezeTrap) {
			if (trap.isActive()) {
				trapFrame = new TextureRegion(freezeTrapSprite);

				Color tmp = spriteBatch.getColor();
				spriteBatch.setColor(tmp.r, tmp.g, tmp.b,
						((FreezeTrap) trap).getFreezeLevel() / 10f);
				spriteBatch.draw(trapFrame,
						trap.getMyRoom().getBoundingBox().x, trap.getMyRoom()
								.getBoundingBox().y, trap.getMyRoom()
								.getBoundingBox().width, trap.getMyRoom()
								.getBoundingBox().height);
				spriteBatch.setColor(tmp);
			}
		} else if (trap instanceof ElectroTrap) {
			trapFrame = new TextureRegion(electroTrapBase);
			spriteBatch.draw(trapFrame, trap.getMyRoom().getBoundingBox().x,
					trap.getMyRoom().getBoundingBox().y, trap.getMyRoom()
							.getBoundingBox().width, trap.getMyRoom()
							.getBoundingBox().height);

			if (trap.isActive()) {
				trapFrame = electroFieldAnimation.getKeyFrame(
						trap.getStateTime(), true);

				for (Vector2 v : ((ElectroTrap) trap).getElectroFields()) {
					spriteBatch.draw(trapFrame, v.x, v.y, 1f, 1f);
				}
			}
		} else if (trap instanceof SpikeTrap) {

			trapFrame = spikeTrapFrames[0];

			for (Vector2 v : ((SpikeTrap) trap).getSpikeFields()) {
				spriteBatch.draw(trapFrame, v.x, v.y + 0.125f, 1f, 1f);
			}

			if (trap.isActive()) {
				trapFrame = spikeTrapFrames[1];

				for (Vector2 v : ((SpikeTrap) trap).getSpikeFields()) {
					spriteBatch.draw(trapFrame, v.x, v.y + 0.125f, 1f, 1f);
				}
			}
		}
	}

	private void drawPlayer(boolean topHalf) {
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

		float deathOffsetX = 0;
		float deathOffsetY = 0;
		// Normal Death Animation
		if (player.getState().equals(State.DEAD)) {

			if (player.getDeathType().equals(DeathType.NORMAL)) {

				if (player.isFacingLeft()) {
					if (player.isFacingDown()) {
						playerFrame = deathLeftDownAnimation.getKeyFrame(
								player.getStateTime(), false);
					} else {
						playerFrame = deathLeftUpAnimation.getKeyFrame(
								player.getStateTime(), false);
					}
				} else {
					if (player.isFacingDown()) {
						playerFrame = deathRightDownAnimation.getKeyFrame(
								player.getStateTime(), false);
					} else {
						playerFrame = deathRightUpAnimation.getKeyFrame(
								player.getStateTime(), false);
					}
				}
				deathOffsetX = -PLAYER_OFFSET_X + 0.5f;
				deathOffsetY = -PLAYER_OFFSET_Y - 0.5f;

			}
			
			if (player.getDeathType().equals(DeathType.BLOODY)) {

				if (player.isFacingLeft()) {
					if (player.isFacingDown()) {
						playerFrame = deathGoreLeftDownAnimation.getKeyFrame(
								player.getStateTime(), false);
					} else {
						playerFrame = deathGoreLeftUpAnimation.getKeyFrame(
								player.getStateTime(), false);
					}
				} else {
					if (player.isFacingDown()) {
						playerFrame = deathGoreRightDownAnimation.getKeyFrame(
								player.getStateTime(), false);
					} else {
						playerFrame = deathGoreRightUpAnimation.getKeyFrame(
								player.getStateTime(), false);
					}
				}
				deathOffsetX = -PLAYER_OFFSET_X + 0.5f;
				deathOffsetY = -PLAYER_OFFSET_Y - 0.5f;

			}
			
			if (player.getDeathType().equals(DeathType.TRAPDOOR)) {

				if (player.isFacingLeft()) {
					if (player.isFacingDown()) {
						playerFrame = deathHangingLeftDown;
					} else {
						playerFrame = deathHangingLeftUp;
					}
				} else {
					if (player.isFacingDown()) {
						playerFrame = deathHangingRightDown;
					} else {
						playerFrame = deathHangingRightUp;
					}
				}
				deathOffsetX = -PLAYER_OFFSET_X + 0.5f;
				deathOffsetY = -PLAYER_OFFSET_Y - 0.5f;

			}

			if (player.getDeathType().equals(DeathType.ELECTROCUTION)) {

				if (!player.isFacingLeft()) {
					playerFrame = deathElecLeftAnimation.getKeyFrame(
							player.getStateTime(), true);
				} else {
					playerFrame = deathElecRightAnimation.getKeyFrame(
							player.getStateTime(), true);
				}
			}
		}

		// if (player.isFacingLeft()) {
		// if (player.isFacingDown()) {
		// playerFrame = deathLeftDownAnimation.getKeyFrame(
		// player.getStateTime(), false);
		// } else {
		// playerFrame = deathLeftUpAnimation.getKeyFrame(
		// player.getStateTime(), false);
		// }
		// } else {
		// if (player.isFacingDown()) {
		// playerFrame = deathRightDownAnimation.getKeyFrame(
		// player.getStateTime(), false);
		// } else {
		// playerFrame = deathRightUpAnimation.getKeyFrame(
		// player.getStateTime(), false);
		// }
		// }

		if (topHalf) {
			playerFrameTop = new TextureRegion(playerFrame, 0, 0,
					(int) playerFrame.getRegionWidth(),
					(int) (splitYHalf * 32f));

			spriteBatch.draw(playerFrameTop, player.getPosition().x
					+ PLAYER_OFFSET_X + deathOffsetX, player.getPosition().y
					+ PLAYER_OFFSET_Y + deathOffsetY
					+ (Player.SIZE_Y - splitYHalf),
					playerFrameTop.getRegionWidth() / 64f,
					playerFrameTop.getRegionHeight() / 32f);
		} else {
			playerFrameBottom = new TextureRegion(playerFrame, 0,
					(int) (splitYHalf * 32f),
					(int) playerFrame.getRegionWidth(),
					(int) (Player.SIZE_Y * 32f - splitYHalf * 32f));

			spriteBatch.draw(playerFrameBottom, player.getPosition().x
					+ PLAYER_OFFSET_X + deathOffsetX, player.getPosition().y
					+ PLAYER_OFFSET_Y + deathOffsetY,
					playerFrameBottom.getRegionWidth() / 64f,
					playerFrameBottom.getRegionHeight() / 32f);
		}

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

		if (world.getCurrentRoom().hasDoors()) {
			for (Door door : world.getCurrentRoom().getDoors()) {
				if (!door.isOpen()) {
					debugRenderer.setColor(new Color(0.5f, 0.5f, 0, 1));
				} else {
					debugRenderer.setColor(new Color(1f, 1f, 0, 1));
				}
				debugRenderer.rect(door.getPosition().x, door.getPosition().y,
						Door.SIZE_X, Door.SIZE_Y);
			}
		}

		debugRenderer.end();
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public void setLight(float intensity) {
		this.lightIntensity = intensity;
	}

}
