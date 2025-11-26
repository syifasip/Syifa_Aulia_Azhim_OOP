package com.syifa.frontend.pools;

import com.badlogic.gdx.utils.Pool;
import com.syifa.frontend.Coin;
import com.badlogic.gdx.math.Vector2;

public class CoinPool extends Pool<Coin> {
    @Override
    protected Coin newObject() {
        return new Coin(new Vector2(0,0));
    }

    @Override
    protected void reset(Coin coin) {
        coin.setActive(false);
    }

    public Coin obtain(float x, float y) {
        Coin coin = super.obtain();
        coin.setPosition(x, y);
        coin.setActive(true);
        return coin;
    }
}
