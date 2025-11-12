package com.syifa.frontend.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class VerticalLaser extends BaseObstacle {
    public VerticalLaser(Vector2 startPosition, int length) {
        super(startPosition, length);
    }

    @Override
    public void initialize(Vector2 startPosition, int length) {
        super.initialize(startPosition, length);
    }

    @Override
    protected void updateCollider() {
        collider = new Rectangle(position.x, position.y, WIDTH, length);
    }

    @Override
    protected void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(position.x, position.y, WIDTH, length);
    }

    @Override
    protected float getRenderWidth() {
        return WIDTH;
    }
}
