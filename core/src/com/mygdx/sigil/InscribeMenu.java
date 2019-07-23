package com.mygdx.sigil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;

public class InscribeMenu extends GameObject {
    Block hostObject;
    GameObject closeButton;
    Vector2 hostOffset;
    int size;

    Vector2 scaledSize;

    Texture frameImg;
    final float frameOffset = 18;
    final float lineWidth = 5;

    public Array<CellSnapArea> cellSnapAreas;
    public float snapAreaSize;

    Array<GameObject> runeCollisions;

    public InscribeMenu(Block host, int size) {
        super(new Texture("smiley.png"), 0, 0, false);

        hostOffset = new Vector2(60, 60);

        hostObject = host;
        this.size = size;

        scaledSize = new Vector2(hostObject.getImage().getWidth() * size,
                hostObject.getImage().getHeight() * size);

        isCollidable = false;
        rect = new Rectangle(getX(), getY(), scaledSize.x, scaledSize.y);
        setLocation(hostObject.getX() + hostOffset.x, hostObject.getY() + hostOffset.y);

        //need to update location
        closeButton = new InscribeButton(this);
        GameObject.gameObjects.add(closeButton);

        frameImg = new Texture("ui/inscribe_frame.png");

        //sets up a tiny rectangle in each cell to recognize the rune
        cellSnapAreas = new Array<CellSnapArea>();
        snapAreaSize = (scaledSize.x/this.size)/2;
        for (int y = 0; y < this.size; y ++) {
            for (int x = 0; x < this.size; x ++) {
                System.out.println(x);
                cellSnapAreas.add(new CellSnapArea(this, new Vector2(x, y), snapAreaSize));
            }
        }

    }

    public void render(SpriteBatch batch) {
        update();
        setLocation(hostObject.getX() + hostOffset.x, hostObject.getY() + hostOffset.y);
        if (!hidden) {
            Color c = batch.getColor();
            batch.setColor(c.r, c.g, c.b, 0.4f);
            batch.draw(hostObject.getImage(), getX(), getY(), //draws background as host's texture
                    scaledSize.x, scaledSize.y);
            batch.setColor(c.r, c.g, c.b, 1f);
            batch.draw(frameImg, getX() - frameOffset, getY() - frameOffset, frameImg.getWidth(), frameImg.getHeight());
        }

    }

    public void additionalUpdate() {
        //makes sure that the csas follow the grid
        for (CellSnapArea csa: cellSnapAreas) {
            csa.updatePosition();
        }

        if (!hidden) {
            runeCollisions = getCollisionsWithClass(Rune.class);
            //find all runes that are over this menu (unattached) and check to see if they are on a snap point
            if (!runeCollisions.isEmpty()) {
                for (GameObject o : runeCollisions) {
                    Rune r = (Rune) o;
                    if (!r.hasHost && !r.beingDragged) {
                        for (CellSnapArea csa : cellSnapAreas) {
                            if (csa.getRect().overlaps(r.getRect())) {
                                r.attachTo(hostObject, new Vector2(csa.gridCoords.x - 1, csa.gridCoords.y - 1));
                                break;
                            }
                        }
                    }
                }
            }

            //if a rune is attached but outside its normal bounds, detach it
            for (Rune r: hostObject.attachedRunes) {
                for (CellSnapArea csa: cellSnapAreas) {
                    if (csa.gridCoords.x == r.gridCoords.x + 1 && csa.gridCoords.y == r.gridCoords.y + 1) {
                        if (!csa.getRect().overlaps(r.getRect())) {
                            r.detach();
                        }
                    }
                }
            }
        }
    }

    public void shapeRender(ShapeRenderer sr) {
        if (!hidden) {
            sr.begin(ShapeType.Filled);
            sr.setColor(Color.BLACK);
            sr.rect(getX() + (scaledSize.x / size) - (lineWidth/2), getY(), lineWidth, scaledSize.y);
            sr.rect(getX() + (scaledSize.x * 2 / size) - (lineWidth/2), getY(), lineWidth, scaledSize.y);
            sr.rect(getX(), getY() + (scaledSize.y / size) - (lineWidth/2), scaledSize.x, lineWidth);
            sr.rect(getX(), getY() + (scaledSize.y * 2 / size) - (lineWidth/2), scaledSize.x, lineWidth);
            sr.end();
        }
    }

    public void hide() {
        hidden = true;
        closeButton.hidden = true;
    }

    public void show() {
        hidden = false;
        closeButton.hidden = false;

        //puts each attached rune in the proper place on the grid
        for (Rune r: hostObject.attachedRunes) {
            r.setLocation(getX() + ((r.gridCoords.x + 1) * (scaledSize.x/size)) + (((scaledSize.x/size)-r.getScaledWidth())/2),
                    getY() + ((r.gridCoords.y + 1) * (scaledSize.y/size)) + (((scaledSize.y/size)-r.getScaledHeight())/2));
        }
    }
}

class InscribeButton extends GameObject {
    InscribeMenu p;
    public InscribeButton(InscribeMenu parent) {
        super(new Texture("ui/exit_button.png"), 0, 0, false);
        p = parent;
    }

    public void update() {
        super.update();
        setLocation(p.getX() + p.scaledSize.x - getImage().getWidth(),
                p.getY() + p.scaledSize.y - getImage().getHeight());
        isCollidable = false;
        if (containsMouse() && Gdx.input.isTouched() && !hidden) {
            p.hostObject.closeMenu();
        }
    }
}

class CellSnapArea {
    InscribeMenu p;
    private Rectangle rect;
    Vector2 gridCoords; //corner is (0, 0)
    float size;

    public CellSnapArea(InscribeMenu parent, Vector2 gridCoordinates, float size) {
        //super(new Texture("smiley.png"), 0, 0, false);
        p = parent;
        rect = new Rectangle();
        this.size = size;

        gridCoords = gridCoordinates;
        updatePosition();
    }

    public void updatePosition() {
        rect.setX(p.getX() + (gridCoords.x * (p.scaledSize.x/p.size) + (((p.scaledSize.x/p.size) - this.size)/2)));
        rect.setY(p.getY() + (gridCoords.y * (p.scaledSize.y/p.size) + (((p.scaledSize.y/p.size) - this.size)/2)));
        rect.setWidth(this.size);
        rect.setHeight(this.size);
    }

    public Rectangle getRect() {
        return this.rect;
    }
}