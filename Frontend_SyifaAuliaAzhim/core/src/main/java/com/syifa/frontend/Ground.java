package com.syifa.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

public class Ground {
    // tinggi dasar tanah, hanya dapat diakses di dalam class ini
    private static final float GROUND_HEIGHT = 50f;

    // collider hanya dapat diakses di dalam class ini
    private Rectangle collider;

    // constructor dapat diakses dimana saja
    public Ground() {
        float width = Gdx.graphics.getWidth() * 2f;
        collider = new Rectangle(0, 0, width, GROUND_HEIGHT);
    }

    // update agar selalu berada di depan kamera dan menutupi layar
    public void update(float cameraX) {
        float screenWidth = Gdx.graphics.getWidth();
        float groundX = cameraX - screenWidth / 2f - 500f;
        collider.x = groundX;
        collider.y = 0f;
        collider.width = screenWidth + 1000f; // memastikan menutupi layar + margin
        // height tetap GROUND_HEIGHT
        collider.height = GROUND_HEIGHT;
    }

    // cek tabrakan dengan player collider
    public boolean isColliding(Rectangle playerCollider) {
        return collider.overlaps(playerCollider);
    }

    // getter tinggi atas tanah
    public float getTopY() {
        return GROUND_HEIGHT;
    }

    // render ground menggunakan ShapeRenderer
    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
        shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
    }
}
