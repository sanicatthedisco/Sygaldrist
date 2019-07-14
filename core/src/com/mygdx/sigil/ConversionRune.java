package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;

public class ConversionRune extends Rune {
    public int input, output;

    public ConversionRune(Texture img, float x, float y, int input, int output) {
        super(img, x, y);

        this.input = input;
        this.output = output;
    }

    public void doRuneAbility() {
    }
}
