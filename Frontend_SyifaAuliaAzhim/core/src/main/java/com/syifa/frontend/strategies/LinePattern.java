package com.syifa.frontend.strategies;

import com.syifa.frontend.Coin;
import com.syifa.frontend.factories.CoinFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinePattern implements CoinPattern {

    private static final float SPACING = 40f;
    private final Random random = new Random();

    @Override
    public List<Coin> spawn(CoinFactory factory, float groundTopY, float spawnX, float screenHeight) {

        int count = 3 + random.nextInt(3);

        float minY = groundTopY + 50f;
        float maxY = screenHeight - 100f;

        float startY = minY + random.nextFloat() * (maxY - minY);

        List<Coin> coins = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Coin c = factory.obtainCoin(spawnX + i * SPACING, startY);
            coins.add(c);
        }

        return coins;
    }

    @Override
    public String getName() {
        return "Line";
    }
}
