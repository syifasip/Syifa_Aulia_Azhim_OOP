package com.syifa.frontend;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {
    private Vector2 position;
    private Rectangle collider;
    private float radius = 15f;
    private boolean active;

    private float bobOffset = 0f;
    private float bobSpeed = 2f;

    public Coin(Vector2 startPosition) {
        this.position = startPosition;
        this.collider = new Rectangle(position.x - radius, position.y - radius, radius * 2, radius * 2);
        this.active = false;
    }

    public void update(float delta) {
        if (!active) return;

        bobOffset += bobSpeed * delta;

        collider.setPosition(position.x - radius, position.y - radius);
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        if (!active) return;

        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        shapeRenderer.setColor(1f, 1f, 0f, 1f);
        shapeRenderer.circle(position.x, drawY, radius);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return active && collider.overlaps(playerCollider);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.bobOffset = 0f;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
