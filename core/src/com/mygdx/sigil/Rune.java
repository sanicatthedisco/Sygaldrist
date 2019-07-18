package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Rune extends GameObject {
    public Block hostObject;
    public boolean hasHost;
    private Vector2 gridCoords;

    Texture smallImage, regularImage;


    public Rune (Texture img, Texture smallImg, float x, float y) {
        super(img, x, y, false);

        hasHost = false;
        gridCoords = new Vector2(0, 0);

        smallImage = smallImg;
        regularImage = getImage();
    }

    public void update() {
        if (hasHost) {
            scale.set(0.9f, 0.9f);
            setImage(smallImage);
            setLocation(hostObject.getX() + (hostObject.getRect().getWidth()/2) + //center
                            (hostObject.getImage().getWidth() / hostObject.gridSize * gridCoords.x //offset by coords
                            - (getScaledWidth()/2)),
                    hostObject.getY() + (hostObject.getRect().getHeight()/2) +
                            (hostObject.getImage().getHeight() / hostObject.gridSize * gridCoords.y)
                            - (getScaledHeight()/2));
        } else {
            scale.set(1, 1);
            drag();
            setImage(regularImage);
        }

        doRuneAbility();
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
        scale.set(0.9f, 0.9f);
        setImage(smallImage);
        return this;
    }

    public Rune detach() {
        if (!hasHost) {
            return this;
        }
        hostObject.attachedRunes.removeValue(this, true);
        scale.set(1, 1);
        setImage(regularImage);
        return this;
    }

    public void doRuneAbility() {};
}
