package net.groupfive.murderdesk.view;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;

public class IsometricScaledTiledMapRenderer extends BatchTiledMapRenderer {
	private float[] vertices = new float[20];
	
	private float unitScaleX = 1f;
	private float unitScaleY = 1f;
	
	public IsometricScaledTiledMapRenderer(TiledMap map) {
		super(map);
	}
	
	public IsometricScaledTiledMapRenderer(TiledMap map, float unitScaleX, float unitScaleY) {
		super(map);
		this.unitScaleX = unitScaleX;
		this.unitScaleY = unitScaleY;
	}

	public IsometricScaledTiledMapRenderer(TiledMap map, Batch batch) {
		super(map, batch);
	}

	public IsometricScaledTiledMapRenderer(TiledMap map, float unitScale) {
		super(map);
		this.unitScaleX = unitScale;
		this.unitScaleY = unitScale;
	}	

	public IsometricScaledTiledMapRenderer(TiledMap map, float unitScale, Batch batch) {
		super(map, unitScale, batch);
	}

	@Override
	public void renderObject (MapObject object) {
		
	}

	@Override
	public void renderTileLayer (TiledMapTileLayer layer) {
		final Color batchColor = spriteBatch.getColor();
		final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());
	
		int col1 = 0;
		int col2 = layer.getWidth() - 1;

		int row1 = 0;
		int row2 = layer.getHeight() - 1;

		float tileWidth = layer.getTileWidth() * unitScaleX;
		float tileHeight = layer.getTileHeight() * unitScaleY;
		float halfTileWidth = tileWidth * 0.5f;
		float halfTileHeight = tileHeight * 0.5f;

		for (int row = row2; row >= row1; row--) {
			for (int col = col1; col <= col2; col++) {
				float x = (col * halfTileWidth) + (row * halfTileWidth);
				float y = (row * halfTileHeight) - (col * halfTileHeight);

				final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
				if(cell == null) continue;
				final TiledMapTile tile = cell.getTile();

				if (tile != null) {
					final boolean flipX = cell.getFlipHorizontally();
					final boolean flipY = cell.getFlipVertically();
					final int rotations = cell.getRotation();

					TextureRegion region = tile.getTextureRegion();

					float x1 = x;
					float y1 = y;
					float x2 = x1 + region.getRegionWidth() * unitScaleX;
					float y2 = y1 + region.getRegionHeight() * unitScaleY;

					float u1 = region.getU();
					float v1 = region.getV2();
					float u2 = region.getU2();
					float v2 = region.getV();

					vertices[X1] = x1;
					vertices[Y1] = y1;
					vertices[C1] = color;
					vertices[U1] = u1;
					vertices[V1] = v1;

					vertices[X2] = x1;
					vertices[Y2] = y2;
					vertices[C2] = color;
					vertices[U2] = u1;
					vertices[V2] = v2;

					vertices[X3] = x2;
					vertices[Y3] = y2;
					vertices[C3] = color;
					vertices[U3] = u2;
					vertices[V3] = v2;

					vertices[X4] = x2;
					vertices[Y4] = y1;
					vertices[C4] = color;
					vertices[U4] = u2;
					vertices[V4] = v1;

					if (flipX) {
						float temp = vertices[U1];
						vertices[U1] = vertices[U3];
						vertices[U3] = temp;
						temp = vertices[U2];
						vertices[U2] = vertices[U4];
						vertices[U4] = temp;
					}
					if (flipY) {
						float temp = vertices[V1];
						vertices[V1] = vertices[V3];
						vertices[V3] = temp;
						temp = vertices[V2];
						vertices[V2] = vertices[V4];
						vertices[V4] = temp;
					}
					if (rotations != 0) {
						switch (rotations) {
							case Cell.ROTATE_90: {
								float tempV = vertices[V1];
								vertices[V1] = vertices[V2];
								vertices[V2] = vertices[V3];
								vertices[V3] = vertices[V4];
								vertices[V4] = tempV;

								float tempU = vertices[U1];
								vertices[U1] = vertices[U2];
								vertices[U2] = vertices[U3];
								vertices[U3] = vertices[U4];
								vertices[U4] = tempU;
								break;
							}
							case Cell.ROTATE_180: {
								float tempU = vertices[U1];
								vertices[U1] = vertices[U3];
								vertices[U3] = tempU;
								tempU = vertices[U2];
								vertices[U2] = vertices[U4];
								vertices[U4] = tempU;
								float tempV = vertices[V1];
								vertices[V1] = vertices[V3];
								vertices[V3] = tempV;
								tempV = vertices[V2];
								vertices[V2] = vertices[V4];
								vertices[V4] = tempV;
								break;
							}
							case Cell.ROTATE_270: {
								float tempV = vertices[V1];
								vertices[V1] = vertices[V4];
								vertices[V4] = vertices[V3];
								vertices[V3] = vertices[V2];
								vertices[V2] = tempV;

								float tempU = vertices[U1];
								vertices[U1] = vertices[U4];
								vertices[U4] = vertices[U3];
								vertices[U3] = vertices[U2];
								vertices[U2] = tempU;
								break;
							}
						}
					}
					spriteBatch.draw(region.getTexture(), vertices, 0, 20);
				}
			}
		}
	}
}
