package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Rune extends GameObject {
    public Block hostObject;
    public boolean hasHost;
    public Vector2 gridCoords;
    public IconRune icon; //represents this rune on its block's texture


    public Rune (Texture img, Texture smallImg, float x, float y) {
        super(img, x, y, false);

        hasHost = false;
        gridCoords = new Vector2(0, 0);
        isDraggable = true;
        isCollidable = false;
        scale.set(0.9f, 0.9f);

        icon = new IconRune(smallImg, this);
        GameObject.gameObjects.add(icon);
    }

    public void update() {
        if (hasHost) {
            if (hostObject.menuOpen) {
                hidden = false;
                //setLocation(hostObject)
            } else {
                hidden = true;
            }
        } else {
            hidden = false;
        }

    }

    public Rune attachTo(Block block, Vector2 position) {
        for (int i = 0; i < block.attachedRunes.size; i ++) {
            if (block.attachedRunes.get(i).gridCoords.equals(position)) {
                System.out.println("You cant put a rune on top of another rune dumbfuck");
                return this;
            }
        }

        block.attachedRunes.add(this);
        hasHost = true;
        hostObject = block;
        gridCoords.set(position);
        isCollidable = false;

        //snaps to grid when attached (should probs refactor to have a local reference to im)
        setLocation(hostObject.im.getX() + ((gridCoords.x + 1) * (hostObject.im.scaledSize.x/hostObject.im.size)) +
                        (((hostObject.im.scaledSize.x/hostObject.im.size)-getScaledWidth())/2),
                hostObject.im.getY() + ((gridCoords.y + 1) * (hostObject.im.scaledSize.y/hostObject.im.size)) +
                        (((hostObject.im.scaledSize.y/hostObject.im.size)-getScaledHeight())/2));

        return this;
    }

    public Rune detach() {
        if (!hasHost) {
            return this;
        }
        hostObject.attachedRunes.removeValue(this, true);
        hasHost = false;
        return this;
    }

}
