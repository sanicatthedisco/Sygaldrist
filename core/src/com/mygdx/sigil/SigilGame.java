package com.mygdx.sigil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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

	private Texture smileyImg, blockImg, uleImg;
	public static OrthographicCamera camera;
	public static float globalScale = 1.3f;

	GameObject smiley, block;

	@Override
	public void create () {
		batch = new SpriteBatch();

		uleImg = new Texture("runes/64w/ule.png");

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

		Rune ule = new Rune(uleImg, 200, 200);
		ule.hasHost = true;
		ule.hostObject = stoneBlock;
		GameObject.gameObjects.add(ule);

	}

	@Override
	public void render () {
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			GameObject.gameObjects.add(new StoneBlock(400, 400));
		}

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (GameObject o: GameObject.gameObjects) {
			o.render(batch);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		smileyImg.dispose();
	}
}
