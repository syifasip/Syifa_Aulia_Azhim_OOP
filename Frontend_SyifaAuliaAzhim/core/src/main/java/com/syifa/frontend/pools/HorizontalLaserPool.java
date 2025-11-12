package com.syifa.frontend.pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.HorizontalLaser;

public class HorizontalLaserPool extends ObjectPool<HorizontalLaser> {

    @Override
    protected HorizontalLaser createObject() {
        return new HorizontalLaser(new Vector2(0, 0), 100);
    }

    @Override
    protected void resetObject(HorizontalLaser obstacle) {
        obstacle.setPosition(Gdx.graphics.getWidth(), 0);
        obstacle.setActive(false);
    }

    public HorizontalLaser obtain(Vector2 position, int length) {
        HorizontalLaser obstacle = super.obtain();
        obstacle.initialize(position, length);
        obstacle.setActive(true);
        return obstacle;
    }
}
