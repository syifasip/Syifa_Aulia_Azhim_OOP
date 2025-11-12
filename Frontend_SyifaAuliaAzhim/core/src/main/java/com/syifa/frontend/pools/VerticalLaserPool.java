package com.syifa.frontend.pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.VerticalLaser;

public class VerticalLaserPool extends ObjectPool<VerticalLaser> {

    @Override
    protected VerticalLaser createObject() {
        return new VerticalLaser(new Vector2(0, 0), 100);
    }

    @Override
    protected void resetObject(VerticalLaser obstacle) {
        obstacle.setPosition(Gdx.graphics.getWidth(), 0);
        obstacle.setActive(false);
    }

    public VerticalLaser obtain(Vector2 position, int length) {
        VerticalLaser obstacle = super.obtain();
        obstacle.initialize(position, length);
        obstacle.setActive(true);
        return obstacle;
    }
}
