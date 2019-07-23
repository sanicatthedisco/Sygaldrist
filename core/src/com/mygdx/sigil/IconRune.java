package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class IconRune extends GameObject {
    Rune parent;

    public IconRune(Texture img, Rune parent) {
        super(img, 0, 0, false);
        this.parent = parent;

        isCollidable = false;
    }

    public void update() {
        if (parent.hasHost) {
            hidden = false;
            setLocation(parent.hostObject.getX() + (parent.hostObject.getRect().getWidth() / 2) + //center
                            (parent.hostObject.getImage().getWidth() / parent.hostObject.gridSize * parent.gridCoords.x //offset by coords
                                    - (getScaledWidth() / 2)),
                    parent.hostObject.getY() + (parent.hostObject.getRect().getHeight() / 2) +
                            (parent.hostObject.getImage().getHeight() / parent.hostObject.gridSize * parent.gridCoords.y)
                            - (getScaledHeight() / 2));
        } else {
            hidden = true;
        }
    }
}
