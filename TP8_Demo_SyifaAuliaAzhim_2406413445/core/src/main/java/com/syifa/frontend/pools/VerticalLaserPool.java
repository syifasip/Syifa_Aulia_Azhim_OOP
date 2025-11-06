package com.syifa.frontend.pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.VerticalLaser;

public class VerticalLaserPool extends ObjectPool<VerticalLaser> {

    @Override
    protected VerticalLaser createObject() {
        // sesuai soal: buat objek baru dengan length = 100
        return new VerticalLaser(new Vector2(0, 0), 100);
    }

    @Override
    protected void resetObject(VerticalLaser object) {
        // posisi awal di ujung kanan layar
        object.setPosition(Gdx.graphics.getWidth(), 0);
        object.setActive(false);
    }

    public VerticalLaser obtain(Vector2 position, int length) {
        VerticalLaser laser = super.obtain();
        laser.initialize(position, length);
        laser.setActive(true);
        return laser;
    }
}
