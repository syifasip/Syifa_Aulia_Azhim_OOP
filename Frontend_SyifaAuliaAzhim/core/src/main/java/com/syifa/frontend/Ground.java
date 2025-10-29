package com.syifa.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Ground {
    private static final float GROUND_HEIGHT = 50f;
    private Rectangle collider;
    public Ground() {
        collider = new Rectangle(0, 0, Gdx.graphics.getWidth() * 2f, GROUND_HEIGHT);
    }
    public void update(float cameraX) {
        float X = cameraX - Gdx.graphics.getWidth() / 2f - 500;
        collider.setPosition(x, 0);
        collider.setWidth(Gdx.graphics.getWidth() * 2f);
    }
    public boolean  isColliding(Rectangle playerCollider) {
        return collider.overlaps(playerCollider);
    }
    public float getTopY() {
        return GROUND_HEIGHT;
    }
    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
        shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
    }
}
