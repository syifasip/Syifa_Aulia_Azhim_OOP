package com.syifa.frontend.pools;

import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.HomingMissile;

public class HomingMissilePool extends ObjectPool<HomingMissile> {

    @Override
    protected HomingMissile createObject() {
        return new HomingMissile(new Vector2());
    }

    @Override
    protected void resetObject(HomingMissile missile) {
        missile.setActive(false);
        missile.setPosition(0, 0);
        missile.setTarget(null);
    }

    public HomingMissile obtain(Vector2 position) {
        HomingMissile missile = super.obtain();
        missile.initialize(position, 0);
        missile.setActive(true);
        return missile;
    }
}
