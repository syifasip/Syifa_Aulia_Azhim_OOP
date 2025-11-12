package com.syifa.frontend.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.obstacles.BaseObstacle;
import com.syifa.frontend.obstacles.VerticalLaser;
import com.syifa.frontend.pools.VerticalLaserPool;
import java.util.List;
import java.util.Random;

class VerticalLaserCreator implements ObstacleFactory.ObstacleCreator {
    private static final float MIN_HEIGHT = 100f;
    private static final float MAX_HEIGHT = 300f;

    private final VerticalLaserPool pool = new VerticalLaserPool();

    @Override
    public BaseObstacle create(float groundTopY, float spawnX, float playerHeight, Random rng) {
        float obstacleHeight = MIN_HEIGHT + (rng.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT));

        float minY = groundTopY + playerHeight;
        float maxY = Gdx.graphics.getHeight() - obstacleHeight - playerHeight;
        if (maxY < minY) maxY = minY;
        float randomY = minY + rng.nextFloat() * Math.max(0, maxY - minY);

        return pool.obtain(new Vector2(spawnX, randomY), (int) obstacleHeight);
    }

    @Override
    public void release(BaseObstacle obstacle) {
        if (obstacle instanceof VerticalLaser) pool.release((VerticalLaser) obstacle);
    }

    @Override
    public void releaseAll() { pool.releaseAll(); }

    @Override
    public List<VerticalLaser> getInUse() { return pool.getInUse(); }

    @Override
    public boolean supports(BaseObstacle obstacle) { return obstacle instanceof VerticalLaser; }

    @Override
    public String getName() { return "VerticalLaser"; }
}

