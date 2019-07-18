package com.mygdx.sigil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Iterator;

//TODO
//These are the general next steps we need in the game:
//Add more types of blocks
//Create runes as gameobjects
//Allow runes to be added to blocks
//Give blocks properties and express them in terms of energy
//Create pop up menus for choosing, crafting, and adding (engraving?) runes into blocks


public class SigilGame extends ApplicationAdapter {
	private SpriteBatch batch;

	private Texture smileyImg, blockImg, uleImg, uleImg_s, dochImg;
	public static OrthographicCamera camera;
	public static float globalScale = 1.3f;

	private Stage stage;
	private Table rootTable, popupMenuTable;
	private Stack stack;
	private Image uleIcon, dochIcon;
	PopupSpawnMenu popupSpawnMenu;

	GameObject smiley, block;

	@Override
	public void create () {
		uleImg = new Texture("runes/64w/ule.png");
		dochImg = new Texture("runes/64w/doch.png");
		uleImg_s = new Texture("runes/20w/ule.png");

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		rootTable = new Table();
		rootTable.setFillParent(true);
		stage.addActor(rootTable);

		popupSpawnMenu = new PopupSpawnMenu();
		popupMenuTable = popupSpawnMenu.getRoot();
		rootTable.add(popupMenuTable).pad(10);
		rootTable.right().top();



		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / globalScale,
				Gdx.graphics.getHeight() / globalScale);
		camera.position.x = Gdx.graphics.getWidth()/2;
		camera.position.y = Gdx.graphics.getHeight()/2;

		for (int i = 0; i < Gdx.graphics.getWidth()/GrassBlock.grassBlockImage.getWidth() + 1; i ++) {
			GameObject.gameObjects.add(new GrassBlock(i * GrassBlock.grassBlockImage.getWidth(), 0));
		}
		StoneBlock stoneBlock = new StoneBlock(400, 400);
		GameObject.gameObjects.add(stoneBlock);

		GameObject.gameObjects.add((new Rune(uleImg, uleImg_s, 0, 0).attachTo(stoneBlock, new Vector2(0, 0))));
		GameObject.gameObjects.add((new Rune(uleImg, uleImg_s, 0, 0).attachTo(stoneBlock, new Vector2(1, 0))));
		GameObject.gameObjects.add((new Rune(uleImg, uleImg_s, 0, 0).attachTo(stoneBlock, new Vector2(-1, -1))));
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			GameObject.gameObjects.add(new StoneBlock(400, 400));
		}

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (GameObject o: GameObject.gameObjects) {
			o.render(batch);
		}
		batch.end();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void dispose () {
		batch.dispose();
		smileyImg.dispose();
	}
}
