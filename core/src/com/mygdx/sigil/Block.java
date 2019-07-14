package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Block extends GameObject {
    public Array<Rune> attachedRunes;
    public int gridSize;

    public Block(Texture img, float x, float y, boolean isKinetic) {
        super(img, x, y, isKinetic);

        gridSize = 3;
    }
}
