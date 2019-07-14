package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Rune extends GameObject {
    public Block hostObject;
    public boolean hasHost;
    private Vector2 gridCoords;

    Texture smallImage, regularImage;


    public Rune (Texture img, float x, float y) {
        super(img, x, y, false);

        hasHost = false;
        gridCoords = new Vector2(0, 0);

        smallImage = new Texture("runes/20w/ule_small.png");
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

    public void doRuneAbility() {};
}
