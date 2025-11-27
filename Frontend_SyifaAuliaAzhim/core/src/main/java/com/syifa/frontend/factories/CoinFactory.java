package com.syifa.frontend.factories;

import com.syifa.frontend.Coin;
import com.syifa.frontend.pools.CoinPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoinFactory {

    private CoinPool coinPool;
    private Random random;
    private List<Coin> activeCoins;

    public CoinFactory(CoinPool pool) {
        this.coinPool = pool;
        this.random = new Random();
        this.activeCoins = new ArrayList<>();
    }

    public void createCoinPattern(float spawnX, float groundTopY) {
        // 30% chance
        if (random.nextFloat() > 0.3f) return;

        for (int i = 0; i < 3; i++) {
            Coin coin = coinPool.obtain(spawnX + i * 40f, groundTopY + 60f);
            activeCoins.add(coin);
        }
    }

    public List<Coin> getActiveCoins() {
        return activeCoins;
    }

    public void releaseCoin(Coin coin) {
        coinPool.free(coin);
        activeCoins.remove(coin);
    }
}
