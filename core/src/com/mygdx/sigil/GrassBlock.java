package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;

public class GrassBlock extends Block {
    static Texture grassBlockImage = new Texture("blocks/grass_block.png");

    public GrassBlock(float x, float y) {
        super(grassBlockImage, x, y, false, false);
        isDraggable = false;
    }
}
