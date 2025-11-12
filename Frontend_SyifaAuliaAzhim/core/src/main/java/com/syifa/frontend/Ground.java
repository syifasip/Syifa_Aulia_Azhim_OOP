package com.syifa.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Ground {
    private static final float GROUND_HEIGHT = 50f;
    private Rectangle collider;

    public Ground() {
        this.collider = new Rectangle(0, 0, Gdx.graphics.getWidth() * 2, GROUND_HEIGHT);
    }

    public void update(float cameraX) {
        float groundWidth = Gdx.graphics.getWidth() * 3;
        this.collider.setPosition(cameraX - Gdx.graphics.getWidth() / 2f - 500, 0);
        this.collider.setWidth(groundWidth + 1000);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return collider.overlaps(playerCollider);
    }

    public float getTopY() {
        return GROUND_HEIGHT;
    }

    // Debug
    public void renderShape(ShapeRenderer shapeRenderer) {
        // Draw ground as gray rectangle
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f); // Gray color
        shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
    }
}
