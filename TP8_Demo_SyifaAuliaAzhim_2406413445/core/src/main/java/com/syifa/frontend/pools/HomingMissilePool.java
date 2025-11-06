package com.syifa.frontend.pools;

import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.HomingMissile;

public class HomingMissilePool extends ObjectPool<HomingMissile> {

    @Override
    protected HomingMissile createObject() {
        // buat objek baru dengan posisi (0,0)
        return new HomingMissile(new Vector2(0, 0));
    }

    @Override
    protected void resetObject(HomingMissile object) {
        // posisi awal di (0,0)
        object.setPosition(0, 0);
        object.setActive(false);
        object.setTarget(null); // hapus referensi target
    }

    public HomingMissile obtain(Vector2 position) {
        HomingMissile missile = super.obtain();
        missile.initialize(position, 0);
        missile.setActive(true);
        return missile;
    }
}
