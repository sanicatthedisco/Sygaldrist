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

	private Texture smileyImg, blockImg;
	public static OrthographicCamera camera;

	GameObject smiley, block;

	final int WIDTH = 768;
	final int HEIGHT = 480;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		for (int i = 0; i < WIDTH/GrassBlock.grassBlockImage.getWidth() + 1; i ++) {
			GameObject.gameObjects.add(new GrassBlock(i * GrassBlock.grassBlockImage.getWidth(), 0));
		}
		GameObject.gameObjects.add(new StoneBlock(400, 400));

	}

	@Override
	public void render () {
		/*
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			smiley.setX(smiley.getX() - (200 * Gdx.graphics.getDeltaTime()));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			smiley.setX(smiley.getX() + (200 * Gdx.graphics.getDeltaTime()));
		}*/

		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			GameObject.gameObjects.add(new StoneBlock(400, 400));
		}

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
