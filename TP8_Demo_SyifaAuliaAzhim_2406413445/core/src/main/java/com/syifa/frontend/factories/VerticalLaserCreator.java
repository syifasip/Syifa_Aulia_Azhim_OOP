package com.syifa.frontend.factories;

import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.*;
import com.syifa.frontend.pools.*;

public class VerticalLaserCreator implements ObstacleFactory.ObstacleCreator {

    private final VerticalLaserPool pool = new VerticalLaserPool();
    private static final float MIN_HEIGHT = 100f;
    private static final float MAX_HEIGHT = 300f;

    @Override
    public BaseObstacle create(float groundTopY, float spawnX, float playerHeight, Random rng) {
        int height = (int) (MIN_HEIGHT + rng.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT));
        float minY = groundTopY + playerHeight;
        float maxY = Gdx.graphics.getHeight() - playerHeight;
        float randomY = minY + rng.nextFloat() * Math.max(0, maxY - minY);

        return pool.obtain(new Vector2(spawnX, randomY), height);
    }

    @Override
    public void release(BaseObstacle obstacle) {
        if (obstacle instanceof VerticalLaser) {
            pool.release((VerticalLaser) obstacle);
        }
    }

    @Override
    public void releaseAll() {
        pool.releaseAll();
    }

    @Override
    public List<? extends BaseObstacle> getInUse() {
        return pool.getInUse();
    }

    @Override
    public boolean supports(BaseObstacle obstacle) {
        return obstacle instanceof VerticalLaser;
    }

    @Override
    public String getName() {
        return "VerticalLaser";
    }
}
