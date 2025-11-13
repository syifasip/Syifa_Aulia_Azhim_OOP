package com.syifa.frontend.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HorizontalLaser extends BaseObstacle {
    public HorizontalLaser(Vector2 startPosition, int length) {
        super(startPosition, length);
    }

    @Override
    public void initialize(Vector2 startPosition, int length) {
        super.initialize(startPosition, length);
    }

    @Override
    protected void updateCollider() {
        collider = new Rectangle(position.x, position.y, length, WIDTH);
    }

    @Override
    protected void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(position.x, position.y, length, WIDTH);
    }

    @Override
    protected float getRenderWidth() {
        return length;
    }
}
