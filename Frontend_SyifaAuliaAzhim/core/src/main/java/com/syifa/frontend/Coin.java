package com.syifa.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {

    private Vector2 position;
    private Rectangle collider;
    private float radius = 15f;
    private boolean active;

    private float bobOffset = 0f;
    private float bobSpeed = 2f; // bisa kamu sesuaikan

    public Coin(Vector2 startPosition) {
        this.position = new Vector2(startPosition);
        this.active = false;
        this.collider = new Rectangle(position.x - radius, position.y - radius, radius * 2, radius * 2);
    }

    public void update(float delta) {
        if (!active) return;

        bobOffset += bobSpeed * delta;

        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);
        collider.setPosition(position.x - radius, drawY - radius);
    }

    public void renderShape(ShapeRenderer renderer) {
        if (!active) return;

        float drawY = position.y + (float)(Math.sin(bobOffset) * 5f);

        renderer.setColor(1f, 1f, 0f, 1f);
        renderer.circle(position.x, drawY, radius);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return active && collider.overlaps(playerCollider);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        collider.setPosition(x - radius, y - radius);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
