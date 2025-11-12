package com.syifa.frontend.factories;

import java.util.*;
import com.syifa.frontend.obstacles.BaseObstacle;

public class ObstacleFactory {

    /** Factory Method implementor */
    public interface ObstacleCreator {
        BaseObstacle create(float groundTopY, float spawnX, float playerHeight, Random rng);
        void release(BaseObstacle obstacle);
        void releaseAll();
        List<? extends BaseObstacle> getInUse();
        boolean supports(BaseObstacle obstacle);
        String getName();
    }

    /** Weighted creator for probability-based spawning */
    private static class WeightedCreator {
        ObstacleCreator creator;
        int weight;

        WeightedCreator(ObstacleCreator creator, int weight) {
            this.creator = creator;
            this.weight = weight;
        }
    }

    private final List<WeightedCreator> weightedCreators = new ArrayList<>();
    private final Random random = new Random();
    private int totalWeight = 0;

    public ObstacleFactory() {
        // Register creators with weights for spawn probability
        // Vertical: 40%, Horizontal: 40%, Homing Missile: 20%
        register(new VerticalLaserCreator(), 2);
        register(new HorizontalLaserCreator(), 2);
        register(new HomingMissileCreator(), 1);
    }

    public void register(ObstacleCreator creator, int weight) {
        weightedCreators.add(new WeightedCreator(creator, weight));
        totalWeight += weight;
    }

    /** Factory Method using weighted random selection */
    public BaseObstacle createRandomObstacle(float groundTopY, float spawnX, float playerHeight) {
        if (weightedCreators.isEmpty()) {
            throw new IllegalStateException("No obstacle creators registered");
        }

        ObstacleCreator creator = selectWeightedCreator();
        return creator.create(groundTopY, spawnX, playerHeight, random);
    }

    private ObstacleCreator selectWeightedCreator() {
        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (WeightedCreator wc : weightedCreators) {
            currentWeight += wc.weight;
            if (randomValue < currentWeight) {
                return wc.creator;
            }
        }

        return weightedCreators.get(0).creator;
    }

    public void releaseObstacle(BaseObstacle obstacle) {
        for (WeightedCreator wc : weightedCreators) {
            if (wc.creator.supports(obstacle)) {
                wc.creator.release(obstacle);
                return;
            }
        }
    }

    public void releaseAllObstacles() {
        for (WeightedCreator wc : weightedCreators) {
            wc.creator.releaseAll();
        }
    }

    public List<BaseObstacle> getAllInUseObstacles() {
        List<BaseObstacle> list = new ArrayList<>();
        for (WeightedCreator wc : weightedCreators) {
            list.addAll(wc.creator.getInUse());
        }
        return list;
    }

    public List<String> getRegisteredCreatorNames() {
        List<String> names = new ArrayList<>();
        for (WeightedCreator wc : weightedCreators) {
            names.add(wc.creator.getName());
        }
        return names;
    }
}


