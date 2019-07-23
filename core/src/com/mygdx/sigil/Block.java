package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Block extends GameObject {
    public Array<Rune> attachedRunes;
    public int gridSize;
    public boolean menuOpen, canBeInscribed;
    public InscribeMenu im;
    static boolean isMenuOpenGlobal = false;

    public Block(Texture img, float x, float y, boolean isKinetic, boolean inscribable) {
        super(img, x, y, isKinetic);

        canBeInscribed = inscribable;

        if (canBeInscribed) {
            im = new InscribeMenu(this, 3);
            GameObject.gameObjects.add(im);

            gridSize = 3;
            attachedRunes = new Array<Rune>();
            menuOpen = false;
            im.hide();
        }

    }

    public void additionalUpdate() {
        //open menu when dragged over by a rune, and stay open until drag ends.
        if (!getCollisionsWithClass(Rune.class).isEmpty() && canBeInscribed && !menuOpen) {
            for (GameObject r: getCollisionsWithClass(Rune.class)) {
                if (!((Rune) r).hasHost) {
                    openMenu();
                }
            }


        }
    }

    public void openMenu() {
        if (!isMenuOpenGlobal) {
            menuOpen = true;
            isMenuOpenGlobal = true;
            im.show();
        }

    }

    public void closeMenu() {
        menuOpen = false;
        isMenuOpenGlobal = false;
        im.hide();
    }
}
