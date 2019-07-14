package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;

public class StoneBlock extends Block {
    static Texture stoneBlockImage = new Texture("blocks/stone_brick.png");

    public StoneBlock(float x, float y) {
        super(stoneBlockImage, x, y, true);
        isDraggable = true;
        mass = 1;
    }
}
