package com.mygdx.sigil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameObject {
    private Texture image;
    private Rectangle rect;
    private boolean isKinetic;
    private Array<GameObject> collisions;
    private float mass;

    boolean isGrounded;
    boolean isDraggable, beingDragged;
    public boolean canCollide;
    public static boolean isDragOccuring;

    private float dx, ddx, dy, ddy, ppdx, ppdy;

    Vector3 touchPos;
    Vector2 dragOffset;
    Vector2 originalLocation;

    static Array<GameObject> gameObjects = new Array<GameObject>();

    //constants
    static float g = 0.3f;

    public GameObject(Texture img, float x, float y, boolean isKinetic) {
        this.image = img;
        this.rect = new Rectangle();
        rect.x = x;
        rect.y = y;
        rect.width = img.getWidth();
        rect.height = img.getHeight();
        this.isKinetic = isKinetic;


        collisions = new Array<GameObject>();
    }

    public void render(SpriteBatch batch) {
        //batch.draw(image, rect.x-(rect.width/2), rect.y-(rect.height/2));
        update();

        batch.draw(image, rect.x, rect.y);
    }

    public void update() {
        if (!beingDragged) {

            dx = 0;

            System.out.println(Math.round(getY()));
            if (!isGrounded()) {
                dy -= g;
            } else {
                dy = 0;
            }

            //Pixel perfect collision
            ppdx = dx;
            ppdy = dy;
            while (!(ppdx == 0 && ppdy == 0)) {
                //not very elegant, but readable.
                if (Math.abs(ppdx) < 1) {
                    move(ppdx, 0);
                    ppdx = 0;
                }
                if (ppdx > 0) {
                    move(1, 0);
                    ppdx --;
                    if (isColliding()) {
                        move(-1, 0);
                        ppdx = 0;
                    }
                } else if (ppdx < 0) {
                    move(-1, 0);
                    ppdx ++;
                    if (isColliding()) {
                        move(1, 0);
                        ppdx = 0;
                    }
                }

                if (Math.abs(ppdy) < 1) {
                    move(0, ppdy);
                    ppdy = 0;
                }
                if (ppdy > 0) {
                    move(0, 1);
                    ppdy --;
                    if (isColliding()) {
                        move(0, -1);
                        ppdy = 0;
                    }
                } else if (ppdy < 0) {
                    move(0, -1);
                    ppdy ++;
                    if (isGrounded()) {
                        move(0, 1);
                        ppdy = 0;
                        dy = 0;
                    }
                }

            }
            //move(dx, dy);
        }
        drag();

        //System.out.println(isGrounded);
    }

    public void drag() {
        if (Gdx.input.isTouched() && isDraggable && (beingDragged || !isDragOccuring)) {
            touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            SigilGame.camera.unproject(touchPos);

            if (rect.contains(new Vector2(touchPos.x, touchPos.y))) {
                if (!beingDragged) originalLocation = new Vector2(getX(), getY());
                dragOffset = new Vector2(touchPos.x - getX(), touchPos.y - getY());
                beingDragged = true;
                isDragOccuring = true;
            }
            if (beingDragged) {
                setLocation(touchPos.x - dragOffset.x, touchPos.y - dragOffset.y);
            }
        } else {
            if (beingDragged) {
                if (isColliding()) {
                    setLocation(originalLocation.x, originalLocation.y);
                }

                beingDragged = false;
                isDragOccuring = false;
            }
        }
    }

    public Array<GameObject> getCollisions() {
        collisions.clear();
        for (int i = 0; i < gameObjects.size; i ++) {
            if (this.rect.overlaps(gameObjects.get(i).getRect()) && gameObjects.get(i) != this) {
                collisions.add(gameObjects.get(i));
            }
        }
        return collisions;
    }

    public boolean isColliding() {
        return !getCollisions().isEmpty();
    }

    //TODO: fix this so that it only detects stuff below it
    public boolean isGrounded() {
        if (!isKinetic) {
            return true;
        }
        System.out.println("///////");
        for (GameObject o: getCollisions()) {
            if (Math.round(o.getY()) + Math.round(o.getRect().getHeight()) <= Math.round(this.getY()) + 1) {
                System.out.println("grounded");
                return true;
            }
        }
        return false;
    }

    public float getX() {
        return rect.x;
    }
    public float getY() {
        return rect.y;
    }
    public void setX(float x) {
        rect.x = x;
    }
    public void setY(float y) {
        rect.y = y;
    }
    public void setLocation(float x, float y) {
        setX(x);
        setY(y);
    }
    public void move(float dx, float dy) {
        setLocation(getX() + dx, getY() + dy);
    }

    public Rectangle getRect() {
        return this.rect;
    }


}
