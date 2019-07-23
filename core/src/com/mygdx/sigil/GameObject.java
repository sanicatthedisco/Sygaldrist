package com.mygdx.sigil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class GameObject extends Actor {
    private Texture image;
    public Rectangle rect;
    private boolean isKinetic;
    private Array<GameObject> collisions;

    boolean isGrounded;
    public boolean isDraggable, beingDragged;
    public boolean canCollide;
    public static boolean isDragOccuring;

    private float dx, ddx, dy, ddy, ppdx, ppdy;

    Vector3 touchPos;
    Vector2 dragOffset;
    Vector2 originalLocation;
    Vector2 scale;

    static Array<GameObject> gameObjects = new Array<GameObject>();

    public boolean isCollidable;
    public boolean hidden;

    //Material properties
    //0 is right, 3pi/2 is down
    public Array<Vector2> forceVectors = new Array<Vector2>();
    public Vector2 resultantForce;
    public float mass, temp;
    public float KE, Q; //Q is internal energy (heat)
    public float Kb;
    public double heatDecayConstant;

    //World constants
    static final float STP = 273;

    static float g = 0.5f;
    static float airTemp = STP + 25;

    public GameObject(Texture img, float x, float y, boolean isKinetic) {
        this.image = img;
        this.rect = new Rectangle();
        rect.x = x;
        rect.y = y;
        rect.width = img.getWidth();
        rect.height = img.getHeight();
        this.isKinetic = isKinetic;

        resultantForce = new Vector2(0, 0);
        dx = 0;
        mass = 1;
        Kb = 1;
        heatDecayConstant = 0.5;

        collisions = new Array<GameObject>();
        scale = new Vector2(1, 1);

        isCollidable = true;
    }

    public void render(SpriteBatch batch) {
        //batch.draw(image, rect.x-(rect.width/2), rect.y-(rect.height/2));
        update();

        if (!hidden) {
            batch.draw(image, rect.x, rect.y, image.getWidth() * scale.x, image.getHeight() * scale.y);
        }
    }
    public void shapeRender(ShapeRenderer sr) {};

    public void update() {
        forceVectors.clear();
        if (!beingDragged) {
            //Vector stuff
            if (!isGrounded()) {
                forceVectors.add(PolarToRectVector(g * mass, (3/2) * (float) Math.PI)); //Gravity (down)
            } else {
                if (dy < 0) {
                    dy = 0; //debating being extra and adding a normal force
                }
            }

            //converting forces into dy and dx pixel motion
            resultantForce = sumVectorArray(forceVectors);
            ddx = resultantForce.x / mass;
            ddy = resultantForce.y / mass;
            dx += ddx;
            dy += ddy;

            //dx and dy carry over; ddx and ddy do not
            doPixelPerfectCollision();

        }
        drag(false);
        additionalUpdate();
    }
    public void additionalUpdate() {};

    public Vector2 sumVectorArray(Array<Vector2> vectorArray) {
        Vector2 vectorSum = new Vector2(0, 0);
        for (int i = 0; i < vectorArray.size; i ++) {
            vectorSum.add(vectorArray.get(i));
        }
        return vectorSum;
    }

    private void doPixelPerfectCollision() {
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
    }

    public void drag(boolean startDrag) {
        if (Gdx.input.isTouched() && isDraggable && (beingDragged || !isDragOccuring)) {
            touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            SigilGame.camera.unproject(touchPos);

            if ((rect.contains(new Vector2(touchPos.x, touchPos.y))) || startDrag) {
                if (!beingDragged) {
                    originalLocation = new Vector2(getX(), getY());
                    if (startDrag) {
                        dragOffset = new Vector2(0, 0);
                    } else {
                        dragOffset = new Vector2(touchPos.x - getX(), touchPos.y - getY());
                    }
                }

                beingDragged = true;
                isDragOccuring = true;
            }
            if (beingDragged) {
                setLocation(touchPos.x - dragOffset.x, touchPos.y - dragOffset.y);
            }
        } else {
            if (beingDragged) {
                if (isColliding() && isCollidable) {
                    setLocation(originalLocation.x, originalLocation.y);
                }

                beingDragged = false;
                isDragOccuring = false;
            }
        }
    }

    public boolean containsMouse() {
        touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        SigilGame.camera.unproject(touchPos);

        return rect.contains(new Vector2(touchPos.x, touchPos.y));
    }

    private void temperatureDecay() {
        setTemp((float) (temp * Math.pow(Math.E, -heatDecayConstant * Math.abs(temp - airTemp)))); //TODO: fix this
    }

    public static Vector2 PolarToRectVector(float r, float theta) {
        return new Vector2((float)(r * Math.sin(theta)), (float)(r * Math.cos(theta)));
    }

    public Array<GameObject> getCollisions() {
        collisions.clear();
        for (int i = 0; i < gameObjects.size; i ++) {
            if (this.rect.overlaps(gameObjects.get(i).getRect()) && gameObjects.get(i) != this &&
                    gameObjects.get(i).isCollidable && !gameObjects.get(i).hidden) {
                collisions.add(gameObjects.get(i));
            }
        }
        return collisions;
    }

    public Array<GameObject> getCollisionsWithClass(Class cls) {
        Array<GameObject> collisionsWithClass = new Array<GameObject>();
        for (int i = 0; i < gameObjects.size; i ++) {
            if (this.rect.overlaps(gameObjects.get(i).getRect()) && gameObjects.get(i) != this &&
                    gameObjects.get(i).getClass() == cls && !gameObjects.get(i).hidden) {
                collisionsWithClass.add(gameObjects.get(i));
            }
        }
        return collisionsWithClass;
    }

    public boolean isColliding() {
        return !getCollisions().isEmpty();
    }

    //TODO: fix this so that it only detects stuff below it
    public boolean isGrounded() {
        if (!isKinetic) {
            return true;
        }

        for (GameObject o: getCollisions()) {
            if (Math.round(o.getY()) + Math.round(o.getRect().getHeight()) <= Math.round(this.getY()) + 1) {
                //System.out.println("grounded");
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

    public void setTemp(float temp) {
        this.temp = temp;
        this.Q = this.temp * Kb * (3/2);
    }

    public Rectangle getRect() {
        return this.rect;
    }
    public void setRect(Rectangle r) {
        this.rect = r;
    }
    public Texture getImage() {
        return this.image;
    }
    public void setImage(Texture newImage) {
        this.image = newImage;
    }
    public float getScaledWidth() {
        return this.image.getWidth() * scale.x;
    }
    public float getScaledHeight() {
        return this.image.getHeight() * scale.y;
    }
    public void hide() {
        hidden = true;
    }
    public void show() {
        hidden = false;
    }


}
