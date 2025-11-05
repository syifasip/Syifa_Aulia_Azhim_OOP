package com.syifa.frontend.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseObstacle {
    protected Vector2 position;
    protected Rectangle collider;
    protected float length;
    protected final float WIDTH = 10f;
    protected boolean active = false;

    public BaseObstacle(Vector2 startPosition, int length) {
        this.position = startPosition;
        this.length = length;
        updateCollider();
    }
    public void Initialize(Vector2 startPosition, int length) {
        this.position = startPosition;
        this.length = length;
        updateCollider();
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (!active) return;
        drawShape(shapeRenderer);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return active && collider.overlaps(playerCollider);
    }

    public boolean isActive() {
        return active;
    }
    public boolean isOffScreenCamera(float cameraLeftEdge) {
        return (position.x + getRenderWidth()) < cameraLeftEdge;
    }

    protected abstract void updateCollider();
    protected abstract void drawShape(ShapeRenderer shapeRenderer);
    public abstract float getRenderWidth();
    public abstract float getRenderHeight();

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        updateCollider();
    }
    public Vector2 getPosition() {
        return position;
    }


}
