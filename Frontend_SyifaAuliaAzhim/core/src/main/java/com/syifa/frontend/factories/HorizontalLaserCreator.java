package com.syifa.frontend.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.BaseObstacle;
import com.syifa.frontend.obstacles.HorizontalLaser;
import com.syifa.frontend.pools.HorizontalLaserPool;
import java.util.List;
import java.util.Random;

class HorizontalLaserCreator implements ObstacleFactory.ObstacleCreator {
    private static final float MIN_LENGTH = 100f;
    private static final float MAX_LENGTH = 300f;

    private final HorizontalLaserPool pool = new HorizontalLaserPool();

    @Override
    public BaseObstacle create(float groundTopY, float spawnX, float playerHeight, Random rng) {
        float obstacleLength = MIN_LENGTH + (rng.nextFloat() * (MAX_LENGTH - MIN_LENGTH));

        float minY = groundTopY + playerHeight;
        float maxY = Gdx.graphics.getHeight() - playerHeight;
        if (maxY < minY) maxY = minY;
        float randomY = minY + rng.nextFloat() * Math.max(0, maxY - minY);

        return pool.obtain(new Vector2(spawnX, randomY), (int) obstacleLength);
    }

    @Override
    public void release(BaseObstacle obstacle) {
        if (obstacle instanceof HorizontalLaser) pool.release((HorizontalLaser) obstacle);
    }

    @Override
    public void releaseAll() { pool.releaseAll(); }

    @Override
    public List<HorizontalLaser> getInUse() { return pool.getInUse(); }

    @Override
    public boolean supports(BaseObstacle obstacle) { return obstacle instanceof HorizontalLaser; }

    @Override
    public String getName() { return "HorizontalLaser"; }
}

