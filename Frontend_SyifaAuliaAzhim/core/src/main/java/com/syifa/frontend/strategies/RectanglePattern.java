package com.syifa.frontend.strategies;

import com.syifa.frontend.Coin;
import com.syifa.frontend.factories.CoinFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RectanglePattern implements CoinPattern {

    private static final float SPACING_X = 40f;
    private static final float SPACING_Y = 40f;

    private final Random random = new Random();

    @Override
    public List<Coin> spawn(CoinFactory factory, float groundTopY, float spawnX, float screenHeight) {

        int cols = 3 + random.nextInt(2);
        int rows = 2 + random.nextInt(2);

        float minY = groundTopY + 50f;
        float maxY = screenHeight - 100f;

        float startY = minY + random.nextFloat() * (maxY - minY);

        List<Coin> coins = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                float x = spawnX + (col * SPACING_X);
                float y = startY + (row * SPACING_Y);

                Coin c = factory.obtainCoin(x, y);
                coins.add(c);
            }
        }

        return coins;
    }

    @Override
    public String getName() {
        return "Rectangle";
    }
}
