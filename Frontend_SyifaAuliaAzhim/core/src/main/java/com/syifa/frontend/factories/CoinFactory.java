package com.syifa.frontend.factories;

import com.syifa.frontend.Coin;
import com.syifa.frontend.pools.CoinPool;

import java.util.ArrayList;
import java.util.List;

public class CoinFactory {

    public final CoinPool coinPool;
    private final List<Coin> activeCoins = new ArrayList<>();

    public CoinFactory() {
        this.coinPool = new CoinPool();
    }

    public Coin obtainCoin(float x, float y) {
        Coin c = coinPool.obtain(x, y);
        activeCoins.add(c);
        return c;
    }

    public List<Coin> getActiveCoins() {
        return activeCoins;
    }

    public void releaseCoin(Coin coin) {
        coinPool.free(coin);
        activeCoins.remove(coin);
    }

    public void releaseAll() {
        for (Coin c : new ArrayList<>(activeCoins)) {
            coinPool.free(c);
        }
        activeCoins.clear();
    }
}
