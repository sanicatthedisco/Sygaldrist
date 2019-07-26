package com.mygdx.sigil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Iterator;

//TODO
//Real shit:

//Be able to rotate runes
//Give runes inputs and outputs based on orientation
//Menu with tools and shortcuts for rotating runes and opening menus

//Give blocks properties and express them in terms of energy

//Other shit:

//Add more types of blocks
//Animations/responses from buttons and other UI elements
//Clean up graphics and resolution (no more shitty global scale)
//Main menu


public class SigilGame extends ApplicationAdapter {
	private SpriteBatch batch;

	static Texture smileyImg, blockImg, uleImg, uleImg_s, dochImg;
	public static OrthographicCamera camera;
	public static float globalScale = 1.3f;

	private Stage stage;
	private Table rootTable, popupMenuTable, popupToolTable;
	private Stack stack;
	private Image uleIcon, dochIcon;
	PopupSpawnMenu popupSpawnMenu;

	GameObject smiley, block;

	ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		uleImg = new Texture("runes/64w/ule.png");
		dochImg = new Texture("runes/64w/doch.png");
		uleImg_s = new Texture("runes/20w/ule.png");
		smileyImg = new Texture("smiley.png");

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		rootTable = new Table();
		rootTable.setFillParent(true);
		stage.addActor(rootTable);

		popupSpawnMenu = new PopupSpawnMenu();
		popupMenuTable = popupSpawnMenu.getRoot();
		rootTable.add(popupMenuTable).pad(10);
		rootTable.right().top();
		rootTable.row();

		popupToolTable = new Table();
		popupToolTable.add(new Image(new Texture("smiley.png")));
		rootTable.add(popupToolTable);


		shapeRenderer = new ShapeRenderer();
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
		stoneBlock.canBeInscribed = true;
		GameObject.gameObjects.add(stoneBlock);
		/*
		GameObject.gameObjects.add((new Rune(uleImg, uleImg_s, 0, 0).attachTo(stoneBlock, new Vector2(0, 0))));
		GameObject.gameObjects.add((new Rune(uleImg, uleImg_s, 0, 0).attachTo(stoneBlock, new Vector2(1, 0))));
		GameObject.gameObjects.add((new Rune(uleImg, uleImg_s, 0, 0).attachTo(stoneBlock, new Vector2(-1, -1))));

		 */

	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			GameObject.gameObjects.add(new StoneBlock(400, 400));
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		stage.act(delta);
		stage.draw();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		//not a great solution, but allows runes to be dragged first.
		for (GameObject o: GameObject.gameObjects) {
			if (o.getClass() == Rune.class) {
				o.drag(false);
			}
		}

		batch.begin();
		for (GameObject o: GameObject.gameObjects) {
			o.render(batch);
		}
		batch.end();

		for (GameObject o: GameObject.gameObjects) {
			o.shapeRender(shapeRenderer);
		}
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		smileyImg.dispose();
	}
}
