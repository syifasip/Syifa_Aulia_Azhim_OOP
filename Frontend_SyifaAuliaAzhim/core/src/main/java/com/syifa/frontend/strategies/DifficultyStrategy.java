package com.syifa.frontend.strategies;

import java.util.Map;

public interface DifficultyStrategy {
    Map<String, Integer> getObstacleWeights();
    float getSpawnInterval();
    int getDensity();
    float getMinGap();
}
